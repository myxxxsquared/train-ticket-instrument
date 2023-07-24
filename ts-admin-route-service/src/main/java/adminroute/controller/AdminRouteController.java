package adminroute.controller;

import edu.fudan.common.entity.RouteInfo;














import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import adminroute.service.AdminRouteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

/**
 * @author fdse
 */
@RestController
@RequestMapping("/api/v1/adminrouteservice")
public class AdminRouteController { 
    private static final Logger logger = LoggerFactory.getLogger(AdminRouteController.class);
















    @Autowired
    AdminRouteService adminRouteService;

    @GetMapping(path = "/welcome")
    public String home(@RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:None][headers:{}]","home",(headers != null ? headers.toString(): null));
        return "Welcome to [ AdminRoute Service ] !";
    }

    @CrossOrigin(origins = "*")
    @GetMapping(path = "/adminroute")
    public HttpEntity getAllRoutes(@RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Get /api/v1/adminrouteservice/adminroute][headers:{}]","getAllRoutes",(headers != null ? headers.toString(): null));
        return ok(adminRouteService.getAllRoutes(headers));
    }

    @PostMapping(value = "/adminroute")
    public HttpEntity addRoute(@RequestBody RouteInfo request, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Post /api/v1/adminrouteservice/adminroute][request:{}, headers:{}]","addRoute",(request != null ? request.toString(): null), (headers != null ? headers.toString(): null));
        return ok(adminRouteService.createAndModifyRoute(request, headers));
    }

    @DeleteMapping(value = "/adminroute/{routeId}")
    public HttpEntity deleteRoute(@PathVariable String routeId, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Delete /api/v1/adminrouteservice/adminroute/{routeId}][routeId:{}, headers:{}]","deleteRoute",routeId, (headers != null ? headers.toString(): null));
        return ok(adminRouteService.deleteRoute(routeId, headers));
    }


}
