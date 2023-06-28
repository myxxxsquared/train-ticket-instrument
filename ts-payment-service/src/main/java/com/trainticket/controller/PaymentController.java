package com.trainticket.controller;

import com.trainticket.entity.Payment;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.trainticket.service.PaymentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

/**
 * @author Chenjie
 * @date 2017/4/7
 */
@RestController
@RequestMapping("/api/v1/paymentservice")
public class PaymentController { 
    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);





    @Autowired
    PaymentService service;

    @GetMapping(path = "/welcome")
    public String home() {
        logger.info("[function name:home]");
        return "Welcome to [ Payment Service ] !";
    }

    @PostMapping(path = "/payment")
    public HttpEntity pay(@RequestBody Payment info, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][Payment:{}, HttpHeaders:{}]","pay",(info != null ? info.toString(): null), (headers != null ? headers.toString(): null));
        return ok(service.pay(info, headers));
    }

    @PostMapping(path = "/payment/money")
    public HttpEntity addMoney(@RequestBody Payment info, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][Payment:{}, HttpHeaders:{}]","addMoney",(info != null ? info.toString(): null), (headers != null ? headers.toString(): null));
        return ok(service.addMoney(info, headers));
    }

    @GetMapping(path = "/payment")
    public HttpEntity query(@RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][HttpHeaders:{}]","query",(headers != null ? headers.toString(): null));
        return ok(service.query(headers));
    }
}
