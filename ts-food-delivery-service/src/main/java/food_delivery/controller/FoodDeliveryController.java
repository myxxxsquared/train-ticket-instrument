package food_delivery.controller;


import edu.fudan.common.util.Response;

















import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private static final Logger logger = LogManager.getLogger(FoodDeliveryController.class);



















    @Autowired
    private FoodDeliveryService foodDeliveryService;

    @GetMapping(path = "/welcome")
    public String home() {
        logger.info("[function name:home, API:Get /api/v1/fooddeliveryservice/welcome]");
        return "Welcome to [ food delivery service ] !";
    }


    @PostMapping("/orders")
    public HttpEntity createFoodDeliveryOrder(@RequestBody FoodDeliveryOrder fd, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Post /api/v1/fooddeliveryservice/orders][fd:{}, headers:{}]","createFoodDeliveryOrder",(fd != null ? fd.toString(): null), (headers != null ? headers.toString(): null));
        return ok(foodDeliveryService.createFoodDeliveryOrder(fd, headers));
    }

    @DeleteMapping("/orders/d/{orderId}")
    public HttpEntity deleteFoodDeliveryOrder(@PathVariable String orderId, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Delete /api/v1/fooddeliveryservice/orders/d/{orderId}][orderId:{}, headers:{}]","deleteFoodDeliveryOrder",orderId, (headers != null ? headers.toString(): null));
        return ok(foodDeliveryService.deleteFoodDeliveryOrder(orderId, headers));
    }

    @GetMapping("/orders/{orderId}")
    public HttpEntity getFoodDeliveryOrderById(@PathVariable String orderId, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Get /api/v1/fooddeliveryservice/orders/{orderId}][orderId:{}, headers:{}]","getFoodDeliveryOrderById",orderId, (headers != null ? headers.toString(): null));
        return ok(foodDeliveryService.getFoodDeliveryOrderById(orderId, headers));
    }

    @GetMapping("/orders/all")
    public HttpEntity getAllFoodDeliveryOrders(@RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Get /api/v1/fooddeliveryservice/orders/all][headers:{}]","getAllFoodDeliveryOrders",(headers != null ? headers.toString(): null));
        return ok(foodDeliveryService.getAllFoodDeliveryOrders(headers));
    }

    @GetMapping("/orders/store/{storeId}")
    public HttpEntity getFoodDeliveryOrderByStoreId(@PathVariable String storeId, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Get /api/v1/fooddeliveryservice/orders/store/{storeId}][storeId:{}, headers:{}]","getFoodDeliveryOrderByStoreId",storeId, (headers != null ? headers.toString(): null));
        return ok(foodDeliveryService.getFoodDeliveryOrderByStoreId(storeId, headers));
    }

    @PutMapping("/orders/tripid")
    public HttpEntity updateTripId(@RequestBody TripOrderInfo tripOrderInfo, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Put /api/v1/fooddeliveryservice/orders/tripid][tripOrderInfo:{}, headers:{}]","updateTripId",(tripOrderInfo != null ? tripOrderInfo.toString(): null), (headers != null ? headers.toString(): null));
        return ok(foodDeliveryService.updateTripId(tripOrderInfo, headers));
    }

    @PutMapping("/orders/seatno")
    public HttpEntity updateSeatNo(@RequestBody SeatInfo seatInfo, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Put /api/v1/fooddeliveryservice/orders/seatno][seatInfo:{}, headers:{}]","updateSeatNo",(seatInfo != null ? seatInfo.toString(): null), (headers != null ? headers.toString(): null));
        return ok(foodDeliveryService.updateSeatNo(seatInfo, headers));
    }

    @PutMapping("/orders/dtime")
    public HttpEntity updateDeliveryTime(@RequestBody DeliveryInfo deliveryInfo, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Put /api/v1/fooddeliveryservice/orders/dtime][deliveryInfo:{}, headers:{}]","updateDeliveryTime",(deliveryInfo != null ? deliveryInfo.toString(): null), (headers != null ? headers.toString(): null));
        return ok(foodDeliveryService.updateDeliveryTime(deliveryInfo, headers));
    }
}
