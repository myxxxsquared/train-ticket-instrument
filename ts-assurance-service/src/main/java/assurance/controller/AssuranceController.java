package assurance.controller;

import assurance.service.AssuranceService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private static final Logger logger = LogManager.getLogger(AssuranceController.class);


    
    @Autowired
    private AssuranceService assuranceService;

    @GetMapping(path = "/welcome")
    public String home(@RequestHeader HttpHeaders headers) {
        return "Welcome to [ Assurance Service ] !";
    }

    @CrossOrigin(origins = "*")
    @GetMapping(path = "/assurances")
    public HttpEntity getAllAssurances(@RequestHeader HttpHeaders headers) {
        return ok(assuranceService.getAllAssurances(headers));
    }

    @CrossOrigin(origins = "*")
    @GetMapping(path = "/assurances/types")
    public HttpEntity getAllAssuranceType(@RequestHeader HttpHeaders headers) {
        return ok(assuranceService.getAllAssuranceTypes(headers));
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping(path = "/assurances/assuranceid/{assuranceId}")
    public HttpEntity deleteAssurance(@PathVariable String assuranceId, @RequestHeader HttpHeaders headers) {
        return ok(assuranceService.deleteById(UUID.fromString(assuranceId), headers));
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping(path = "/assurances/orderid/{orderId}")
    public HttpEntity deleteAssuranceByOrderId(@PathVariable String orderId, @RequestHeader HttpHeaders headers) {
        return ok(assuranceService.deleteByOrderId(UUID.fromString(orderId), headers));
    }

    @CrossOrigin(origins = "*")
    @PatchMapping(path = "/assurances/{assuranceId}/{orderId}/{typeIndex}")
    public HttpEntity modifyAssurance(@PathVariable String assuranceId,
                                      @PathVariable String orderId,
                                      @PathVariable int typeIndex, @RequestHeader HttpHeaders headers) {
        return ok(assuranceService.modify(assuranceId, orderId, typeIndex, headers));
    }


    @CrossOrigin(origins = "*")
    @GetMapping(path = "/assurances/{typeIndex}/{orderId}")
    public HttpEntity createNewAssurance(@PathVariable int typeIndex, @PathVariable String orderId, @RequestHeader HttpHeaders headers) {
        return ok(assuranceService.create(typeIndex, orderId, headers));
    }

    @CrossOrigin(origins = "*")
    @GetMapping(path = "/assurances/assuranceid/{assuranceId}")
    public HttpEntity getAssuranceById(@PathVariable String assuranceId, @RequestHeader HttpHeaders headers) {
        return ok(assuranceService.findAssuranceById(UUID.fromString(assuranceId), headers));
    }

    @CrossOrigin(origins = "*")
    @GetMapping(path = "/assurance/orderid/{orderId}")
    public HttpEntity findAssuranceByOrderId(@PathVariable String orderId, @RequestHeader HttpHeaders headers) {
        return ok(assuranceService.findAssuranceByOrderId(UUID.fromString(orderId), headers));
    }

}
