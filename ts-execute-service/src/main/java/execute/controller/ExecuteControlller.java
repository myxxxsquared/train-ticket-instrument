package execute.controller;

import execute.serivce.ExecuteService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

/**
 * @author fdse
 */
@RestController
@RequestMapping("/api/v1/executeservice")
public class ExecuteControlller {

    @Autowired
    private ExecuteService executeService;

    private static final Logger LOGGER = LogManager.getLogger(ExecuteControlller.class);

    @GetMapping(path = "/welcome")
    public String home(@RequestHeader HttpHeaders headers) {
        return "Welcome to [ Execute Service ] !";
    }

    @CrossOrigin(origins = "*")
    @GetMapping(path = "/execute/execute/{orderId}")
    public HttpEntity executeTicket(@PathVariable String orderId, @RequestHeader HttpHeaders headers) {
        // ExecuteControlller.LOGGER.info("[executeTicket][Execute][Id: {}]", orderId);
        LOGGER.info("[function name:{}, API:Get /api/v1/executeservice/execute/execute/{orderId}][orderId:{},headers:{}]","executeTicket",(orderId != null ? orderId.toString(): null),(headers != null ? headers.toString(): null));
        // null
        return ok(executeService.ticketExecute(orderId, headers));
    }

    @CrossOrigin(origins = "*")
    @GetMapping(path = "/execute/collected/{orderId}")
    public HttpEntity collectTicket(@PathVariable String orderId, @RequestHeader HttpHeaders headers) {
        // ExecuteControlller.LOGGER.info("[collectTicket][Collect][Id: {}]", orderId);
        LOGGER.info("[function name:{}, API:Get /api/v1/executeservice/execute/collected/{orderId}][orderId:{},headers:{}]","executeTicket",(orderId != null ? orderId.toString(): null),(headers != null ? headers.toString(): null));
        // null
        return ok(executeService.ticketCollect(orderId, headers));
    }

}
