package consign.controller;

import consign.entity.Consign;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(ConsignController.class);




    @Autowired
    ConsignService service;

    @GetMapping(path = "/welcome")
    public String home(@RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][HttpHeaders:{}]","home",(headers != null ? headers.toString(): null));
        return "Welcome to [ Consign Service ] !";
    }

    @PostMapping(value = "/consigns")
    public HttpEntity insertConsign(@RequestBody Consign request,
                                    @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][Consign:{}, HttpHeaders:{}]","insertConsign",(request != null ? request.toString(): null), (headers != null ? headers.toString(): null));
        return ok(service.insertConsignRecord(request, headers));
    }

    @PutMapping(value = "/consigns")
    public HttpEntity updateConsign(@RequestBody Consign request, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][Consign:{}, HttpHeaders:{}]","updateConsign",(request != null ? request.toString(): null), (headers != null ? headers.toString(): null));
        return ok(service.updateConsignRecord(request, headers));
    }

    @GetMapping(value = "/consigns/account/{id}")
    public HttpEntity findByAccountId(@PathVariable String id, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][String:{}, HttpHeaders:{}]","findByAccountId",id, (headers != null ? headers.toString(): null));
        UUID newid = UUID.fromString(id);
        return ok(service.queryByAccountId(newid, headers));
    }

    @GetMapping(value = "/consigns/order/{id}")
    public HttpEntity findByOrderId(@PathVariable String id, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][String:{}, HttpHeaders:{}]","findByOrderId",id, (headers != null ? headers.toString(): null));
        UUID newid = UUID.fromString(id);
        return ok(service.queryByOrderId(newid, headers));
    }

    @GetMapping(value = "/consigns/{consignee}")
    public HttpEntity findByConsignee(@PathVariable String consignee, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][String:{}, HttpHeaders:{}]","findByConsignee",consignee, (headers != null ? headers.toString(): null));
        return ok(service.queryByConsignee(consignee, headers));
    }

}
