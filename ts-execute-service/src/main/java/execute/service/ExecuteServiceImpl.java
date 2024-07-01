package execute.service;

import edu.fudan.common.util.Response;



















import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import edu.fudan.common.entity.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @author fdse
 */
@Service
public class ExecuteServiceImpl implements ExecuteService { 
    private static final Logger logger = LogManager.getLogger(ExecuteServiceImpl.class);























    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private DiscoveryClient discoveryClient;

    String orderStatusWrong = "Order Status Wrong";
    private String getServiceUrl(String serviceName) {
        return "http://" + serviceName;
    }


    @Override
    public Response ticketExecute(String orderId, HttpHeaders headers) {

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
   
        //1.Get order information
        headers = null;
        Response<Order> resultFromOrder = getOrderByIdFromOrder(orderId, headers);
        Order order;
        if (resultFromOrder.getStatus() == 1) {
            order =  resultFromOrder.getData();
            //2.Check if the order can come in
            // if (order.getStatus() != OrderStatus.COLLECTED.getCode()) {
            //     logger.error("[ticketExecute][getOrderByIdFromOrder][ticket execute error: {}][orderId: {}]", orderStatusWrong, orderId);
            //     return new Response<>(0, orderStatusWrong, null);
            // }
            //3.Confirm inbound, request change order information

            Response resultExecute = executeOrder(orderId, OrderStatus.USED.getCode(), headers);
            if (resultExecute.getStatus() == 1) {
                return new Response<>(1, "Success.", null);
            } else {
                logger.error("[ticketExecute][executeOrder][executeOrder error: {}][orderId: {}]", resultExecute.getMsg(), orderId);
                return new Response<>(0, resultExecute.getMsg(), null);
            }
        } else {
            resultFromOrder = getOrderByIdFromOrderOther(orderId, headers);
            if (resultFromOrder.getStatus() == 1) {
                order =   resultFromOrder.getData();
                //2.Check if the order can come in
                if (order.getStatus() != OrderStatus.COLLECTED.getCode()) {
                    logger.error("[ticketExecute][getOrderByIdFromOrderOther][ticket execute error: {}][orderId: {}]", orderStatusWrong, orderId);
                    return new Response<>(0, orderStatusWrong, null);
                }
                //3.Confirm inbound, request change order information

                Response resultExecute = executeOrderOther(orderId, OrderStatus.USED.getCode(), headers);
                if (resultExecute.getStatus() == 1) {
                    return new Response<>(1, "Success", null);
                } else {
                    logger.error("[ticketExecute][executeOrderOther][executeOrderOther error: {}][orderId: {}]", resultExecute.getMsg(), orderId);
                    return new Response<>(0, resultExecute.getMsg(), null);
                }
            } else {
                logger.error("[ticketExecute][getOrderByIdFromOrderOther][ticker execute error: {}][orderId: {}]", "Order Not Found", orderId);
                return new Response<>(0, "Order Not Found", null);
            }
        }
    }

    @Override
    public Response ticketCollect(String orderId, HttpHeaders headers) {
        //1.Get order information

        headers = null;
        Response<Order> resultFromOrder = getOrderByIdFromOrder(orderId, headers);
        Order order;
        if (resultFromOrder.getStatus() == 1) {
            order =  resultFromOrder.getData();
            //2.Check if the order can come in
            if (order.getStatus() != OrderStatus.PAID.getCode() && order.getStatus() != OrderStatus.CHANGE.getCode()) {
                logger.error("[ticketCollect][getOrderByIdFromOrder][ticket collect error: {}][orderId: {}]", orderStatusWrong, orderId);
                return new Response<>(0, orderStatusWrong, null);
            }
            //3.Confirm inbound, request change order information

            Response resultExecute = executeOrder(orderId, OrderStatus.COLLECTED.getCode(), headers);
            if (resultExecute.getStatus() == 1) {
                return new Response<>(1, "Success", null);
            } else {
                logger.error("[ticketCollect][executeOrder][ticket collect error: {}][orderId: {}]", resultExecute.getMsg(), orderId);
                return new Response<>(0, resultExecute.getMsg(), null);
            }
        } else {
            resultFromOrder = getOrderByIdFromOrderOther(orderId, headers);
            if (resultFromOrder.getStatus() == 1) {
                order = (Order) resultFromOrder.getData();
                //2.Check if the order can come in
                // if (order.getStatus() != OrderStatus.PAID.getCode() && order.getStatus() != OrderStatus.CHANGE.getCode()) {
                //     logger.error("[ticketCollect][getOrderByIdFromOrderOther][ticket collect error: {}][orderId: {}]", orderStatusWrong, orderId);
                //     return new Response<>(0, orderStatusWrong, null);
                // }
                //3.Confirm inbound, request change order information
                Response resultExecute = executeOrderOther(orderId, OrderStatus.COLLECTED.getCode(), headers);
                if (resultExecute.getStatus() == 1) {
                    return new Response<>(1, "Success.", null);
                } else {
                    logger.error("[ticketCollect][executeOrderOther][ticket collect error: {}][orderId: {}]", resultExecute.getMsg(), orderId);
                    return new Response<>(0, resultExecute.getMsg(), null);
                }
            } else {
                logger.error("[ticketCollect][getOrderByIdFromOrderOther][ticket collect error: {}][orderId: {}]", "Order Not Found", orderId);
                return new Response<>(0, "Order Not Found", null);
            }
        }
    }


    private Response executeOrder(String orderId, int status, HttpHeaders headers) {
        headers = null;
        HttpEntity requestEntity = new HttpEntity(headers);
        String order_service_url=getServiceUrl("ts-order-service");
        ResponseEntity<Response> re = restTemplate.exchange(
                order_service_url + "/api/v1/orderservice/order/status/" + orderId + "/" + status,
                HttpMethod.GET,
                requestEntity,
                Response.class);
        return re.getBody();
    }


    private Response executeOrderOther(String orderId, int status, HttpHeaders headers) {
        headers = null;
        HttpEntity requestEntity = new HttpEntity(headers);
        String order_other_service_url=getServiceUrl("ts-order-other-service");
        ResponseEntity<Response> re = restTemplate.exchange(
                order_other_service_url + "/api/v1/orderOtherService/orderOther/status/" + orderId + "/" + status,
                HttpMethod.GET,
                requestEntity,
                Response.class);
        return re.getBody();
    }

    private Response<Order> getOrderByIdFromOrder(String orderId, HttpHeaders headers) {
        headers = null;
        HttpEntity requestEntity = new HttpEntity(headers);
        String order_service_url=getServiceUrl("ts-order-service");
        ResponseEntity<Response<Order>> re = restTemplate.exchange(
                order_service_url + "/api/v1/orderservice/order/" + orderId,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<Response<Order>>() {
                });
        return re.getBody();
    }

    private Response<Order> getOrderByIdFromOrderOther(String orderId, HttpHeaders headers) {
        headers = null;
        HttpEntity requestEntity = new HttpEntity(headers);
        String order_other_service_url=getServiceUrl("ts-order-other-service");
        ResponseEntity<Response<Order>> re = restTemplate.exchange(
                order_other_service_url + "/api/v1/orderOtherService/orderOther/" + orderId,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<Response<Order>>() {
                });
        return re.getBody();
    }

}
