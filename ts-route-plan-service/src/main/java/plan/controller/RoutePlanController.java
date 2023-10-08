package plan.controller;


import org.springframework.beans.factory.annotation.Autowired;














import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import edu.fudan.common.entity.RoutePlanInfo;
import plan.service.RoutePlanService;

import static org.springframework.http.ResponseEntity.ok;

/**
 * @author fdse
 */
@RestController
@RequestMapping("/api/v1/routeplanservice")
public class RoutePlanController { 
    private static final Logger logger = LogManager.getLogger(RoutePlanController.class);
















    @Autowired
    private RoutePlanService routePlanService;

    @GetMapping(path = "/welcome")
    public String home() {
        logger.info("[function name:home, API:Get /api/v1/routeplanservice/welcome]");
        return "Welcome to [ RoutePlan Service ] !";
    }

    @PostMapping(value = "/routePlan/cheapestRoute")
    public HttpEntity getCheapestRoutes(@RequestBody RoutePlanInfo info, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Post /api/v1/routeplanservice/routePlan/cheapestRoute][info:{}, headers:{}]","getCheapestRoutes",(info != null ? info.toString(): null), (headers != null ? headers.toString(): null));
        return ok(routePlanService.searchCheapestResult(info, headers));
    }

    @PostMapping(value = "/routePlan/quickestRoute")
    public HttpEntity getQuickestRoutes(@RequestBody RoutePlanInfo info, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Post /api/v1/routeplanservice/routePlan/quickestRoute][info:{}, headers:{}]","getQuickestRoutes",(info != null ? info.toString(): null), (headers != null ? headers.toString(): null));
        return ok(routePlanService.searchQuickestResult(info, headers));
    }

    @PostMapping(value = "/routePlan/minStopStations")
    public HttpEntity getMinStopStations(@RequestBody RoutePlanInfo info, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Post /api/v1/routeplanservice/routePlan/minStopStations][info:{}, headers:{}]","getMinStopStations",(info != null ? info.toString(): null), (headers != null ? headers.toString(): null));
        return ok(routePlanService.searchMinStopStations(info, headers));
    }

}
