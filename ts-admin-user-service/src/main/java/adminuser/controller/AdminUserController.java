package adminuser.controller;

import adminuser.dto.UserDto;











import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(AdminUserController.class);













    @Autowired
    AdminUserService adminUserService;

    @GetMapping(path = "/welcome")
    public String home(@RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][headers:{}]","home",(headers != null ? headers.toString(): null));
        return "Welcome to [ AdminUser Service ] !";
    }

    @CrossOrigin(origins = "*")
    @GetMapping
    public HttpEntity getAllUsers(@RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][headers:{}]","getAllUsers",(headers != null ? headers.toString(): null));
        return ok(adminUserService.getAllUsers(headers));
    }

    @PutMapping
    public HttpEntity updateUser(@RequestBody UserDto userDto, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][userDto:{}, headers:{}]","updateUser",(userDto != null ? userDto.toString(): null), (headers != null ? headers.toString(): null));
        return ok(adminUserService.updateUser(userDto, headers));
    }


    @PostMapping
    public HttpEntity addUser(@RequestBody UserDto userDto, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][userDto:{}, headers:{}]","addUser",(userDto != null ? userDto.toString(): null), (headers != null ? headers.toString(): null));
        return ok(adminUserService.addUser(userDto, headers));
    }

    @DeleteMapping(value = "/{userId}")
    public HttpEntity deleteUser(@PathVariable String userId, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][userId:{}, headers:{}]","deleteUser",userId, (headers != null ? headers.toString(): null));
        return ok(adminUserService.deleteUser(userId, headers));
    }

}
