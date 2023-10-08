package auth.controller;

import auth.dto.AuthDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import auth.service.UserService;
import edu.fudan.common.util.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author fdse
 */
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController { 
    private static final Logger logger = LogManager.getLogger(AuthController.class);
    @Autowired
    private UserService userService;
    /**
     * only while  user register, this method will be called by ts-user-service
     * to create a default role use
     *
     * @return
     */
    @GetMapping("/hello")
    public String getHello() {
        logger.info("[function name:getHello, API:Get /api/v1/auth/hello]");
        return "hello";
    }

    @PostMapping
    public HttpEntity<Response> createDefaultUser(@RequestBody AuthDto authDto) {
        logger.info("[function name:{}, API: /api/v1/auth][authDto:{}]","createDefaultUser",(authDto != null ? authDto.toString(): null));
        userService.createDefaultAuthUser(authDto);
        Response response = new Response(1, "SUCCESS", authDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}

