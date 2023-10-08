package consign.controller;

import consign.entity.Consign;














import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import consign.service.ConsignService;

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
@RequestMapping("/api/v1/consignservice")
public class ConsignController { 
    private static final Logger logger = LogManager.getLogger(ConsignController.class);
















    @Autowired
    ConsignService service;

    @GetMapping(path = "/welcome")
    public String home(@RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Get /api/v1/consignservice/welcome][headers:{}]","home",(headers != null ? headers.toString(): null));
        return "Welcome to [ Consign Service ] !";
    }

    @PostMapping(value = "/consigns")
    public HttpEntity insertConsign(@RequestBody Consign request,
                                    @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Post /api/v1/consignservice/consigns][request:{}, headers:{}]","insertConsign",(request != null ? request.toString(): null), (headers != null ? headers.toString(): null));
        return ok(service.insertConsignRecord(request, headers));
    }

    @PutMapping(value = "/consigns")
    public HttpEntity updateConsign(@RequestBody Consign request, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Put /api/v1/consignservice/consigns][request:{}, headers:{}]","updateConsign",(request != null ? request.toString(): null), (headers != null ? headers.toString(): null));
        return ok(service.updateConsignRecord(request, headers));
    }

    @GetMapping(value = "/consigns/account/{id}")
    public HttpEntity findByAccountId(@PathVariable String id, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Get /api/v1/consignservice/consigns/account/{id}][id:{}, headers:{}]","findByAccountId",id, (headers != null ? headers.toString(): null));
        UUID newid = UUID.fromString(id);
        return ok(service.queryByAccountId(newid, headers));
    }

    @GetMapping(value = "/consigns/order/{id}")
    public HttpEntity findByOrderId(@PathVariable String id, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Get /api/v1/consignservice/consigns/order/{id}][id:{}, headers:{}]","findByOrderId",id, (headers != null ? headers.toString(): null));
        UUID newid = UUID.fromString(id);
        return ok(service.queryByOrderId(newid, headers));
    }

    @GetMapping(value = "/consigns/{consignee}")
    public HttpEntity findByConsignee(@PathVariable String consignee, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Get /api/v1/consignservice/consigns/{consignee}][consignee:{}, headers:{}]","findByConsignee",consignee, (headers != null ? headers.toString(): null));
        return ok(service.queryByConsignee(consignee, headers));
    }

}
