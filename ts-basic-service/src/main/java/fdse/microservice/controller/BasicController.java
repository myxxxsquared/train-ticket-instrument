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
        logger.info("[function name:{}, API:Get /api/v1/basicservice/welcome][headers:{}]","home",(headers != null ? headers.toString(): null));
        return "Welcome to [ Basic Service ] !";
    }

    @PostMapping(value = "/basic/travel")
    public HttpEntity queryForTravel(@RequestBody Travel info, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Post /api/v1/basicservice/basic/travel][info:{}, headers:{}]","queryForTravel",(info != null ? info.toString(): null), (headers != null ? headers.toString(): null));
        return ok(service.queryForTravel(info, headers));
    }

    @PostMapping(value = "/basic/travels")
    public HttpEntity queryForTravels(@RequestBody List<Travel> infos, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Post /api/v1/basicservice/basic/travels][infos:{}, headers:{}]","queryForTravels",(infos != null ? infos.toString(): null), (headers != null ? headers.toString(): null));
        return ok(service.queryForTravels(infos, headers));
    }

    @GetMapping(value = "/basic/{stationName}")
    public HttpEntity queryForStationId(@PathVariable String stationName, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Get /api/v1/basicservice/basic/{stationName}][stationName:{}, headers:{}]","queryForStationId",stationName, (headers != null ? headers.toString(): null));
        return ok(service.queryForStationId(stationName, headers));
    }

}
