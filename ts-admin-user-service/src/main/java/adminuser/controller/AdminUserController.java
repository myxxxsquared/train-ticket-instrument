package adminuser.controller;

import adminuser.dto.UserDto;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import adminuser.service.AdminUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

/**
 * @author fdse
 */
@RestController
@RequestMapping("/api/v1/adminuserservice/users")
public class AdminUserController { 
    private static final Logger logger = LogManager.getLogger(AdminUserController.class);
 
    @Autowired
    AdminUserService adminUserService;

    @GetMapping(path = "/welcome")
    public String home(@RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Get /api/v1/adminuserservice/users/welcome][headers:{}]","home",(headers != null ? headers.toString(): null));
        return "Welcome to [ AdminUser Service ] !";
    }

    @CrossOrigin(origins = "*")
    @GetMapping
    public HttpEntity getAllUsers(@RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Get /api/v1/adminuserservice/users][headers:{}]","getAllUsers",(headers != null ? headers.toString(): null));
        return ok(adminUserService.getAllUsers(headers));
    }

    @PutMapping
    public HttpEntity updateUser(@RequestBody UserDto userDto, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Put /api/v1/adminuserservice/users][userDto:{}, headers:{}]","updateUser",(userDto != null ? userDto.toString(): null), (headers != null ? headers.toString(): null));
        return ok(adminUserService.updateUser(userDto, headers));
    }


    @PostMapping
    public HttpEntity addUser(@RequestBody UserDto userDto, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Post /api/v1/adminuserservice/users][userDto:{}, headers:{}]","addUser",(userDto != null ? userDto.toString(): null), (headers != null ? headers.toString(): null));
        return ok(adminUserService.addUser(userDto, headers));
    }

    @DeleteMapping(value = "/{userId}")
    public HttpEntity deleteUser(@PathVariable String userId, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Delete /api/v1/adminuserservice/users/{userId}][userId:{}, headers:{}]","deleteUser",userId, (headers != null ? headers.toString(): null));
        return ok(adminUserService.deleteUser(userId, headers));
    }

}
