package rebook.controller;


import org.springframework.beans.factory.annotation.Autowired;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import rebook.entity.RebookInfo;
import rebook.service.RebookService;

import static org.springframework.http.ResponseEntity.ok;

/**
 * @author fdse
 */
@RestController
@RequestMapping("/api/v1/rebookservice")
public class RebookController { 
    private static final Logger logger = LoggerFactory.getLogger(RebookController.class);





    @Autowired
    RebookService service;

    @GetMapping(path = "/welcome")
    public String home() {
        logger.info("[function name:home]");
        return "Welcome to [ Rebook Service ] !";
    }

    @PostMapping(value = "/rebook/difference")
    public HttpEntity payDifference(@RequestBody RebookInfo info,
                                    @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][RebookInfo:{}, HttpHeaders:{}]","payDifference",(info != null ? info.toString(): null), (headers != null ? headers.toString(): null));
        return ok(service.payDifference(info, headers));
    }

    @PostMapping(value = "/rebook")
    public HttpEntity rebook(@RequestBody RebookInfo info, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][RebookInfo:{}, HttpHeaders:{}]","rebook",(info != null ? info.toString(): null), (headers != null ? headers.toString(): null));
        return ok(service.rebook(info, headers));
    }

}
