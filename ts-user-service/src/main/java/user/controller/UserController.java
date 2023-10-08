package user.controller;

import edu.fudan.common.util.Response;



















import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import user.dto.UserDto;
import user.service.UserService;

import java.util.UUID;

import static org.springframework.http.ResponseEntity.ok;

/**
 * @author fdse
 */
@RestController
@RequestMapping("/api/v1/userservice/users")
public class UserController { 
    private static final Logger logger = LogManager.getLogger(UserController.class);




















    @Autowired
    private UserService userService;

    @GetMapping("/hello")
    public String testHello() {
        logger.info("[function name:testHello, API:Get /api/v1/userservice/users/hello]");
        return "Hello";
    }

    @GetMapping
    public ResponseEntity<Response> getAllUser(@RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Get /api/v1/userservice/users][headers:{}]","getAllUser",(headers != null ? headers.toString(): null));
        return ok(userService.getAllUsers(headers));
    }

    @GetMapping("/{userName}")
    public ResponseEntity<Response> getUserByUserName(@PathVariable String userName, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Get /api/v1/userservice/users/{userName}][userName:{}, headers:{}]","getUserByUserName",userName, (headers != null ? headers.toString(): null));
        return ok(userService.findByUserName(userName, headers));
    }
    @GetMapping("/id/{userId}")
    public ResponseEntity<Response> getUserByUserId(@PathVariable String userId, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Get /api/v1/userservice/users/id/{userId}][userId:{}, headers:{}]","getUserByUserId",userId, (headers != null ? headers.toString(): null));
        return ok(userService.findByUserId(userId, headers));
    }

    @PostMapping("/register")
    public ResponseEntity<Response> registerUser(@RequestBody UserDto userDto, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Post /api/v1/userservice/users/register][userDto:{}, headers:{}]","registerUser",(userDto != null ? userDto.toString(): null), (headers != null ? headers.toString(): null));
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.saveUser(userDto, headers));
    }


    @DeleteMapping("/{userId}")
    public ResponseEntity<Response> deleteUserById(@PathVariable String userId,
                                                   @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Delete /api/v1/userservice/users/{userId}][userId:{}, headers:{}]","deleteUserById",userId, (headers != null ? headers.toString(): null));
        return ok(userService.deleteUser(userId, headers));
    }

    @PutMapping
    public ResponseEntity<Response> updateUser(@RequestBody UserDto user,
                                               @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API: /api/v1/userservice/users][user:{}, headers:{}]","updateUser",(user != null ? user.toString(): null), (headers != null ? headers.toString(): null));
        return ok(userService.updateUser(user, headers));
    }

}
