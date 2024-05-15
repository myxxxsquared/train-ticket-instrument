package cancel.service;

import edu.fudan.common.entity.NotifyInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import edu.fudan.common.entity.OrderStatus;
import edu.fudan.common.entity.Order;
import edu.fudan.common.entity.SeatClass;
import edu.fudan.common.entity.User;
import edu.fudan.common.util.Response;
import edu.fudan.common.util.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import cancel.async.AsyncTask;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author fdse
 */
@Service
public class CancelServiceImpl implements CancelService { 
    private static final Logger logger = LogManager.getLogger(CancelServiceImpl.class);


    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private DiscoveryClient discoveryClient;
    @Autowired
    private AsyncTask asyncTask;

    String orderStatusCancelNotPermitted = "Order Status Cancel Not Permitted";

    private String getServiceUrl(String serviceName) {
        return "http://" + serviceName;
    }

    @Override
    public Response cancelOrder(String orderId, String loginId, HttpHeaders headers) {

        Response<Order> orderResult = getOrderByIdFromOrder(orderId, headers);
        if (orderResult.getStatus() == 1) {
            Order order =  orderResult.getData();
            if (order.getStatus() == OrderStatus.NOTPAID.getCode()
                    || order.getStatus() == OrderStatus.PAID.getCode() || order.getStatus() == OrderStatus.CHANGE.getCode()) {

                // order.setStatus(OrderStatus.CANCEL.getCode());

                Response changeOrderResult = cancelFromOrder(order, headers);
                // 0 -- not find order   1 - cancel success
                if (changeOrderResult.getStatus() == 1) {
                    //Draw back money
                    String money = calculateRefund(order);
                    boolean status = drawbackMoney(money, loginId, headers);
                    if (status) {



                        Response<User> result = getAccount(loginId, headers);
                        if (result.getStatus() == 0) {
                            return new Response<>(0, "Cann't find userinfo by user id.", null);
                        }
                        NotifyInfo notifyInfo = new NotifyInfo();
                        notifyInfo.setDate(new Date().toString());
                        notifyInfo.setEmail(result.getData().getEmail());
                        notifyInfo.setStartPlace(order.getFrom());
                        notifyInfo.setEndPlace(order.getTo());
                        notifyInfo.setUsername(result.getData().getUserName());
                        notifyInfo.setSeatNumber(order.getSeatNumber());
                        notifyInfo.setOrderNumber(order.getId().toString());
                        notifyInfo.setPrice(order.getPrice());
                        notifyInfo.setSeatClass(SeatClass.getNameByCode(order.getSeatClass()));
                        notifyInfo.setStartTime(order.getTravelTime().toString());

                        // TODO: change to async message serivce
                        sendEmail(notifyInfo, headers);

                    } else {
                        CancelServiceImpl.logger.error("[cancelOrder][Draw Back Money Failed][loginId: {}, orderId: {}]", loginId, orderId);
                    }
                    return new Response<>(1, "Success.", "test not null");
                } else {
                    CancelServiceImpl.logger.error("[cancelOrder][Cancel Order Failed][orderId: {}, Reason: {}]", orderId, changeOrderResult.getMsg());
                    return new Response<>(0, changeOrderResult.getMsg(), null);
                }

            } else {
                return new Response<>(0, orderStatusCancelNotPermitted, null);
            }
        } else {

            Response<Order> orderOtherResult = getOrderByIdFromOrderOther(orderId, headers);
            if (orderOtherResult.getStatus() == 1) {

                Order order =   orderOtherResult.getData();
                if (order.getStatus() == OrderStatus.NOTPAID.getCode()
                        || order.getStatus() == OrderStatus.PAID.getCode() || order.getStatus() == OrderStatus.CHANGE.getCode()) {

                    /*********************** Fault Injection - F1 *************************/
                    // The faulty code assumes that drawbackMoney() happens before cancelFromOtherOrder()
                    // However, this is not guaranteed because of network latency
                    // If the order is cancelled first by cancelFromOtherOrder(), drawbackMoney() will see that order is already cancelled and no refund will be issued 
                    //
                    /*********************** Fault Injection - F11 *************************/
                    // Due to the lack of control like F1, the two microservices may set the value in a wrong sequence.
                    // However, the second microservice that set the value may recheck the value and correct the value.
                    // The recheck process does not always happen.
                    // If two microservices set the value in a wrong sequence but the recheck process does not executed, this fault occurs.
                    Future<Boolean> taskDrawBackMoney = asyncTask.drawBackMoney(orderId, loginId, headers);
                    Future<Boolean> taskCancelOrder = asyncTask.cancelFromOtherOrder(order, headers);

                    while(!taskCancelOrder.isDone() || !taskDrawBackMoney.isDone()) {}
                    boolean drawbackMoneyStatus = false;
                    boolean changeOrderStatus = false;

                    try {
                        drawbackMoneyStatus = taskDrawBackMoney.get();
                        changeOrderStatus = taskCancelOrder.get();
                    } catch (InterruptedException | ExecutionException e) {
                        logger.error("Failed to get async execution result: {}", e.toString());
                    }
                    
                    if (drawbackMoneyStatus == true && changeOrderStatus == true) {
                        return new Response<>(1, "Success.", null);
                    } else {
                        CancelServiceImpl.logger.error("[cancelOrder][Failed][loginId: {}, orderId: {}]", loginId, orderId);
                        String errorMessageString = String.format("Fail.Reason: drawbackMoney %s cancelOrder %s", changeOrderStatus, drawbackMoneyStatus);
                        return new Response<>(0, errorMessageString, null);
                    }
                    /**********************************************************************/
                } else {
                    CancelServiceImpl.logger.warn("[cancelOrder][Cancel Order, Order Status Not Permitted][loginId: {}, orderId: {}]", loginId, orderId);
                    return new Response<>(0, orderStatusCancelNotPermitted, null);
                }
            } else {
                CancelServiceImpl.logger.warn("[cancelOrder][Cancel Order, Order Not Found][loginId: {}, orderId: {}]", loginId, orderId);
                return new Response<>(0, "Order Not Found.", null);
            }
        }
    }

