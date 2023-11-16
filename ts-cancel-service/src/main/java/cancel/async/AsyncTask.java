package cancel.async;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Future;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import cancel.service.CancelServiceImpl;
import edu.fudan.common.entity.Order;
import edu.fudan.common.entity.OrderStatus;
import edu.fudan.common.util.Response;
import edu.fudan.common.util.StringUtils;

@Component
public class AsyncTask {
    private static final Logger logger = LogManager.getLogger(CancelServiceImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    @Async("asyncTaskExecutor")
    public Future<Boolean> cancelFromOtherOrder(Order order, HttpHeaders headers) {
        // Original code from sync version of CancelServiceImpl.cancelFromOtherOrder()
        logger.info("[function name:{}][order:{}, headers:{}]","cancelFromOtherOrder",(order != null ? order.toString(): null), (headers != null ? headers.toString(): null));
        order.setStatus(OrderStatus.CANCEL.getCode());
        HttpHeaders newHeaders = getAuthorizationHeadersFrom(headers);
        HttpEntity requestEntity = new HttpEntity(order, newHeaders);
        String order_other_service_url = getServiceUrl("ts-order-other-service");
        ResponseEntity<Response> re = restTemplate.exchange(
                order_other_service_url + "/api/v1/orderOtherService/orderOther",
                HttpMethod.PUT,
                requestEntity,
                Response.class);
        logger.info("[status code:{}, url:{}, type:{}, headers:{}]",re.getStatusCode(),
                order_other_service_url + "/api/v1/orderOtherService/orderOther","PUT",headers);
            
        return new AsyncResult<Boolean>(re.getBody().getStatus() == 1);
    }

    @Async("asyncTaskExecutor")
    public Future<Boolean> drawBackMoney(String orderId, String loginId, HttpHeaders headers) {
        // Sleep to simulate network latency, force drawBackMoney() to be after cancelFromOtherOrder()
        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            logger.error("Failed to simulate network latency with Thread.sleep(): {}", e.toString());
        }

        Response<Order> orderOtherResult = getOrderByIdFromOrderOther(orderId, headers);
        Order order = orderOtherResult.getData();

        if (order.getStatus() == OrderStatus.CANCEL.getCode()) {
            return new AsyncResult<Boolean>(false);
        }

        String money = calculateRefund(order);
        boolean status = drawbackMoney(money, loginId, headers);

        return new AsyncResult<Boolean>(status);
    }

    //Original, unmodified code from CancelServiceImpl
    public static HttpHeaders getAuthorizationHeadersFrom(HttpHeaders oldHeaders) {
        HttpHeaders newHeaders = new HttpHeaders();
        if (oldHeaders.containsKey(HttpHeaders.AUTHORIZATION)) {
            newHeaders.add(HttpHeaders.AUTHORIZATION, oldHeaders.getFirst(HttpHeaders.AUTHORIZATION));
        }
        return newHeaders;
    }

    // Original, unmodified code from CancelServiceImpl
    private String getServiceUrl(String serviceName) {
        logger.info("[function name:{}][serviceName:{}]","getServiceUrl",serviceName);
        return "http://" + serviceName;
    }

    // Original, unmodified code from CancelServiceImpl
    private Response<Order> getOrderByIdFromOrderOther(String orderId, HttpHeaders headers) {
        logger.info("[function name:{}][orderId:{}, headers:{}]","getOrderByIdFromOrderOther",orderId, (headers != null ? headers.toString(): null));
        HttpHeaders newHeaders = getAuthorizationHeadersFrom(headers);
        HttpEntity requestEntity = new HttpEntity(newHeaders);
        String order_other_service_url = getServiceUrl("ts-order-other-service");
        ResponseEntity<Response<Order>> re = restTemplate.exchange(
                order_other_service_url + "/api/v1/orderOtherService/orderOther/" + orderId,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<Response<Order>>() {
                });
        logger.info("[status code:{}, url:{}, type:{}, headers:{}]",re.getStatusCode(),
                order_other_service_url + "/api/v1/orderOtherService/orderOther/" + orderId,"GET",headers);
        return re.getBody();
    }

    // Original, unmodified code from CancelServiceImpl
    private String calculateRefund(Order order) {
        logger.info("[function name:{}][order:{}]","calculateRefund",(order != null ? order.toString(): null));
        if (order.getStatus() == OrderStatus.NOTPAID.getCode()) {
            return "0.00";
        }
        Date nowDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(StringUtils.String2Date(order.getTravelDate()));
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(StringUtils.String2Date(order.getTravelTime()));
        int hour = cal2.get(Calendar.HOUR);
        int minute = cal2.get(Calendar.MINUTE);
        int second = cal2.get(Calendar.SECOND);
        Date startTime = new Date(year,  //NOSONAR
                month,
                day,
                hour,
                minute,
                second);
        if (nowDate.after(startTime)) {
            logger.warn("[calculateRefund][Cancel Order, Ticket expire refund 0]");
            return "0";
        } else {
            double totalPrice = Double.parseDouble(order.getPrice());
            double price = totalPrice * 0.8;
            DecimalFormat priceFormat = new java.text.DecimalFormat("0.00");
            String str = priceFormat.format(price);
            return str;
        }
    }

    // Original, unmodified code from CancelServiceImpl
    public boolean drawbackMoney(String money, String userId, HttpHeaders headers) {
        logger.info("[function name:{}][money:{}, userId:{}, headers:{}]","drawbackMoney",money, userId, (headers != null ? headers.toString(): null));

        HttpHeaders newHeaders = getAuthorizationHeadersFrom(headers);
        HttpEntity requestEntity = new HttpEntity(newHeaders);
        String inside_payment_service_url = getServiceUrl("ts-inside-payment-service");
        ResponseEntity<Response> re = restTemplate.exchange(
                inside_payment_service_url + "/api/v1/inside_pay_service/inside_payment/drawback/" + userId + "/" + money,
                HttpMethod.GET,
                requestEntity,
                Response.class);
        logger.info("[status code:{}, url:{}, type:{}, headers:{}]",re.getStatusCode(),
                inside_payment_service_url + "/api/v1/inside_pay_service/inside_payment/drawback/" + userId + "/" + money,"GET",headers);
        Response result = re.getBody();

        return result.getStatus() == 1;
    }
}
