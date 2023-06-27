package adminorder.controller;

import edu.fudan.common.entity.*;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import adminorder.service.AdminOrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

/**
 * @author fdse
 */
@RestController
@RequestMapping("/api/v1/adminorderservice")
public class AdminOrderController { 
    private static final Logger logger = LoggerFactory.getLogger(AdminOrderController.class);




    @Autowired
    AdminOrderService adminOrderService;

    @GetMapping(path = "/welcome")
    public String home(@RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][HttpHeaders:{}]","home",(headers != null ? headers.toString(): null));
        return "Welcome to [Admin Order Service] !";
    }

    @CrossOrigin(origins = "*")
    @GetMapping(path = "/adminorder")
    public HttpEntity getAllOrders(@RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][HttpHeaders:{}]","getAllOrders",(headers != null ? headers.toString(): null));
        return ok(adminOrderService.getAllOrders(headers));
    }

    @PostMapping(value = "/adminorder")
    public HttpEntity addOrder(@RequestBody Order request, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][Order:{}, HttpHeaders:{}]","addOrder",(request != null ? request.toString(): null), (headers != null ? headers.toString(): null));
        return ok(adminOrderService.addOrder(request, headers));
    }

    @PutMapping(value = "/adminorder")
    public HttpEntity updateOrder(@RequestBody Order request, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][Order:{}, HttpHeaders:{}]","updateOrder",(request != null ? request.toString(): null), (headers != null ? headers.toString(): null));
        return ok(adminOrderService.updateOrder(request, headers));
    }

    @DeleteMapping(value = "/adminorder/{orderId}/{trainNumber}")
    public HttpEntity deleteOrder(@PathVariable String orderId, @PathVariable String trainNumber, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][String:{}, String:{}, HttpHeaders:{}]","deleteOrder",orderId, trainNumber, (headers != null ? headers.toString(): null));
        return ok(adminOrderService.deleteOrder(orderId, trainNumber, headers));
    }

}