    public boolean sendEmail(NotifyInfo notifyInfo, HttpHeaders headers) {
        HttpHeaders newHeaders = getAuthorizationHeadersFrom(headers);
        HttpEntity requestEntity = new HttpEntity(notifyInfo, newHeaders);
        String notification_service_url = getServiceUrl("ts-notification-service");
        ResponseEntity<Boolean> re = restTemplate.exchange(
                notification_service_url + "/api/v1/notifyservice/notification/order_cancel_success",
                HttpMethod.POST,
                requestEntity,
                Boolean.class);
        return re.getBody();
    }

    @Override
    public Response calculateRefund(String orderId, HttpHeaders headers) {

        Response<Order> orderResult = getOrderByIdFromOrder(orderId, headers);
        if (orderResult.getStatus() == 1) {
            Order order =   orderResult.getData();
            if (order.getStatus() == OrderStatus.NOTPAID.getCode()
                    || order.getStatus() == OrderStatus.PAID.getCode()) {
                if (order.getStatus() == OrderStatus.NOTPAID.getCode()) {
                    return new Response<>(1, "Success. Refoud 0", "0");
                } else {
                    return new Response<>(1, "Success. ", calculateRefund(order));
                }
            } else {
                return new Response<>(0, "Order Status Cancel Not Permitted, Refound error", null);
            }
        } else {

            Response<Order> orderOtherResult = getOrderByIdFromOrderOther(orderId, headers);
            if (orderOtherResult.getStatus() == 1) {
                Order order =   orderOtherResult.getData();
                if (order.getStatus() == OrderStatus.NOTPAID.getCode()
                        || order.getStatus() == OrderStatus.PAID.getCode()) {
                    if (order.getStatus() == OrderStatus.NOTPAID.getCode()) {
                        return new Response<>(1, "Success, Refound 0", "0");
                    } else {
                        return new Response<>(1, "Success", calculateRefund(order));
                    }
                } else {
                    CancelServiceImpl.logger.warn("[Cancel Order][Refund Price, Order Other. Cancel Not Permitted][orderId: {}]", orderId);
                    return new Response<>(0, orderStatusCancelNotPermitted, null);
                }
            } else {
                CancelServiceImpl.logger.error("[Cancel Order][Refund Price][Order not found][orderId: {}]", orderId);
                return new Response<>(0, "Order Not Found", null);
            }
        }
    }

