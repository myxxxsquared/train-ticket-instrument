package assurance.controller;

import assurance.service.AssuranceService;















import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.http.ResponseEntity.ok;

/**
 * @author fdse
 */
@RestController
@RequestMapping("/api/v1/assuranceservice")
public class AssuranceController { 
    private static final Logger logger = LoggerFactory.getLogger(AssuranceController.class);
















    @Autowired
    private AssuranceService assuranceService;

    @GetMapping(path = "/welcome")
    public String home(@RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:None][headers:{}]","home",(headers != null ? headers.toString(): null));
        return "Welcome to [ Assurance Service ] !";
    }

    @CrossOrigin(origins = "*")
    @GetMapping(path = "/assurances")
    public HttpEntity getAllAssurances(@RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Get /api/v1/assuranceservice/assurances][headers:{}]","getAllAssurances",(headers != null ? headers.toString(): null));
        return ok(assuranceService.getAllAssurances(headers));
    }

    @CrossOrigin(origins = "*")
    @GetMapping(path = "/assurances/types")
    public HttpEntity getAllAssuranceType(@RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Get /api/v1/assuranceservice/assurances/types][headers:{}]","getAllAssuranceType",(headers != null ? headers.toString(): null));
        return ok(assuranceService.getAllAssuranceTypes(headers));
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping(path = "/assurances/assuranceid/{assuranceId}")
    public HttpEntity deleteAssurance(@PathVariable String assuranceId, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Delete /api/v1/assuranceservice/assurances/assuranceid/{assuranceId}][assuranceId:{}, headers:{}]","deleteAssurance",assuranceId, (headers != null ? headers.toString(): null));
        return ok(assuranceService.deleteById(UUID.fromString(assuranceId), headers));
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping(path = "/assurances/orderid/{orderId}")
    public HttpEntity deleteAssuranceByOrderId(@PathVariable String orderId, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Delete /api/v1/assuranceservice/assurances/orderid/{orderId}][orderId:{}, headers:{}]","deleteAssuranceByOrderId",orderId, (headers != null ? headers.toString(): null));
        return ok(assuranceService.deleteByOrderId(UUID.fromString(orderId), headers));
    }

    @CrossOrigin(origins = "*")
    @PatchMapping(path = "/assurances/{assuranceId}/{orderId}/{typeIndex}")
    public HttpEntity modifyAssurance(@PathVariable String assuranceId,
                                      @PathVariable String orderId,
                                      @PathVariable int typeIndex, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:None][assuranceId:{}, orderId:{}, typeIndex:{}, headers:{}]","modifyAssurance",assuranceId, orderId, typeIndex, (headers != null ? headers.toString(): null));
        return ok(assuranceService.modify(assuranceId, orderId, typeIndex, headers));
    }


    @CrossOrigin(origins = "*")
    @GetMapping(path = "/assurances/{typeIndex}/{orderId}")
    public HttpEntity createNewAssurance(@PathVariable int typeIndex, @PathVariable String orderId, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Get /api/v1/assuranceservice/assurances/{typeIndex}/{orderId}][typeIndex:{}, orderId:{}, headers:{}]","createNewAssurance",typeIndex, orderId, (headers != null ? headers.toString(): null));
        return ok(assuranceService.create(typeIndex, orderId, headers));
    }

    @CrossOrigin(origins = "*")
    @GetMapping(path = "/assurances/assuranceid/{assuranceId}")
    public HttpEntity getAssuranceById(@PathVariable String assuranceId, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Get /api/v1/assuranceservice/assurances/assuranceid/{assuranceId}][assuranceId:{}, headers:{}]","getAssuranceById",assuranceId, (headers != null ? headers.toString(): null));
        return ok(assuranceService.findAssuranceById(UUID.fromString(assuranceId), headers));
    }

    @CrossOrigin(origins = "*")
    @GetMapping(path = "/assurance/orderid/{orderId}")
    public HttpEntity findAssuranceByOrderId(@PathVariable String orderId, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Get /api/v1/assuranceservice/assurance/orderid/{orderId}][orderId:{}, headers:{}]","findAssuranceByOrderId",orderId, (headers != null ? headers.toString(): null));
        return ok(assuranceService.findAssuranceByOrderId(UUID.fromString(orderId), headers));
    }

}
