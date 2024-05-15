package fdse.microservice.controller;

import edu.fudan.common.entity.Travel;


















import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import fdse.microservice.service.BasicService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

/**
 * @author Chenjie
 * @date 2017/6/6.
 */
@RestController
@RequestMapping("/api/v1/basicservice")

public class BasicController { 
    private static final Logger logger = LogManager.getLogger(BasicController.class);






















    @Autowired
    BasicService service;

    @GetMapping(path = "/welcome")
    public String home(@RequestHeader HttpHeaders headers) {
        return "Welcome to [ Basic Service ] !";
    }

    @PostMapping(value = "/basic/travel")
    public HttpEntity queryForTravel(@RequestBody Travel info, @RequestHeader HttpHeaders headers) {
        return ok(service.queryForTravel(info, headers));
    }

    @PostMapping(value = "/basic/travels")
    public HttpEntity queryForTravels(@RequestBody List<Travel> infos, @RequestHeader HttpHeaders headers) {
        return ok(service.queryForTravels(infos, headers));
    }

    @GetMapping(value = "/basic/{stationName}")
    public HttpEntity queryForStationId(@PathVariable String stationName, @RequestHeader HttpHeaders headers) {
        return ok(service.queryForStationId(stationName, headers));
    }

}
