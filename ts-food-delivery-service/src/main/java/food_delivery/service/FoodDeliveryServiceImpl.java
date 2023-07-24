package food_delivery.service;


import edu.fudan.common.util.Response;
















import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import food_delivery.entity.*;
import edu.fudan.common.entity.*;
import food_delivery.repository.FoodDeliveryOrderRepository;

import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FoodDeliveryServiceImpl implements FoodDeliveryService { 
    private static final Logger logger = LoggerFactory.getLogger(FoodDeliveryServiceImpl.class);


















    @Autowired
    FoodDeliveryOrderRepository foodDeliveryOrderRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DiscoveryClient discoveryClient;

    private String getServiceUrl(String serviceName) {
        logger.info("[function name:{}][serviceName:{}]","getServiceUrl",serviceName);
        return "http://" + serviceName;
    }

    @Override
    public Response createFoodDeliveryOrder(FoodDeliveryOrder fd, HttpHeaders headers) {
        logger.info("[function name:{}][fd:{}, headers:{}]","createFoodDeliveryOrder",(fd != null ? fd.toString(): null), (headers != null ? headers.toString(): null));
        String stationFoodStoreId = fd.getStationFoodStoreId();

        String staion_food_service_url = getServiceUrl("ts-station-food-service");
        ResponseEntity<Response<StationFoodStoreInfo>> getStationFoodStore = restTemplate.exchange(
                staion_food_service_url + "/api/v1/stationfoodservice/stationfoodstores/bystoreid/" + stationFoodStoreId,
                HttpMethod.GET,
                new HttpEntity(headers),
                new ParameterizedTypeReference<Response<StationFoodStoreInfo>>() {
                });
        logger.info("[status code:{}, url:{}, type:{}, headers:{}]",getStationFoodStore.getStatusCode(),
                staion_food_service_url + "/api/v1/stationfoodservice/stationfoodstores/bystoreid/" + stationFoodStoreId,"GET",headers);
        Response<StationFoodStoreInfo> result = getStationFoodStore.getBody();
        StationFoodStoreInfo stationFoodStoreInfo = result.getData();
        List<Food> storeFoodList = stationFoodStoreInfo.getFoodList();
        Map<String, Double> foodPrice = storeFoodList.stream()
                                                     .collect(Collectors.toMap(Food::getFoodName, Food::getPrice));
        List<Food> orderFoodList = fd.getFoodList();
        double deliveryFee = 0;
        for (Food food : orderFoodList) {
            Double fee = foodPrice.get(food.getFoodName());
            if (fee == null) {
                logger.error("{}:{} have no such food: {}", stationFoodStoreId, stationFoodStoreInfo.getStoreName(), food.getFoodName());
                return new Response<>(0, "Food not in store", null);
            }
            deliveryFee += fee;
        }
        deliveryFee += stationFoodStoreInfo.getDeliveryFee();
        fd.setDeliveryFee(deliveryFee);
        FoodDeliveryOrder res = foodDeliveryOrderRepository.save(fd);
        return new Response<>(1, "Save success", res);
    }

    @Override
    public Response deleteFoodDeliveryOrder(String id, HttpHeaders headers) {
        logger.info("[function name:{}][id:{}, headers:{}]","deleteFoodDeliveryOrder",id, (headers != null ? headers.toString(): null));
        FoodDeliveryOrder t = foodDeliveryOrderRepository.findById(id).orElse(null);
        if (t == null) {
            logger.error("[deleteFoodDeliveryOrder] No such food delivery order id: {}", id);
            return new Response<>(0, "No such food delivery order id", id);
        } else {
            foodDeliveryOrderRepository.deleteById(id);
            return new Response<>(1, "Delete success", null);
        }
    }

    @Override
    public Response getFoodDeliveryOrderById(String id, HttpHeaders headers) {
        logger.info("[function name:{}][id:{}, headers:{}]","getFoodDeliveryOrderById",id, (headers != null ? headers.toString(): null));
        FoodDeliveryOrder t = foodDeliveryOrderRepository.findById(id).orElse(null);
        if (t == null) {
            logger.error("[deleteFoodDeliveryOrder] No such food delivery order id: {}", id);
            return new Response<>(0, "No such food delivery order id", id);
        } else {
            return new Response<>(1, "Get success", t);
        }
    }

    @Override
    public Response getAllFoodDeliveryOrders(HttpHeaders headers) {
        logger.info("[function name:{}][headers:{}]","getAllFoodDeliveryOrders",(headers != null ? headers.toString(): null));
        List<FoodDeliveryOrder> foodDeliveryOrders = foodDeliveryOrderRepository.findAll();
      logger.info("[foodDeliveryOrders:{},headers:{}]", (foodDeliveryOrders != null ? foodDeliveryOrders : null));
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
        if (foodDeliveryOrders == null) {
            logger.error("[getAllFoodDeliveryOrders] Food delivery orders query error");
            return new Response<>(0, "food delivery orders query error", null);
        } else {
            return new Response<>(1, "Get success", foodDeliveryOrders);
        }
    }

    @Override
    public Response getFoodDeliveryOrderByStoreId(String storeId, HttpHeaders headers) {
        logger.info("[function name:{}][storeId:{}, headers:{}]","getFoodDeliveryOrderByStoreId",storeId, (headers != null ? headers.toString(): null));
        List<FoodDeliveryOrder> foodDeliveryOrders = foodDeliveryOrderRepository.findByStationFoodStoreId(storeId);
      logger.info("[foodDeliveryOrders:{},headers:{}]", (foodDeliveryOrders != null ? foodDeliveryOrders : null));
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
        if (foodDeliveryOrders == null) {
            logger.error("[getAllFoodDeliveryOrders] Food delivery orders query error");
            return new Response<>(0, "food delivery orders query error", storeId);
        } else {
            return new Response<>(1, "Get success", foodDeliveryOrders);
        }
    }

    @Override
    public Response updateTripId(TripOrderInfo tripInfo, HttpHeaders headers) {
        logger.info("[function name:{}][tripInfo:{}, headers:{}]","updateTripId",(tripInfo != null ? tripInfo.toString(): null), (headers != null ? headers.toString(): null));
        String id = tripInfo.getOrderId();
        String tripId = tripInfo.getTripId();
        FoodDeliveryOrder t = foodDeliveryOrderRepository.findById(id).orElse(null);
        if (t == null) {
            logger.error("[updateTripId] No such delivery order id: {}", id);
            return new Response<>(0, "No such delivery order id", id);
        } else {
            t.setTripId(tripId);
            foodDeliveryOrderRepository.save(t);
            return new Response<>(1, "update tripId success", t);
        }
    }

    @Override
    public Response updateSeatNo(SeatInfo seatInfo, HttpHeaders headers) {
        logger.info("[function name:{}][seatInfo:{}, headers:{}]","updateSeatNo",(seatInfo != null ? seatInfo.toString(): null), (headers != null ? headers.toString(): null));
        String id = seatInfo.getOrderId();
        int seatNo = seatInfo.getSeatNo();
        FoodDeliveryOrder t = foodDeliveryOrderRepository.findById(id).orElse(null);
        if (t == null) {
            logger.error("[updateSeatNo] No such delivery order id: {}", id);
            return new Response<>(0, "No such delivery order id", id);
        } else {
            t.setSeatNo(seatNo);
            foodDeliveryOrderRepository.save(t);
            return new Response<>(1, "update seatNo success", t);
        }
    }

    @Override
    public Response updateDeliveryTime(DeliveryInfo deliveryInfo, HttpHeaders headers) {
        logger.info("[function name:{}][deliveryInfo:{}, headers:{}]","updateDeliveryTime",(deliveryInfo != null ? deliveryInfo.toString(): null), (headers != null ? headers.toString(): null));
        String id = deliveryInfo.getOrderId();
        String deliveryTime = deliveryInfo.getDeliveryTime();
        FoodDeliveryOrder t = foodDeliveryOrderRepository.findById(id).orElse(null);
        if (t == null) {
            logger.error("[updateDeliveryTime] No such delivery order id: {}", id);
            return new Response<>(0, "No such delivery order id", id);
        } else {
            t.setDeliveryTime(deliveryTime);
            foodDeliveryOrderRepository.save(t);
            return new Response<>(1, "update deliveryTime success", t);
        }
    }
}
