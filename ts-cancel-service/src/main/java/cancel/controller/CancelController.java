package cancel.controller;

import cancel.service.CancelService;









import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import edu.fudan.common.util.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

/**
 * @author fdse
 */
@RestController
@RequestMapping("/api/v1/cancelservice")
public class CancelController { 
    private static final Logger logger = LoggerFactory.getLogger(CancelController.class);











    @Autowired
    CancelService cancelService;

    @GetMapping(path = "/welcome")
    public String home(@RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][headers:{}]","home",(headers != null ? headers.toString(): null));
        return "Welcome to [ Cancel Service ] !";
    }

    @CrossOrigin(origins = "*")
    @GetMapping(path = "/cancel/refound/{orderId}")
    public HttpEntity calculate(@PathVariable String orderId, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][orderId:{}, headers:{}]","calculate",orderId, (headers != null ? headers.toString(): null));
        return ok(cancelService.calculateRefund(orderId, headers));
    }

    @CrossOrigin(origins = "*")
    @GetMapping(path = "/cancel/{orderId}/{loginId}")
    public HttpEntity cancelTicket(@PathVariable String orderId, @PathVariable String loginId,
                                   @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][orderId:{}, loginId:{}, headers:{}]","cancelTicket",orderId, loginId, (headers != null ? headers.toString(): null));
        try {
            return ok(cancelService.cancelOrder(orderId, loginId, headers));
        } catch (Exception e) {
            CancelController.logger.error(e.getMessage());
            return ok(new Response<>(1, "error", null));
        }
    }

}
