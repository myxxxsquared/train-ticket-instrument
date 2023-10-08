package other.controller;

import edu.fudan.common.entity.Seat;
















import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import edu.fudan.common.util.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;


import other.entity.Order;
import other.entity.QueryInfo;
import other.service.OrderOtherService;

import java.util.Date;

import static org.springframework.http.ResponseEntity.ok;

/**
 * @author fdse
 */
@RestController
@RequestMapping("/api/v1/orderOtherService")
public class OrderOtherController { 
    private static final Logger logger = LogManager.getLogger(OrderOtherController.class);


















    @Autowired
    private OrderOtherService orderService;

    @GetMapping(path = "/welcome")
    public String home() {
        logger.info("[function name:home, API:Get /api/v1/orderOtherService/welcome]");
        return "Welcome to [ Order Other Service ] !";
    }

    /***************************For Normal Use***************************/

    @PostMapping(value = "/orderOther/tickets")
    public HttpEntity getTicketListByDateAndTripId(@RequestBody Seat seatRequest, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Post /api/v1/orderOtherService/orderOther/tickets][seatRequest:{}, headers:{}]","getTicketListByDateAndTripId",(seatRequest != null ? seatRequest.toString(): null), (headers != null ? headers.toString(): null));
        return ok(orderService.getSoldTickets(seatRequest, headers));
    }

    @CrossOrigin(origins = "*")
    @PostMapping(path = "/orderOther")
    public HttpEntity createNewOrder(@RequestBody Order createOrder, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Post /api/v1/orderOtherService/orderOther][createOrder:{}, headers:{}]","createNewOrder",(createOrder != null ? createOrder.toString(): null), (headers != null ? headers.toString(): null));
        return ok(orderService.create(createOrder, headers));
    }

    @CrossOrigin(origins = "*")
    @PostMapping(path = "/orderOther/admin")
    public HttpEntity addcreateNewOrder(@RequestBody Order order, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Post /api/v1/orderOtherService/orderOther/admin][order:{}, headers:{}]","addcreateNewOrder",(order != null ? order.toString(): null), (headers != null ? headers.toString(): null));
        return ok(orderService.addNewOrder(order, headers));
    }

    @CrossOrigin(origins = "*")
    @PostMapping(path = "/orderOther/query")
    public HttpEntity queryOrders(@RequestBody QueryInfo qi,
                                  @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Post /api/v1/orderOtherService/orderOther/query][qi:{}, headers:{}]","queryOrders",(qi != null ? qi.toString(): null), (headers != null ? headers.toString(): null));
        return ok(orderService.queryOrders(qi, qi.getLoginId(), headers));

    }

    @CrossOrigin(origins = "*")
    @PostMapping(path = "/orderOther/refresh")
    public HttpEntity queryOrdersForRefresh(@RequestBody QueryInfo qi,
                                            @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Post /api/v1/orderOtherService/orderOther/refresh][qi:{}, headers:{}]","queryOrdersForRefresh",(qi != null ? qi.toString(): null), (headers != null ? headers.toString(): null));
        return ok(orderService.queryOrdersForRefresh(qi, qi.getLoginId(), headers));
    }


    @CrossOrigin(origins = "*")
    @GetMapping(path = "/orderOther/{travelDate}/{trainNumber}")
    public HttpEntity calculateSoldTicket(@PathVariable String travelDate, @PathVariable String trainNumber,
                                          @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Get /api/v1/orderOtherService/orderOther/{travelDate}/{trainNumber}][travelDate:{}, trainNumber:{}, headers:{}]","calculateSoldTicket",travelDate, trainNumber, (headers != null ? headers.toString(): null));
        return ok(orderService.queryAlreadySoldOrders(StringUtils.String2Date(travelDate), trainNumber, headers));
    }

    @CrossOrigin(origins = "*")
    @GetMapping(path = "/orderOther/price/{orderId}")
    public HttpEntity getOrderPrice(@PathVariable String orderId, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Get /api/v1/orderOtherService/orderOther/price/{orderId}][orderId:{}, headers:{}]","getOrderPrice",orderId, (headers != null ? headers.toString(): null));
        return ok(orderService.getOrderPrice(orderId, headers));
    }

    @CrossOrigin(origins = "*")
    @GetMapping(path = "/orderOther/orderPay/{orderId}")
    public HttpEntity payOrder(@PathVariable String orderId, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Get /api/v1/orderOtherService/orderOther/orderPay/{orderId}][orderId:{}, headers:{}]","payOrder",orderId, (headers != null ? headers.toString(): null));
        return ok(orderService.payOrder(orderId, headers));
    }

    @CrossOrigin(origins = "*")
    @GetMapping(path = "/orderOther/{orderId}")
    public HttpEntity getOrderById(@PathVariable String orderId, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Get /api/v1/orderOtherService/orderOther/{orderId}][orderId:{}, headers:{}]","getOrderById",orderId, (headers != null ? headers.toString(): null));
        return ok(orderService.getOrderById(orderId, headers));
    }

    @CrossOrigin(origins = "*")
    @GetMapping(path = "/orderOther/status/{orderId}/{status}")
    public HttpEntity modifyOrder(@PathVariable String orderId, @PathVariable int status, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Get /api/v1/orderOtherService/orderOther/status/{orderId}/{status}][orderId:{}, status:{}, headers:{}]","modifyOrder",orderId, status, (headers != null ? headers.toString(): null));
        return ok(orderService.modifyOrder(orderId, status, headers));
    }

    @CrossOrigin(origins = "*")
    @GetMapping(path = "/orderOther/security/{checkDate}/{accountId}")
    public HttpEntity securityInfoCheck(@PathVariable String checkDate, @PathVariable String accountId,
                                        @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Get /api/v1/orderOtherService/orderOther/security/{checkDate}/{accountId}][checkDate:{}, accountId:{}, headers:{}]","securityInfoCheck",checkDate, accountId, (headers != null ? headers.toString(): null));
        return ok(orderService.checkSecurityAboutOrder(StringUtils.String2Date(checkDate), accountId, headers));
    }

    @CrossOrigin(origins = "*")
    @PutMapping(path = "/orderOther")
    public HttpEntity saveOrderInfo(@RequestBody Order orderInfo,
                                    @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Put /api/v1/orderOtherService/orderOther][orderInfo:{}, headers:{}]","saveOrderInfo",(orderInfo != null ? orderInfo.toString(): null), (headers != null ? headers.toString(): null));
        return ok(orderService.saveChanges(orderInfo, headers));
    }

    @CrossOrigin(origins = "*")
    @PutMapping(path = "/orderOther/admin")
    public HttpEntity updateOrder(@RequestBody Order order, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Put /api/v1/orderOtherService/orderOther/admin][order:{}, headers:{}]","updateOrder",(order != null ? order.toString(): null), (headers != null ? headers.toString(): null));
        return ok(orderService.updateOrder(order, headers));
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping(path = "/orderOther/{orderId}")
    public HttpEntity deleteOrder(@PathVariable String orderId, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Delete /api/v1/orderOtherService/orderOther/{orderId}][orderId:{}, headers:{}]","deleteOrder",orderId, (headers != null ? headers.toString(): null));
        return ok(orderService.deleteOrder(orderId, headers));
    }

    /***************For super admin(Single Service Test*******************/

    @CrossOrigin(origins = "*")
    @GetMapping(path = "/orderOther")
    public HttpEntity findAllOrder(@RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Get /api/v1/orderOtherService/orderOther][headers:{}]","findAllOrder",(headers != null ? headers.toString(): null));
        return ok(orderService.getAllOrders(headers));
    }

}
