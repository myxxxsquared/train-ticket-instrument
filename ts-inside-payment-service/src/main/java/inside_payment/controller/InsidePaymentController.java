package inside_payment.controller;

import inside_payment.entity.*;









import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import inside_payment.service.InsidePaymentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

/**
 * @author fdse
 */
@RestController
@RequestMapping("/api/v1/inside_pay_service")
public class InsidePaymentController { 
    private static final Logger logger = LoggerFactory.getLogger(InsidePaymentController.class);











    @Autowired
    public InsidePaymentService service;

    @GetMapping(path = "/welcome")
    public String home() {
        logger.info("[function name:home]");
        return "Welcome to [ InsidePayment Service ] !";
    }

    @PostMapping(value = "/inside_payment")
    public HttpEntity pay(@RequestBody PaymentInfo info, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][info:{}, headers:{}]","pay",(info != null ? info.toString(): null), (headers != null ? headers.toString(): null));
        return ok(service.pay(info, headers));
    }

    @PostMapping(value = "/inside_payment/account")
    public HttpEntity createAccount(@RequestBody AccountInfo info, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][info:{}, headers:{}]","createAccount",(info != null ? info.toString(): null), (headers != null ? headers.toString(): null));
        return ok(service.createAccount(info, headers));
    }

    @GetMapping(value = "/inside_payment/{userId}/{money}")
    public HttpEntity addMoney(@PathVariable String userId, @PathVariable
            String money, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][userId:{}, money:{}, headers:{}]","addMoney",userId, money, (headers != null ? headers.toString(): null));
        return ok(service.addMoney(userId, money, headers));
    }

    @GetMapping(value = "/inside_payment/payment")
    public HttpEntity queryPayment(@RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][headers:{}]","queryPayment",(headers != null ? headers.toString(): null));
        return ok(service.queryPayment(headers));
    }

    @GetMapping(value = "/inside_payment/account")
    public HttpEntity queryAccount(@RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][headers:{}]","queryAccount",(headers != null ? headers.toString(): null));
        return ok(service.queryAccount(headers));
    }

    @GetMapping(value = "/inside_payment/drawback/{userId}/{money}")
    public HttpEntity drawBack(@PathVariable String userId, @PathVariable String money, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][userId:{}, money:{}, headers:{}]","drawBack",userId, money, (headers != null ? headers.toString(): null));
        return ok(service.drawBack(userId, money, headers));
    }

    @PostMapping(value = "/inside_payment/difference")
    public HttpEntity payDifference(@RequestBody PaymentInfo info, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][info:{}, headers:{}]","payDifference",(info != null ? info.toString(): null), (headers != null ? headers.toString(): null));
        return ok(service.payDifference(info, headers));
    }

    @GetMapping(value = "/inside_payment/money")
    public HttpEntity queryAddMoney(@RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][headers:{}]","queryAddMoney",(headers != null ? headers.toString(): null));
        return ok(service.queryAddMoney(headers));
    }

}
