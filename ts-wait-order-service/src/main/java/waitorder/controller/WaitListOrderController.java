package waitorder.controller;



import org.springframework.beans.factory.annotation.Autowired;

















import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private static final Logger logger = LogManager.getLogger(WaitListOrderController.class);





















    @Autowired
    private WaitListOrderService waitListOrderService;

    @GetMapping(path = "/welcome")
    public String home() {
        return "Welcome to [ Wait Order Service ] !";
    }

    @PostMapping(path = "/order")
    public HttpEntity createNewOrder(@RequestBody WaitListOrderVO createOrder, @RequestHeader HttpHeaders headers) {
        return ok(waitListOrderService.create(createOrder, headers));
    }

    @GetMapping(path = "/orders")
    public HttpEntity getAllOrders(@RequestHeader HttpHeaders headers){
        return ok(waitListOrderService.getAllOrders(headers));
    }

    @GetMapping(path = "/waitlistorders")
    public HttpEntity getWaitListOrders(@RequestHeader HttpHeaders headers){
        return ok(waitListOrderService.getAllWaitListOrders(headers));
    }


}
