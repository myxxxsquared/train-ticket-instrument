package route.controller;

import edu.fudan.common.util.Response;






import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import route.entity.RouteInfo;
import route.service.RouteService;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

/**
 * @author fdse
 */
@RestController
@RequestMapping("/api/v1/routeservice")
public class RouteController { 
    private static final Logger logger = LoggerFactory.getLogger(RouteController.class);






    @Autowired
    private RouteService routeService;

    @GetMapping(path = "/welcome")
    public String home() {
        logger.info("[function name:home]");
        return "Welcome to [ Route Service ] !";
    }

    @PostMapping(path = "/routes")
    public ResponseEntity<Response> createAndModifyRoute(@RequestBody RouteInfo createAndModifyRouteInfo, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][RouteInfo:{}, HttpHeaders:{}]","createAndModifyRoute",(createAndModifyRouteInfo != null ? createAndModifyRouteInfo.toString(): null), (headers != null ? headers.toString(): null));
        return ok(routeService.createAndModify(createAndModifyRouteInfo, headers));
    }

    @DeleteMapping(path = "/routes/{routeId}")
    public HttpEntity deleteRoute(@PathVariable String routeId, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][String:{}, HttpHeaders:{}]","deleteRoute",routeId, (headers != null ? headers.toString(): null));
        return ok(routeService.deleteRoute(routeId, headers));
    }

    @GetMapping(path = "/routes/{routeId}")
    public HttpEntity queryById(@PathVariable String routeId, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][String:{}, HttpHeaders:{}]","queryById",routeId, (headers != null ? headers.toString(): null));
        return ok(routeService.getRouteById(routeId, headers));
    }

    @PostMapping(path = "/routes/byIds")
    public HttpEntity queryByIds(@RequestBody List<String> routeIds, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][List<String>:{}, HttpHeaders:{}]","queryByIds",(routeIds != null ? routeIds.toString(): null), (headers != null ? headers.toString(): null));
        return ok(routeService.getRouteByIds(routeIds, headers));
    }

    @GetMapping(path = "/routes")
    public HttpEntity queryAll(@RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][HttpHeaders:{}]","queryAll",(headers != null ? headers.toString(): null));
        return ok(routeService.getAllRoutes(headers));
    }

    @GetMapping(path = "/routes/{start}/{end}")
    public HttpEntity queryByStartAndTerminal(@PathVariable String start,
                                              @PathVariable String end,
                                              @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][String:{}, String:{}, HttpHeaders:{}]","queryByStartAndTerminal",start, end, (headers != null ? headers.toString(): null));
        return ok(routeService.getRouteByStartAndEnd(start, end, headers));
    }

}