package security.controller;


import org.springframework.beans.factory.annotation.Autowired;


















import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import security.entity.*;
import security.service.SecurityService;

import static org.springframework.http.ResponseEntity.ok;

/**
 * @author fdse
 */
@RestController
@RequestMapping("/api/v1/securityservice")
public class SecurityController { 
    private static final Logger logger = LogManager.getLogger(SecurityController.class);




















    @Autowired
    private SecurityService securityService;

    @GetMapping(value = "/welcome")
    public String home(@RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Get /api/v1/securityservice/welcome][headers:{}]","home",(headers != null ? headers.toString(): null));
        return "welcome to [Security Service]";
    }

    @CrossOrigin(origins = "*")
    @GetMapping(path = "/securityConfigs")
    public HttpEntity findAllSecurityConfig(@RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Get /api/v1/securityservice/securityConfigs][headers:{}]","findAllSecurityConfig",(headers != null ? headers.toString(): null));
        return ok(securityService.findAllSecurityConfig(headers));
    }

    @CrossOrigin(origins = "*")
    @PostMapping(path = "/securityConfigs")
    public HttpEntity create(@RequestBody SecurityConfig info, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Post /api/v1/securityservice/securityConfigs][info:{}, headers:{}]","create",(info != null ? info.toString(): null), (headers != null ? headers.toString(): null));
        return ok(securityService.addNewSecurityConfig(info, headers));
    }

    @CrossOrigin(origins = "*")
    @PutMapping(path = "/securityConfigs")
    public HttpEntity update(@RequestBody SecurityConfig info, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Put /api/v1/securityservice/securityConfigs][info:{}, headers:{}]","update",(info != null ? info.toString(): null), (headers != null ? headers.toString(): null));
        return ok(securityService.modifySecurityConfig(info, headers));
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping(path = "/securityConfigs/{id}")
    public HttpEntity delete(@PathVariable String id, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Delete /api/v1/securityservice/securityConfigs/{id}][id:{}, headers:{}]","delete",id, (headers != null ? headers.toString(): null));
        return ok(securityService.deleteSecurityConfig(id, headers));
    }

    @CrossOrigin(origins = "*")
    @GetMapping(path = "/securityConfigs/{accountId}")
    public HttpEntity check(@PathVariable String accountId, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Get /api/v1/securityservice/securityConfigs/{accountId}][accountId:{}, headers:{}]","check",accountId, (headers != null ? headers.toString(): null));
        return ok(securityService.check(accountId, headers));
    }

}
