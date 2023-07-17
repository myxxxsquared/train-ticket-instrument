package fdse.microservice.controller;

import edu.fudan.common.entity.Travel;











import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(BasicController.class);













    @Autowired
    BasicService service;

    @GetMapping(path = "/welcome")
    public String home(@RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][headers:{}]","home",(headers != null ? headers.toString(): null));
        return "Welcome to [ Basic Service ] !";
    }

    @PostMapping(value = "/basic/travel")
    public HttpEntity queryForTravel(@RequestBody Travel info, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][info:{}, headers:{}]","queryForTravel",(info != null ? info.toString(): null), (headers != null ? headers.toString(): null));
        return ok(service.queryForTravel(info, headers));
    }

    @PostMapping(value = "/basic/travels")
    public HttpEntity queryForTravels(@RequestBody List<Travel> infos, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][infos:{}, headers:{}]","queryForTravels",(infos != null ? infos.toString(): null), (headers != null ? headers.toString(): null));
        return ok(service.queryForTravels(infos, headers));
    }

    @GetMapping(value = "/basic/{stationName}")
    public HttpEntity queryForStationId(@PathVariable String stationName, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][stationName:{}, headers:{}]","queryForStationId",stationName, (headers != null ? headers.toString(): null));
        return ok(service.queryForStationId(stationName, headers));
    }

}