    private String calculateRefund(Order order) {
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
            CancelServiceImpl.logger.warn("[calculateRefund][Cancel Order, Ticket expire refund 0]");
            return "0";
        } else {
            double totalPrice = Double.parseDouble(order.getPrice());
            double price = totalPrice * 0.8;
            DecimalFormat priceFormat = new java.text.DecimalFormat("0.00");
            String str = priceFormat.format(price);
            return str;
        }
    }


    private Response cancelFromOrder(Order order, HttpHeaders headers) {
        order.setStatus(OrderStatus.CANCEL.getCode());
        // add authorization header
        HttpHeaders newHeaders = getAuthorizationHeadersFrom(headers);
        HttpEntity requestEntity = new HttpEntity(order, newHeaders);
        String order_service_url = getServiceUrl("ts-order-service");
        ResponseEntity<Response> re = restTemplate.exchange(
                order_service_url + "/api/v1/orderservice/order",
                HttpMethod.PUT,
                requestEntity,
                Response.class);

        return re.getBody();
    }

    public static HttpHeaders getAuthorizationHeadersFrom(HttpHeaders oldHeaders) {
        HttpHeaders newHeaders = new HttpHeaders();
        if (oldHeaders.containsKey(HttpHeaders.AUTHORIZATION)) {
            newHeaders.add(HttpHeaders.AUTHORIZATION, oldHeaders.getFirst(HttpHeaders.AUTHORIZATION));
        }
        return newHeaders;
    }


    private Response cancelFromOtherOrder(Order order, HttpHeaders headers) {
        order.setStatus(OrderStatus.CANCEL.getCode());
        HttpHeaders newHeaders = getAuthorizationHeadersFrom(headers);
        HttpEntity requestEntity = new HttpEntity(order, newHeaders);
        String order_other_service_url = getServiceUrl("ts-order-other-service");
        ResponseEntity<Response> re = restTemplate.exchange(
                order_other_service_url + "/api/v1/orderOtherService/orderOther",
                HttpMethod.PUT,
                requestEntity,
                Response.class);

        return re.getBody();
    }

    public boolean drawbackMoney(String money, String userId, HttpHeaders headers) {

        HttpHeaders newHeaders = getAuthorizationHeadersFrom(headers);
        HttpEntity requestEntity = new HttpEntity(newHeaders);
        String inside_payment_service_url = getServiceUrl("ts-inside-payment-service");
        ResponseEntity<Response> re = restTemplate.exchange(
                inside_payment_service_url + "/api/v1/inside_pay_service/inside_payment/drawback/" + userId + "/" + money,
                HttpMethod.GET,
                requestEntity,
                Response.class);
        Response result = re.getBody();

        return result.getStatus() == 1;
    }

    public Response<User> getAccount(String orderId, HttpHeaders headers) {
        HttpHeaders newHeaders = getAuthorizationHeadersFrom(headers);
        HttpEntity requestEntity = new HttpEntity(newHeaders);
        String user_service_url = getServiceUrl("ts-user-service");
        ResponseEntity<Response<User>> re = restTemplate.exchange(
                user_service_url + "/api/v1/userservice/users/id/" + orderId,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<Response<User>>() {
                });
        return re.getBody();
    }

    private Response<Order> getOrderByIdFromOrder(String orderId, HttpHeaders headers) {
        HttpHeaders newHeaders = getAuthorizationHeadersFrom(headers);
        HttpEntity requestEntity = new HttpEntity(newHeaders);
        String order_service_url = getServiceUrl("ts-order-service");
        ResponseEntity<Response<Order>> re = restTemplate.exchange(
                order_service_url + "/api/v1/orderservice/order/" + orderId,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<Response<Order>>() {
                });
        return re.getBody();
    }

    private Response<Order> getOrderByIdFromOrderOther(String orderId, HttpHeaders headers) {
        HttpHeaders newHeaders = getAuthorizationHeadersFrom(headers);
        HttpEntity requestEntity = new HttpEntity(newHeaders);
        String order_other_service_url = getServiceUrl("ts-order-other-service");
        ResponseEntity<Response<Order>> re = restTemplate.exchange(
                order_other_service_url + "/api/v1/orderOtherService/orderOther/" + orderId,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<Response<Order>>() {
                });
        return re.getBody();
    }

}
