package order.controller;

import edu.fudan.common.entity.Seat;

















import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import edu.fudan.common.util.StringUtils;
import order.entity.*;
import order.service.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

import static org.springframework.http.ResponseEntity.ok;

/**
 * @author fdse
 */
@RestController
@RequestMapping("/api/v1/orderservice")
public class OrderController { 
    private static final Logger logger = LogManager.getLogger(OrderController.class);



















    @Autowired
    private OrderService orderService;

    @GetMapping(path = "/welcome")
    public String home() {
        logger.info("[function name:home, API:Get /api/v1/orderservice/welcome]");
        return "Welcome to [ Order Service ] !";
    }

    /***************************For Normal Use***************************/

    @PostMapping(value = "/order/tickets")
    public HttpEntity getTicketListByDateAndTripId(@RequestBody Seat seatRequest, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Post /api/v1/orderservice/order/tickets][seatRequest:{}, headers:{}]","getTicketListByDateAndTripId",(seatRequest != null ? seatRequest.toString(): null), (headers != null ? headers.toString(): null));
        return ok(orderService.getSoldTickets(seatRequest, headers));
    }

    @CrossOrigin(origins = "*")
    @PostMapping(path = "/order")
    public HttpEntity createNewOrder(@RequestBody Order createOrder, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Post /api/v1/orderservice/order][createOrder:{}, headers:{}]","createNewOrder",(createOrder != null ? createOrder.toString(): null), (headers != null ? headers.toString(): null));
        return ok(orderService.create(createOrder, headers));
    }

    @CrossOrigin(origins = "*")
    @PostMapping(path = "/order/admin")
    public HttpEntity addcreateNewOrder(@RequestBody Order order, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Post /api/v1/orderservice/order/admin][order:{}, headers:{}]","addcreateNewOrder",(order != null ? order.toString(): null), (headers != null ? headers.toString(): null));
        return ok(orderService.addNewOrder(order, headers));
    }

    @CrossOrigin(origins = "*")
    @PostMapping(path = "/order/query")
    public HttpEntity queryOrders(@RequestBody OrderInfo qi,
                                  @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Post /api/v1/orderservice/order/query][qi:{}, headers:{}]","queryOrders",(qi != null ? qi.toString(): null), (headers != null ? headers.toString(): null));
        return ok(orderService.queryOrders(qi, qi.getLoginId(), headers));
    }

    @CrossOrigin(origins = "*")
    @PostMapping(path = "/order/refresh")
    public HttpEntity queryOrdersForRefresh(@RequestBody OrderInfo qi,
                                            @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Post /api/v1/orderservice/order/refresh][qi:{}, headers:{}]","queryOrdersForRefresh",(qi != null ? qi.toString(): null), (headers != null ? headers.toString(): null));
        return ok(orderService.queryOrdersForRefresh(qi, qi.getLoginId(), headers));
    }

    @CrossOrigin(origins = "*")
    @GetMapping(path = "/order/{travelDate}/{trainNumber}")
    public HttpEntity calculateSoldTicket(@PathVariable String travelDate, @PathVariable String trainNumber,
                                          @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Get /api/v1/orderservice/order/{travelDate}/{trainNumber}][travelDate:{}, trainNumber:{}, headers:{}]","calculateSoldTicket",travelDate, trainNumber, (headers != null ? headers.toString(): null));
        return ok(orderService.queryAlreadySoldOrders(StringUtils.String2Date(travelDate), trainNumber, headers));
    }

    @CrossOrigin(origins = "*")
    @GetMapping(path = "/order/price/{orderId}")
    public HttpEntity getOrderPrice(@PathVariable String orderId, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Get /api/v1/orderservice/order/price/{orderId}][orderId:{}, headers:{}]","getOrderPrice",orderId, (headers != null ? headers.toString(): null));
        // String
        return ok(orderService.getOrderPrice(orderId, headers));
    }


    @CrossOrigin(origins = "*")
    @GetMapping(path = "/order/orderPay/{orderId}")
    public HttpEntity payOrder(@PathVariable String orderId, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Get /api/v1/orderservice/order/orderPay/{orderId}][orderId:{}, headers:{}]","payOrder",orderId, (headers != null ? headers.toString(): null));
        // Order
        return ok(orderService.payOrder(orderId, headers));
    }

    @CrossOrigin(origins = "*")
    @GetMapping(path = "/order/{orderId}")
    public HttpEntity getOrderById(@PathVariable String orderId, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Get /api/v1/orderservice/order/{orderId}][orderId:{}, headers:{}]","getOrderById",orderId, (headers != null ? headers.toString(): null));
        // Order
        return ok(orderService.getOrderById(orderId, headers));
    }

    @CrossOrigin(origins = "*")
    @GetMapping(path = "/order/status/{orderId}/{status}")
    public HttpEntity modifyOrder(@PathVariable String orderId, @PathVariable int status, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Get /api/v1/orderservice/order/status/{orderId}/{status}][orderId:{}, status:{}, headers:{}]","modifyOrder",orderId, status, (headers != null ? headers.toString(): null));
        // Order
        return ok(orderService.modifyOrder(orderId, status, headers));
    }


    @CrossOrigin(origins = "*")
    @GetMapping(path = "/order/security/{checkDate}/{accountId}")
    public HttpEntity securityInfoCheck(@PathVariable String checkDate, @PathVariable String accountId,
                                        @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Get /api/v1/orderservice/order/security/{checkDate}/{accountId}][checkDate:{}, accountId:{}, headers:{}]","securityInfoCheck",checkDate, accountId, (headers != null ? headers.toString(): null));
        return ok(orderService.checkSecurityAboutOrder(StringUtils.String2Date(checkDate), accountId, headers));
    }


    @CrossOrigin(origins = "*")
    @PutMapping(path = "/order")
    public HttpEntity saveOrderInfo(@RequestBody Order orderInfo,
                                    @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Put /api/v1/orderservice/order][orderInfo:{}, headers:{}]","saveOrderInfo",(orderInfo != null ? orderInfo.toString(): null), (headers != null ? headers.toString(): null));
        return ok(orderService.saveChanges(orderInfo, headers));
    }

    @CrossOrigin(origins = "*")
    @PutMapping(path = "/order/admin")
    public HttpEntity updateOrder(@RequestBody Order order, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Put /api/v1/orderservice/order/admin][order:{}, headers:{}]","updateOrder",(order != null ? order.toString(): null), (headers != null ? headers.toString(): null));
        return ok(orderService.updateOrder(order, headers));
    }


    @CrossOrigin(origins = "*")
    @DeleteMapping(path = "/order/{orderId}")
    public HttpEntity deleteOrder(@PathVariable String orderId, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Delete /api/v1/orderservice/order/{orderId}][orderId:{}, headers:{}]","deleteOrder",orderId, (headers != null ? headers.toString(): null));
        // Order
        return ok(orderService.deleteOrder(orderId, headers));
    }

    /***************For super admin(Single Service Test*******************/

    @CrossOrigin(origins = "*")
    @GetMapping(path = "/order")
    public HttpEntity findAllOrder(@RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Get /api/v1/orderservice/order][headers:{}]","findAllOrder",(headers != null ? headers.toString(): null));
        // ArrayList<Order>
        return ok(orderService.getAllOrders(headers));
    }

}
