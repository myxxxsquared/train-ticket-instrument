package cancel.service;

import edu.fudan.common.entity.NotifyInfo;











import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author fdse
 */
@Service
public class CancelServiceImpl implements CancelService { 
    private static final Logger logger = LoggerFactory.getLogger(CancelServiceImpl.class);













    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private DiscoveryClient discoveryClient;

    String orderStatusCancelNotPermitted = "Order Status Cancel Not Permitted";

    private String getServiceUrl(String serviceName) {
        logger.info("[function name:{}][serviceName:{}]","getServiceUrl",serviceName);
        return "http://" + serviceName;
    }

    @Override
    public Response cancelOrder(String orderId, String loginId, HttpHeaders headers) {
        logger.info("[function name:{}][orderId:{}, loginId:{}, headers:{}]","cancelOrder",orderId, loginId, (headers != null ? headers.toString(): null));

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



                        Response<User> result = getAccount(order.getAccountId().toString(), headers);
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
                        // sendEmail(notifyInfo, headers);

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

//                    order.setStatus(OrderStatus.CANCEL.getCode());
                    Response changeOrderResult = cancelFromOtherOrder(order, headers);

                    if (changeOrderResult.getStatus() == 1) {
                        //Draw back money
                        String money = calculateRefund(order);
                        boolean status = drawbackMoney(money, loginId, headers);
                        if (status) {
                        } else {
                            CancelServiceImpl.logger.error("[cancelOrder][Draw Back Money Failed][loginId: {}, orderId: {}]", loginId, orderId);
                        }
                        return new Response<>(1, "Success.", null);
                    } else {
                        CancelServiceImpl.logger.error("[cancelOrder][Cancel Order Failed][orderId: {}, Reason: {}]", orderId, changeOrderResult.getMsg());
                        return new Response<>(0, "Fail.Reason:" + changeOrderResult.getMsg(), null);
                    }
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
        logger.info("[function name:{}][notifyInfo:{}, headers:{}]","sendEmail",(notifyInfo != null ? notifyInfo.toString(): null), (headers != null ? headers.toString(): null));
        HttpHeaders newHeaders = getAuthorizationHeadersFrom(headers);
        HttpEntity requestEntity = new HttpEntity(notifyInfo, newHeaders);
        String notification_service_url = getServiceUrl("ts-notification-service");
        ResponseEntity<Boolean> re = restTemplate.exchange(
                notification_service_url + "/api/v1/notifyservice/notification/order_cancel_success",
                HttpMethod.POST,
                requestEntity,
                Boolean.class);
        logger.info("[status code:{}, url:{} and type:{}]",re.getStatusCode(),
                notification_service_url + "/api/v1/notifyservice/notification/order_cancel_success","POST");
        return re.getBody();
    }

    @Override
    public Response calculateRefund(String orderId, HttpHeaders headers) {
        logger.info("[function name:{}][orderId:{}, headers:{}]","calculateRefund",orderId, (headers != null ? headers.toString(): null));

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
        logger.info("[function name:{}][order:{}, headers:{}]","cancelFromOrder",(order != null ? order.toString(): null), (headers != null ? headers.toString(): null));
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
        logger.info("[status code:{}, url:{} and type:{}]",re.getStatusCode(),
                order_service_url + "/api/v1/orderservice/order","PUT");

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
        logger.info("[status code:{}, url:{} and type:{}]",re.getStatusCode(),
                order_other_service_url + "/api/v1/orderOtherService/orderOther","PUT");

        return re.getBody();
    }

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
        logger.info("[status code:{}, url:{} and type:{}]",re.getStatusCode(),
                inside_payment_service_url + "/api/v1/inside_pay_service/inside_payment/drawback/" + userId + "/" + money,"GET");
        Response result = re.getBody();

        return result.getStatus() == 1;
    }

    public Response<User> getAccount(String orderId, HttpHeaders headers) {
        logger.info("[function name:{}][orderId:{}, headers:{}]","getAccount",orderId, (headers != null ? headers.toString(): null));
        HttpHeaders newHeaders = getAuthorizationHeadersFrom(headers);
        HttpEntity requestEntity = new HttpEntity(newHeaders);
        String user_service_url = getServiceUrl("ts-user-service");
        ResponseEntity<Response<User>> re = restTemplate.exchange(
                user_service_url + "/api/v1/userservice/users/id/" + orderId,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<Response<User>>() {
                });
        logger.info("[status code:{}, url:{} and type:{}]",re.getStatusCode(),
                user_service_url + "/api/v1/userservice/users/id/" + orderId,"GET");
        return re.getBody();
    }

    private Response<Order> getOrderByIdFromOrder(String orderId, HttpHeaders headers) {
        logger.info("[function name:{}][orderId:{}, headers:{}]","getOrderByIdFromOrder",orderId, (headers != null ? headers.toString(): null));
        HttpHeaders newHeaders = getAuthorizationHeadersFrom(headers);
        HttpEntity requestEntity = new HttpEntity(newHeaders);
        String order_service_url = getServiceUrl("ts-order-service");
        ResponseEntity<Response<Order>> re = restTemplate.exchange(
                order_service_url + "/api/v1/orderservice/order/" + orderId,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<Response<Order>>() {
                });
        logger.info("[status code:{}, url:{} and type:{}]",re.getStatusCode(),
                order_service_url + "/api/v1/orderservice/order/" + orderId,"GET");
        return re.getBody();
    }

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
        logger.info("[status code:{}, url:{} and type:{}]",re.getStatusCode(),
                order_other_service_url + "/api/v1/orderOtherService/orderOther/" + orderId,"GET");
        return re.getBody();
    }

}
