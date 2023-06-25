package adminorder.service;

import edu.fudan.common.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import edu.fudan.common.util.Response;

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

import java.util.ArrayList;
import java.util.List;

/**
 * @author fdse
 */
@Service
public class AdminOrderServiceImpl implements AdminOrderService { 
    private static final Logger logger = LoggerFactory.getLogger(AdminOrderServiceImpl.class);

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private DiscoveryClient discoveryClient;

    private String getServiceUrl(String serviceName) {
        return "http://" + serviceName;
    }

    @Override
    public Response getAllOrders(HttpHeaders headers) {
        logger.info("[function name:{}][headers:{}]","getAllOrders",headers.toString());
        //Get all of the orders
        ArrayList<Order> orders = new ArrayList<>();
        //From ts-order-service
        HttpEntity requestEntity = new HttpEntity(null);

        String order_service_url = getServiceUrl("ts-order-service");
        ResponseEntity<Response<ArrayList<Order>>> re = restTemplate.exchange(
                order_service_url + "/api/v1/orderservice/order",
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<Response<ArrayList<Order>>>() {
                });
        logger.info("the client API's status code and url are: {} {} {}",re.getStatusCode(),
                order_service_url + "/api/v1/orderservice/order","GET");
        Response<ArrayList<Order>> result = re.getBody();

        if (result.getStatus() == 1) {
            ArrayList<Order> orders1 = result.getData();
            orders.addAll(orders1);
        } else {
            AdminOrderServiceImpl.logger.error("[getAllOrders][receive response][Get Orders From ts-order-service fail!]");
        }
        //From ts-order-other-service
        HttpEntity requestEntity2 = new HttpEntity(null);
        String order_other_service_url = getServiceUrl("ts-order-other-service");
        ResponseEntity<Response<ArrayList<Order>>> re2 = restTemplate.exchange(
                order_other_service_url + "/api/v1/orderOtherService/orderOther",
                HttpMethod.GET,
                requestEntity2,
                new ParameterizedTypeReference<Response<ArrayList<Order>>>() {
                });
        logger.info("the client API's status code and url are: {} {} {}",re2.getStatusCode(),
                order_other_service_url + "/api/v1/orderOtherService/orderOther","GET");
        result = re2.getBody();

        if (result.getStatus() == 1) {
            ArrayList<Order> orders1 = (ArrayList<Order>) result.getData();
            orders.addAll(orders1);
        } else {
            AdminOrderServiceImpl.logger.error("[getAllOrders][receive response][Get Orders From ts-order-other-service fail!]");
        }
        //Return orders
        return new Response<>(1, "Get the orders successfully!", orders);

    }

    @Override
    public Response deleteOrder(String orderId, String trainNumber, HttpHeaders headers) {
        logger.info("[function name:{}][orderId:{}, trainNumber:{}, headers:{}]","deleteOrder",orderId, trainNumber, headers.toString());
        Response deleteOrderResult;
        if (trainNumber.startsWith("G") || trainNumber.startsWith("D")) {
            HttpEntity requestEntity = new HttpEntity(null);
            String order_service_url = getServiceUrl("ts-order-service");
            ResponseEntity<Response> re = restTemplate.exchange(
                     order_service_url + "/api/v1/orderservice/order/" + orderId,
                    HttpMethod.DELETE,
                    requestEntity,
                    Response.class);
        logger.info("the client API's status code and url are: {} {} {}",re.getStatusCode(),
                     order_service_url + "/api/v1/orderservice/order/" + orderId,"DELETE");
            deleteOrderResult = re.getBody();

        } else {
            HttpEntity requestEntity = new HttpEntity(null);
            String order_other_service_url = getServiceUrl("ts-order-other-service");
            ResponseEntity<Response> re = restTemplate.exchange(
                    order_other_service_url + "/api/v1/orderOtherService/orderOther/" + orderId,
                    HttpMethod.DELETE,
                    requestEntity,
                    Response.class);
        logger.info("the client API's status code and url are: {} {} {}",re.getStatusCode(),
                    order_other_service_url + "/api/v1/orderOtherService/orderOther/" + orderId,"DELETE");
            deleteOrderResult = re.getBody();

        }
        return deleteOrderResult;

    }

    @Override
    public Response updateOrder(Order request, HttpHeaders headers) {
        logger.info("[function name:{}][request:{}, headers:{}]","updateOrder",request.toString(), headers.toString());

        Response updateOrderResult;
        if (request.getTrainNumber().startsWith("G") || request.getTrainNumber().startsWith("D")) {
            HttpEntity requestEntity = new HttpEntity(request, headers);
            String order_service_url = getServiceUrl("ts-order-service");
            ResponseEntity<Response> re = restTemplate.exchange(
                    order_service_url + "/api/v1/orderservice/order/admin",
                    HttpMethod.PUT,
                    requestEntity,
                    Response.class);
        logger.info("the client API's status code and url are: {} {} {}",re.getStatusCode(),
                    order_service_url + "/api/v1/orderservice/order/admin","PUT");
            updateOrderResult = re.getBody();

        } else {
            HttpEntity requestEntity = new HttpEntity(request, headers);
            String order_other_service_url = getServiceUrl("ts-order-other-service");
            ResponseEntity<Response> re = restTemplate.exchange(
                    order_other_service_url + "/api/v1/orderOtherService/orderOther/admin",
                    HttpMethod.PUT,
                    requestEntity,
                    Response.class);
        logger.info("the client API's status code and url are: {} {} {}",re.getStatusCode(),
                    order_other_service_url + "/api/v1/orderOtherService/orderOther/admin","PUT");
            updateOrderResult = re.getBody();

        }
        return updateOrderResult;
    }

    @Override
    public Response addOrder(Order request, HttpHeaders headers) {
        logger.info("[function name:{}][request:{}, headers:{}]","addOrder",request.toString(), headers.toString());

        Response addOrderResult;
        if (request.getTrainNumber().startsWith("G") || request.getTrainNumber().startsWith("D")) {
            HttpEntity requestEntity = new HttpEntity(request, headers);
            String order_service_url = getServiceUrl("ts-order-service");
            ResponseEntity<Response> re = restTemplate.exchange(
                    order_service_url + "/api/v1/orderservice/order/admin",
                    HttpMethod.POST,
                    requestEntity,
                    Response.class);
        logger.info("the client API's status code and url are: {} {} {}",re.getStatusCode(),
                    order_service_url + "/api/v1/orderservice/order/admin","POST");
            addOrderResult = re.getBody();

        } else {
            HttpEntity requestEntity = new HttpEntity(request, headers);
            String order_other_service_url = getServiceUrl("ts-order-other-service");
            ResponseEntity<Response> re = restTemplate.exchange(
                     order_other_service_url + "/api/v1/orderOtherService/orderOther/admin",
                    HttpMethod.POST,
                    requestEntity,
                    Response.class);
        logger.info("the client API's status code and url are: {} {} {}",re.getStatusCode(),
                     order_other_service_url + "/api/v1/orderOtherService/orderOther/admin","POST");
            addOrderResult = re.getBody();

        }
        return addOrderResult;

    }


}
