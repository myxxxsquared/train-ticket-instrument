package foodsearch.controller;

import edu.fudan.common.util.JsonUtils;











import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import foodsearch.entity.*;
import foodsearch.mq.RabbitSend;
import foodsearch.service.FoodService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/v1/foodservice")
public class FoodController { 
    private static final Logger logger = LoggerFactory.getLogger(FoodController.class);













    @Autowired
    FoodService foodService;

    @Autowired
    RabbitSend sender;

    @GetMapping(path = "/welcome")
    public String home() {
        logger.info("[function name:home]");
        return "Welcome to [ Food Service ] !";
    }

    @GetMapping(path = "/test_send_delivery")
    public boolean test_send_delivery() {
        logger.info("[function name:test_send_delivery]");
        Delivery delivery = new Delivery();
        delivery.setFoodName("HotPot");
        delivery.setOrderId(UUID.randomUUID());
        delivery.setStationName("Shang Hai");
        delivery.setStoreName("MiaoTing Instant-Boiled Mutton");

        String deliveryJson = JsonUtils.object2Json(delivery);
        sender.send(deliveryJson);
        return true;
    }

    @GetMapping(path = "/orders")
    public HttpEntity findAllFoodOrder(@RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][headers:{}]","findAllFoodOrder",(headers != null ? headers.toString(): null));
        return ok(foodService.findAllFoodOrder(headers));
    }

    @PostMapping(path = "/orders")
    public HttpEntity createFoodOrder(@RequestBody FoodOrder addFoodOrder, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][addFoodOrder:{}, headers:{}]","createFoodOrder",(addFoodOrder != null ? addFoodOrder.toString(): null), (headers != null ? headers.toString(): null));
        return ok(foodService.createFoodOrder(addFoodOrder, headers));
    }

    @PostMapping(path = "/createOrderBatch")
    public HttpEntity createFoodBatches(@RequestBody List<FoodOrder> foodOrderList, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][foodOrderList:{}, headers:{}]","createFoodBatches",(foodOrderList != null ? foodOrderList.toString(): null), (headers != null ? headers.toString(): null));
        return ok(foodService.createFoodOrdersInBatch(foodOrderList, headers));
    }


    @PutMapping(path = "/orders")
    public HttpEntity updateFoodOrder(@RequestBody FoodOrder updateFoodOrder, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][updateFoodOrder:{}, headers:{}]","updateFoodOrder",(updateFoodOrder != null ? updateFoodOrder.toString(): null), (headers != null ? headers.toString(): null));
        return ok(foodService.updateFoodOrder(updateFoodOrder, headers));
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @DeleteMapping(path = "/orders/{orderId}")
    public HttpEntity deleteFoodOrder(@PathVariable String orderId, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][orderId:{}, headers:{}]","deleteFoodOrder",orderId, (headers != null ? headers.toString(): null));
        return ok(foodService.deleteFoodOrder(orderId, headers));
    }

    @GetMapping(path = "/orders/{orderId}")
    public HttpEntity findFoodOrderByOrderId(@PathVariable String orderId, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][orderId:{}, headers:{}]","findFoodOrderByOrderId",orderId, (headers != null ? headers.toString(): null));
        return ok(foodService.findByOrderId(orderId, headers));
    }

    // This relies on a lot of other services, not completely modified
    @GetMapping(path = "/foods/{date}/{startStation}/{endStation}/{tripId}")
    public HttpEntity getAllFood(@PathVariable String date, @PathVariable String startStation,
                                 @PathVariable String endStation, @PathVariable String tripId,
                                 @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][date:{}, startStation:{}, endStation:{}, tripId:{}, headers:{}]","getAllFood",date, startStation, endStation, tripId, (headers != null ? headers.toString(): null));
        return ok(foodService.getAllFood(date, startStation, endStation, tripId, headers));
    }

}
