package route.controller;

import edu.fudan.common.util.Response;





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




    @Autowired
    private RouteService routeService;

    @GetMapping(path = "/welcome")
    public String home() {
        return "Welcome to [ Route Service ] !";
    }

    @PostMapping(path = "/routes")
    public ResponseEntity<Response> createAndModifyRoute(@RequestBody RouteInfo createAndModifyRouteInfo, @RequestHeader HttpHeaders headers) {
        return ok(routeService.createAndModify(createAndModifyRouteInfo, headers));
    }

    @DeleteMapping(path = "/routes/{routeId}")
    public HttpEntity deleteRoute(@PathVariable String routeId, @RequestHeader HttpHeaders headers) {
        return ok(routeService.deleteRoute(routeId, headers));
    }

    @GetMapping(path = "/routes/{routeId}")
    public HttpEntity queryById(@PathVariable String routeId, @RequestHeader HttpHeaders headers) {
        return ok(routeService.getRouteById(routeId, headers));
    }

    @PostMapping(path = "/routes/byIds")
    public HttpEntity queryByIds(@RequestBody List<String> routeIds, @RequestHeader HttpHeaders headers) {
        return ok(routeService.getRouteByIds(routeIds, headers));
    }

    @GetMapping(path = "/routes")
    public HttpEntity queryAll(@RequestHeader HttpHeaders headers) {
        return ok(routeService.getAllRoutes(headers));
    }

    @GetMapping(path = "/routes/{start}/{end}")
    public HttpEntity queryByStartAndTerminal(@PathVariable String start,
                                              @PathVariable String end,
                                              @RequestHeader HttpHeaders headers) {
        return ok(routeService.getRouteByStartAndEnd(start, end, headers));
    }

}