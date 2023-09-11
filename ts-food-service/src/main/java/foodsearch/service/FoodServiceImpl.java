package foodsearch.service;

import edu.fudan.common.entity.Food;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import edu.fudan.common.entity.StationFoodStore;
import edu.fudan.common.entity.TrainFood;
import edu.fudan.common.util.JsonUtils;
import edu.fudan.common.util.Response;
import edu.fudan.common.entity.Route;
import foodsearch.entity.*;
import foodsearch.mq.RabbitSend;
import foodsearch.repository.FoodOrderRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpStatus;
// import org.springframework.http.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FoodServiceImpl implements FoodService { 
    private static final Logger logger = LoggerFactory.getLogger(FoodServiceImpl.class);



















    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private FoodOrderRepository foodOrderRepository;

    @Autowired
    private RabbitSend sender;

    @Autowired
    private DiscoveryClient discoveryClient;

    private String getServiceUrl(String serviceName) {
        logger.info("[function name:{}][serviceName:{}]","getServiceUrl",serviceName);
        return "http://" + serviceName;
    }

    String success = "Success.";
    String orderIdNotExist = "Order Id Is Non-Existent.";

    @Override
    public Response createFoodOrdersInBatch(List<FoodOrder> orders, HttpHeaders headers) {
        logger.info("[function name:{}][orders:{}, headers:{}]","createFoodOrdersInBatch",(orders != null ? orders.toString(): null), (headers != null ? headers.toString(): null));
        boolean error = false;
        String errorOrderId = "";

        // Check if foodOrder exists
        for (FoodOrder addFoodOrder : orders) {
            FoodOrder fo = foodOrderRepository.findByOrderId(addFoodOrder.getOrderId());
            if (fo != null) {
                logger.error("[createFoodOrdersInBatch][AddFoodOrder][Order Id Has Existed][OrderId: {}]", addFoodOrder.getOrderId());
                error = true;
                errorOrderId = addFoodOrder.getOrderId().toString();
                break;
            }
        }
        if (error) {
            return new Response<>(0, "Order Id " + errorOrderId + "Existed", null);
        }

        List<String> deliveryJsons = new ArrayList<>();
        for (FoodOrder addFoodOrder : orders) {
            FoodOrder fo = new FoodOrder();
            fo.setId(UUID.randomUUID().toString());
            fo.setOrderId(addFoodOrder.getOrderId());
            fo.setFoodType(addFoodOrder.getFoodType());
            if (addFoodOrder.getFoodType() == 2) {
                fo.setStationName(addFoodOrder.getStationName());
                fo.setStoreName(addFoodOrder.getStoreName());
            }
            fo.setFoodName(addFoodOrder.getFoodName());
            fo.setPrice(addFoodOrder.getPrice());
      
      logger.info("[fo:{},headers:{}]", (fo != null ? fo : null));
      foodOrderRepository.save(fo);

            Delivery delivery = new Delivery();
            delivery.setFoodName(addFoodOrder.getFoodName());
            delivery.setOrderId(UUID.fromString(addFoodOrder.getOrderId()));
            delivery.setStationName(addFoodOrder.getStationName());
            delivery.setStoreName(addFoodOrder.getStoreName());

            String deliveryJson = JsonUtils.object2Json(delivery);
            deliveryJsons.add(deliveryJson);
        }

        // 批量发送消息
        for(String deliveryJson: deliveryJsons) {
            try {
                sender.send(deliveryJson);
            } catch (Exception e) {
                logger.error("[createFoodOrdersInBatch][AddFoodOrder][send delivery info to mq error][exception: {}]", e.toString());
            }
        }

        return new Response<>(1, success, null);
    }

    @Override
    public Response createFoodOrder(FoodOrder addFoodOrder, HttpHeaders headers) {
        logger.info("[function name:{}][addFoodOrder:{}, headers:{}]","createFoodOrder",(addFoodOrder != null ? addFoodOrder.toString(): null), (headers != null ? headers.toString(): null));

        FoodOrder fo = foodOrderRepository.findByOrderId(addFoodOrder.getOrderId());
        if (fo != null) {
            FoodServiceImpl.logger.error("[createFoodOrder][AddFoodOrder][Order Id Has Existed][OrderId: {}]", addFoodOrder.getOrderId());
            return new Response<>(0, "Order Id Has Existed.", null);
        } else {
            fo = new FoodOrder();
            fo.setId(UUID.randomUUID().toString());
            fo.setOrderId(addFoodOrder.getOrderId());
            fo.setFoodType(addFoodOrder.getFoodType());
            if (addFoodOrder.getFoodType() == 2) {
                fo.setStationName(addFoodOrder.getStationName());
                fo.setStoreName(addFoodOrder.getStoreName());
            }
            fo.setFoodName(addFoodOrder.getFoodName());
            fo.setPrice(addFoodOrder.getPrice());
      
      logger.info("[fo:{},headers:{}]", (fo != null ? fo : null));
      foodOrderRepository.save(fo);

            Delivery delivery = new Delivery();
            delivery.setFoodName(addFoodOrder.getFoodName());
            delivery.setOrderId(UUID.fromString(addFoodOrder.getOrderId()));
            delivery.setStationName(addFoodOrder.getStationName());
            delivery.setStoreName(addFoodOrder.getStoreName());

            String deliveryJson = JsonUtils.object2Json(delivery);
            try {
                sender.send(deliveryJson);
            } catch (Exception e) {
                logger.error("[createFoodOrder][AddFoodOrder][send delivery info to mq error][exception: {}]", e.toString());
            }

            return new Response<>(1, success, fo);
        }
    }

    @Transactional
    @Override
    public Response deleteFoodOrder(String orderId, HttpHeaders headers) {
        logger.info("[function name:{}][orderId:{}, headers:{}]","deleteFoodOrder",orderId, (headers != null ? headers.toString(): null));
        FoodOrder foodOrder = foodOrderRepository.findByOrderId(UUID.fromString(orderId).toString());
        if (foodOrder == null) {
            FoodServiceImpl.logger.error("[deleteFoodOrder][Cancel FoodOrder][Order Id Is Non-Existent][orderId: {}]", orderId);
            return new Response<>(0, orderIdNotExist, null);
        } else {
//            foodOrderRepository.deleteFoodOrderByOrderId(UUID.fromString(orderId));
            foodOrderRepository.deleteFoodOrderByOrderId(orderId);
            return new Response<>(1, success, null);
        }
    }

    @Override
    public Response findAllFoodOrder(HttpHeaders headers) {
        logger.info("[function name:{}][headers:{}]","findAllFoodOrder",(headers != null ? headers.toString(): null));
        List<FoodOrder> foodOrders = foodOrderRepository.findAll();
        logger.info("[foodOrders:{},headers:{}]", (foodOrders != null ? foodOrders : null));
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
        if (foodOrders != null && !foodOrders.isEmpty()) {
            return new Response<>(1, success, foodOrders);
        } else {
            FoodServiceImpl.logger.error("[findAllFoodOrder][Find all food order error: {}]", "No Content");
            return new Response<>(0, "No Content", null);
        }
    }


    @Override
    public Response updateFoodOrder(FoodOrder updateFoodOrder, HttpHeaders headers) {
        logger.info("[function name:{}][updateFoodOrder:{}, headers:{}]","updateFoodOrder",(updateFoodOrder != null ? updateFoodOrder.toString(): null), (headers != null ? headers.toString(): null));
        FoodOrder fo = foodOrderRepository.findById(updateFoodOrder.getId()).orElse(null);
        if (fo == null) {
            return new Response<>(0, orderIdNotExist, null);
        } else {
            fo.setFoodType(updateFoodOrder.getFoodType());
            if (updateFoodOrder.getFoodType() == 1) {
                fo.setStationName(updateFoodOrder.getStationName());
                fo.setStoreName(updateFoodOrder.getStoreName());
            }
            fo.setFoodName(updateFoodOrder.getFoodName());
            fo.setPrice(updateFoodOrder.getPrice());
      
      logger.info("[fo:{},headers:{}]", (fo != null ? fo : null));
      foodOrderRepository.save(fo);
            return new Response<>(1, "Success", fo);
        }
    }

    @Override
    public Response findByOrderId(String orderId, HttpHeaders headers) {
        logger.info("[function name:{}][orderId:{}, headers:{}]","findByOrderId",orderId, (headers != null ? headers.toString(): null));
        FoodOrder fo = foodOrderRepository.findByOrderId(UUID.fromString(orderId).toString());
      logger.info("[fo:{},headers:{}]", (fo != null ? fo : null));
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
        if (fo != null) {
            return new Response<>(1, success, fo);
        } else {
            FoodServiceImpl.logger.warn("[findByOrderId][Find Order by id][Order Id Is Non-Existent][orderId: {}]", orderId);
            return new Response<>(0, orderIdNotExist, null);
        }
    }


    @Override
    public Response getAllFood(String date, String startStation, String endStation, String tripId, HttpHeaders headers) {
        logger.info("[function name:{}][date:{}, startStation:{}, endStation:{}, tripId:{}, headers:{}]","getAllFood",date, startStation, endStation, tripId, (headers != null ? headers.toString(): null));
        AllTripFood allTripFood = new AllTripFood();

        if (null == tripId || tripId.length() <= 2) {
            FoodServiceImpl.logger.error("[getAllFood][Get the Get Food Request Failed][Trip id is not suitable][date: {}, tripId: {}]", date, tripId);
            return new Response<>(0, "Trip id is not suitable", null);
        }

        // need return this tow element
        List<Food> trainFoodList = null;
        Map<String, List<StationFoodStore>> foodStoreListMap = new HashMap<>();

        /**--------------------------------------------------------------------------------------*/
        HttpEntity requestEntityGetTrainFoodListResult = new HttpEntity(null);
        String train_food_service_url = getServiceUrl("ts-train-food-service");
        ResponseEntity<Response<List<Food>>> reGetTrainFoodListResult = restTemplate.exchange(
                train_food_service_url + "/api/v1/trainfoodservice/trainfoods/" + tripId,
                HttpMethod.GET,
                requestEntityGetTrainFoodListResult,
                new ParameterizedTypeReference<Response<List<Food>>>() {
                });
        logger.info("[status code:{}, url:{}, type:{}, headers:{}]",reGetTrainFoodListResult.getStatusCode(),
                train_food_service_url + "/api/v1/trainfoodservice/trainfoods/" + tripId,"GET",headers);



        List<Food> trainFoodListResult = reGetTrainFoodListResult.getBody().getData();

        if (trainFoodListResult != null) {
            trainFoodList = trainFoodListResult;
        } else {
            FoodServiceImpl.logger.error("[getAllFood][reGetTrainFoodListResult][Get the Get Food Request Failed!][date: {}, tripId: {}]", date, tripId);
            // return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response<>(0, "Get the Get Food Request Failed!", null));
            return new Response<>(0, "Get the Get Food Request Failed!", null);
        }
        //车次途经的车站
        /**--------------------------------------------------------------------------------------*/
        HttpEntity requestEntityGetRouteResult = new HttpEntity(null, null);
        String travel_service_url = getServiceUrl("ts-travel-service");
        ResponseEntity<Response<Route>> reGetRouteResult = restTemplate.exchange(
                travel_service_url + "/api/v1/travelservice/routes/" + tripId,
                HttpMethod.GET,
                requestEntityGetRouteResult,
                new ParameterizedTypeReference<Response<Route>>() {
                });
        logger.info("[status code:{}, url:{}, type:{}, headers:{}]",reGetRouteResult.getStatusCode(),
                travel_service_url + "/api/v1/travelservice/routes/" + tripId,"GET",headers);
        Response<Route> stationResult = reGetRouteResult.getBody();

        if (stationResult.getStatus() == 1) {
            Route route = stationResult.getData();
            List<String> stations = route.getStations();
            //去除不经过的站，如果起点终点有的话
            if (null != startStation && !"".equals(startStation)) {
                /**--------------------------------------------------------------------------------------*/
                for (int i = 0; i < stations.size(); i++) {
                    if (stations.get(i).equals(startStation)) {
                        break;
                    } else {
                        stations.remove(i);
                    }
                }
            }
            if (null != endStation && !"".equals(endStation)) {
                /**--------------------------------------------------------------------------------------*/
                for (int i = stations.size() - 1; i >= 0; i--) {
                    if (stations.get(i).equals(endStation)) {
                        break;
                    } else {
                        stations.remove(i);
                    }
                }
            }

            HttpEntity requestEntityFoodStoresListResult = new HttpEntity(stations, null);
            String station_food_service_url = getServiceUrl("ts-station-food-service");
            ResponseEntity<Response<List<StationFoodStore>>> reFoodStoresListResult = restTemplate.exchange(
                     station_food_service_url + "/api/v1/stationfoodservice/stationfoodstores",
                    HttpMethod.POST,
                    requestEntityFoodStoresListResult,
                    new ParameterizedTypeReference<Response<List<StationFoodStore>>>() {
                    });
        logger.info("[status code:{}, url:{}, type:{}, headers:{}]",reFoodStoresListResult.getStatusCode(),
                     station_food_service_url + "/api/v1/stationfoodservice/stationfoodstores","POST",headers);
            List<StationFoodStore> stationFoodStoresListResult = reFoodStoresListResult.getBody().getData();
            if (stationFoodStoresListResult != null && !stationFoodStoresListResult.isEmpty()) {
                for (String station : stations) {
                    List<StationFoodStore> res = stationFoodStoresListResult.stream()
                            .filter(stationFoodStore -> (stationFoodStore.getStationName().equals(station)))
                            .collect(Collectors.toList());
                    foodStoreListMap.put(station, res);
                }
            } else {
                FoodServiceImpl.logger.error("[getAllFood][Get the Get Food Request Failed!][foodStoresListResult is null][date: {}, tripId: {}]", date, tripId);
                return new Response<>(0, "Get All Food Failed", allTripFood);
            }
        } else {
            FoodServiceImpl.logger.error("[getAllFood][Get the Get Food Request Failed!][station status error][date: {}, tripId: {}]", date, tripId);
            return new Response<>(0, "Get All Food Failed", allTripFood);
        }
        allTripFood.setTrainFoodList(trainFoodList);
        allTripFood.setFoodStoreListMap(foodStoreListMap);
        return new Response<>(1, "Get All Food Success", allTripFood);
    }
}
