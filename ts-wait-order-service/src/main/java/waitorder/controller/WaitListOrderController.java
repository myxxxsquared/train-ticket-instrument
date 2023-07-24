package waitorder.controller;



import org.springframework.beans.factory.annotation.Autowired;














import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import waitorder.entity.WaitListOrderVO;
import waitorder.service.WaitListOrderService;


import static org.springframework.http.ResponseEntity.ok;

/**
 * @author fdse
 */
@RestController
@RequestMapping("/api/v1/waitorderservice")
public class WaitListOrderController { 
    private static final Logger logger = LoggerFactory.getLogger(WaitListOrderController.class);
















    @Autowired
    private WaitListOrderService waitListOrderService;

    @GetMapping(path = "/welcome")
    public String home() {
        logger.info("[function name:home, API:None]");
        return "Welcome to [ Wait Order Service ] !";
    }

    @PostMapping(path = "/order")
    public HttpEntity createNewOrder(@RequestBody WaitListOrderVO createOrder, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Post /api/v1/waitorderservice/order][createOrder:{}, headers:{}]","createNewOrder",(createOrder != null ? createOrder.toString(): null), (headers != null ? headers.toString(): null));
        return ok(waitListOrderService.create(createOrder, headers));
    }

    @GetMapping(path = "/orders")
    public HttpEntity getAllOrders(@RequestHeader HttpHeaders headers){
        logger.info("[function name:{}, API:Get /api/v1/waitorderservice/orders][headers:{}]","getAllOrders",(headers != null ? headers.toString(): null));
        return ok(waitListOrderService.getAllOrders(headers));
    }

    @GetMapping(path = "/waitlistorders")
    public HttpEntity getWaitListOrders(@RequestHeader HttpHeaders headers){
        logger.info("[function name:{}, API:Get /api/v1/waitorderservice/waitlistorders][headers:{}]","getWaitListOrders",(headers != null ? headers.toString(): null));
        return ok(waitListOrderService.getAllWaitListOrders(headers));
    }


}
