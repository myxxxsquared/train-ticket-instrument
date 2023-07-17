package plan.controller;


import org.springframework.beans.factory.annotation.Autowired;









import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(RoutePlanController.class);











    @Autowired
    private RoutePlanService routePlanService;

    @GetMapping(path = "/welcome")
    public String home() {
        logger.info("[function name:home]");
        return "Welcome to [ RoutePlan Service ] !";
    }

    @PostMapping(value = "/routePlan/cheapestRoute")
    public HttpEntity getCheapestRoutes(@RequestBody RoutePlanInfo info, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][info:{}, headers:{}]","getCheapestRoutes",(info != null ? info.toString(): null), (headers != null ? headers.toString(): null));
        return ok(routePlanService.searchCheapestResult(info, headers));
    }

    @PostMapping(value = "/routePlan/quickestRoute")
    public HttpEntity getQuickestRoutes(@RequestBody RoutePlanInfo info, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][info:{}, headers:{}]","getQuickestRoutes",(info != null ? info.toString(): null), (headers != null ? headers.toString(): null));
        return ok(routePlanService.searchQuickestResult(info, headers));
    }

    @PostMapping(value = "/routePlan/minStopStations")
    public HttpEntity getMinStopStations(@RequestBody RoutePlanInfo info, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][info:{}, headers:{}]","getMinStopStations",(info != null ? info.toString(): null), (headers != null ? headers.toString(): null));
        return ok(routePlanService.searchMinStopStations(info, headers));
    }

}
