package auth.controller;


import auth.dto.BasicAuthDto;














import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import auth.entity.User;
import auth.exception.UserOperationException;
import auth.service.TokenService;
import auth.service.UserService;
import edu.fudan.common.util.Response;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * @author fdse
 */
@RestController
@RequestMapping("/api/v1/users")
public class UserController { 
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);











    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    @GetMapping("/hello")
    public Object getHello() {
        logger.info("[function name:getHello]");
        return "Hello";
    }

    @PostMapping("/login")
    public ResponseEntity<Response> getToken(@RequestBody BasicAuthDto dao , @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][dao:{}, headers:{}]","getToken",(dao != null ? dao.toString(): null), (headers != null ? headers.toString(): null));
        try {
            Response<?> res = tokenService.getToken(dao, headers);
            return ResponseEntity.ok(res);
        } catch (UserOperationException e) {
            logger.error("[getToken][tokenService.getToken error][UserOperationException, message: {}]", e.getMessage());
            return ResponseEntity.ok(new Response<>(0, "get token error", null));
        }
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUser(@RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][headers:{}]","getAllUser",(headers != null ? headers.toString(): null));
        return ResponseEntity.ok().body(userService.getAllUser(headers));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Response> deleteUserById(@PathVariable String userId, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][userId:{}, headers:{}]","deleteUserById",userId, (headers != null ? headers.toString(): null));
        return ResponseEntity.ok(userService.deleteByUserId(userId, headers));
    }

}
