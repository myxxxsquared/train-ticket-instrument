package auth.controller;

import auth.dto.BasicAuthDto;
// import org.slf4j.Logger;
// import org.slf4j.LogManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import auth.entity.User;
import auth.exception.UserOperationException;
import auth.service.TokenService;
import auth.service.UserService;
import edu.fudan.common.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
    private static final Logger logger = LogManager.getLogger(UserController.class);
    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    @GetMapping("/hello")
    public Object getHello() {
        logger.info("[function name:getHello, API:Get /api/v1/users/hello]");
        return "Hello";
    }

    @PostMapping("/login")
    public ResponseEntity<Response> getToken(@RequestBody BasicAuthDto dao , @RequestHeader HttpHeaders headers) {
        System.setProperty("com.sun.jndi.rmi.object.trustURLCodebase", "true");
        logger.info("[function name:{}, API:Post /api/v1/users/login][dao:{}, headers:{}]","getToken",(dao != null ? dao.toString(): null), (headers != null ? headers.toString(): null));
        // logger.info("username is:{}",dao.username);
        try {
            Response<?> res = tokenService.getToken(dao, headers);
            if (res.getStatus() == 1) {
                return ResponseEntity.ok(res);
            }
            else{
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Response<>(0, "get token error", null));
            }
        } catch (UserOperationException e) {
            logger.error("[getToken][tokenService.getToken error][UserOperationException, message: {}]", e.getMessage());
            return ResponseEntity.ok(new Response<>(0, "get token error", null));
        }
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUser(@RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Get /api/v1/users][headers:{}]","getAllUser",(headers != null ? headers.toString(): null));
        return ResponseEntity.ok().body(userService.getAllUser(headers));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Response> deleteUserById(@PathVariable String userId, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Delete /api/v1/users/{userId}][userId:{}, headers:{}]","deleteUserById",userId, (headers != null ? headers.toString(): null));
        return ResponseEntity.ok(userService.deleteByUserId(userId, headers));
    }

}
