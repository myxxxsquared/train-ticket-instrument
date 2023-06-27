package food_delivery.controller;


import edu.fudan.common.util.Response;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import food_delivery.entity.DeliveryInfo;
import food_delivery.entity.FoodDeliveryOrder;
import food_delivery.entity.SeatInfo;
import food_delivery.entity.TripOrderInfo;
import food_delivery.service.FoodDeliveryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/v1/fooddeliveryservice")
public class FoodDeliveryController { 
    private static final Logger logger = LoggerFactory.getLogger(FoodDeliveryController.class);




    @Autowired
    private FoodDeliveryService foodDeliveryService;

    @GetMapping(path = "/welcome")
    public String home() {
        logger.info("[function name:home]");
        return "Welcome to [ food delivery service ] !";
    }


    @PostMapping("/orders")
    public HttpEntity createFoodDeliveryOrder(@RequestBody FoodDeliveryOrder fd, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][FoodDeliveryOrder:{}, HttpHeaders:{}]","createFoodDeliveryOrder",(fd != null ? fd.toString(): null), (headers != null ? headers.toString(): null));
        return ok(foodDeliveryService.createFoodDeliveryOrder(fd, headers));
    }

    @DeleteMapping("/orders/d/{orderId}")
    public HttpEntity deleteFoodDeliveryOrder(@PathVariable String orderId, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][String:{}, HttpHeaders:{}]","deleteFoodDeliveryOrder",orderId, (headers != null ? headers.toString(): null));
        return ok(foodDeliveryService.deleteFoodDeliveryOrder(orderId, headers));
    }

    @GetMapping("/orders/{orderId}")
    public HttpEntity getFoodDeliveryOrderById(@PathVariable String orderId, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][String:{}, HttpHeaders:{}]","getFoodDeliveryOrderById",orderId, (headers != null ? headers.toString(): null));
        return ok(foodDeliveryService.getFoodDeliveryOrderById(orderId, headers));
    }

    @GetMapping("/orders/all")
    public HttpEntity getAllFoodDeliveryOrders(@RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][HttpHeaders:{}]","getAllFoodDeliveryOrders",(headers != null ? headers.toString(): null));
        return ok(foodDeliveryService.getAllFoodDeliveryOrders(headers));
    }

    @GetMapping("/orders/store/{storeId}")
    public HttpEntity getFoodDeliveryOrderByStoreId(@PathVariable String storeId, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][String:{}, HttpHeaders:{}]","getFoodDeliveryOrderByStoreId",storeId, (headers != null ? headers.toString(): null));
        return ok(foodDeliveryService.getFoodDeliveryOrderByStoreId(storeId, headers));
    }

    @PutMapping("/orders/tripid")
    public HttpEntity updateTripId(@RequestBody TripOrderInfo tripOrderInfo, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][TripOrderInfo:{}, HttpHeaders:{}]","updateTripId",(tripOrderInfo != null ? tripOrderInfo.toString(): null), (headers != null ? headers.toString(): null));
        return ok(foodDeliveryService.updateTripId(tripOrderInfo, headers));
    }

    @PutMapping("/orders/seatno")
    public HttpEntity updateSeatNo(@RequestBody SeatInfo seatInfo, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][SeatInfo:{}, HttpHeaders:{}]","updateSeatNo",(seatInfo != null ? seatInfo.toString(): null), (headers != null ? headers.toString(): null));
        return ok(foodDeliveryService.updateSeatNo(seatInfo, headers));
    }

    @PutMapping("/orders/dtime")
    public HttpEntity updateDeliveryTime(@RequestBody DeliveryInfo deliveryInfo, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][DeliveryInfo:{}, HttpHeaders:{}]","updateDeliveryTime",(deliveryInfo != null ? deliveryInfo.toString(): null), (headers != null ? headers.toString(): null));
        return ok(foodDeliveryService.updateDeliveryTime(deliveryInfo, headers));
    }
}
