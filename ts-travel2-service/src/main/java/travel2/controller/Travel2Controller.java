package travel2.controller;

import edu.fudan.common.entity.TripResponse;













import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import travel2.service.TravelService;

import java.util.ArrayList;

import static org.springframework.http.ResponseEntity.ok;

/**
 * @author fdse
 */
@RestController
@RequestMapping("/api/v1/travel2service")
public class Travel2Controller { 
    private static final Logger logger = LoggerFactory.getLogger(Travel2Controller.class);














    @Autowired
    private TravelService service;

    @GetMapping(path = "/welcome")
    public String home(@RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:None][headers:{}]","home",(headers != null ? headers.toString(): null));
        return "Welcome to [ Travle2 Service ] !";
    }

    @GetMapping(value = "/train_types/{tripId}")
    public HttpEntity getTrainTypeByTripId(@PathVariable String tripId,
                                           @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Get /api/v1/travel2service/train_types/{tripId}][tripId:{}, headers:{}]","getTrainTypeByTripId",tripId, (headers != null ? headers.toString(): null));
        return ok(service.getTrainTypeByTripId(tripId, headers));
    }

    @GetMapping(value = "/routes/{tripId}")
    public HttpEntity getRouteByTripId(@PathVariable String tripId,
                                       @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Get /api/v1/travel2service/routes/{tripId}][tripId:{}, headers:{}]","getRouteByTripId",tripId, (headers != null ? headers.toString(): null));
        //Route
        return ok(service.getRouteByTripId(tripId, headers));
    }

    @PostMapping(value = "/trips/routes")
    public HttpEntity getTripsByRouteId(@RequestBody ArrayList<String> routeIds,
                                        @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Post /api/v1/travel2service/trips/routes][routeIds:{}, headers:{}]","getTripsByRouteId",(routeIds != null ? routeIds.toString(): null), (headers != null ? headers.toString(): null));
        return ok(service.getTripByRoute(routeIds, headers));
    }

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/trips")
    public HttpEntity<?> createTrip(@RequestBody edu.fudan.common.entity.TravelInfo routeIds, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:None][routeIds:{}, headers:{}]","createTrip",(routeIds != null ? routeIds.toString(): null), (headers != null ? headers.toString(): null));
        return new ResponseEntity<>(service.create(routeIds, headers), HttpStatus.CREATED);
    }

    /**
     *Return Trip only, no left ticket information
     *
     * @param tripId trip id
     * @param headers headers
     * @return HttpEntity
     */
    @CrossOrigin(origins = "*")
    @GetMapping(value = "/trips/{tripId}")
    public HttpEntity retrieve(@PathVariable String tripId, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Get /api/v1/travel2service/trips/{tripId}][tripId:{}, headers:{}]","retrieve",tripId, (headers != null ? headers.toString(): null));
        return ok(service.retrieve(tripId, headers));
    }

    @CrossOrigin(origins = "*")
    @PutMapping(value = "/trips")
    public HttpEntity updateTrip(@RequestBody edu.fudan.common.entity.TravelInfo info, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Put /api/v1/travel2service/trips][info:{}, headers:{}]","updateTrip",(info != null ? info.toString(): null), (headers != null ? headers.toString(): null));
        return ok(service.update(info, headers));
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping(value = "/trips/{tripId}")
    public HttpEntity deleteTrip(@PathVariable String tripId, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Delete /api/v1/travel2service/trips/{tripId}][tripId:{}, headers:{}]","deleteTrip",tripId, (headers != null ? headers.toString(): null));
        return ok(service.delete(tripId, headers));
    }

    /**
     * Return Trip and the remaining tickets
     *
     * @param info trip info
     * @param headers headers
     * @return HttpEntity
     */
    @CrossOrigin(origins = "*")
    @PostMapping(value = "/trips/left")
    public HttpEntity queryInfo(@RequestBody edu.fudan.common.entity.TripInfo info, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Post /api/v1/travel2service/trips/left][info:{}, headers:{}]","queryInfo",(info != null ? info.toString(): null), (headers != null ? headers.toString(): null));
        if (info.getStartPlace() == null || info.getStartPlace().length() == 0 ||
                info.getEndPlace() == null || info.getEndPlace().length() == 0 ||
                info.getDepartureTime() == null) {
            ArrayList<TripResponse> errorList = new ArrayList<>();
            return ok(errorList);
        }
        return ok(service.queryByBatch(info, headers));
    }

    /**
     * Return a Trip and the remaining tickets
     *
     * @param gtdi trip all datail info
     * @param headers headers
     * @return HttpEntity
     */
    @CrossOrigin(origins = "*")
    @PostMapping(value = "/trip_detail")
    public HttpEntity getTripAllDetailInfo(@RequestBody edu.fudan.common.entity.TripAllDetailInfo gtdi, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Post /api/v1/travel2service/trip_detail][gtdi:{}, headers:{}]","getTripAllDetailInfo",(gtdi != null ? gtdi.toString(): null), (headers != null ? headers.toString(): null));
        return ok(service.getTripAllDetailInfo(gtdi, headers));
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/trips")
    public HttpEntity queryAll(@RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Get /api/v1/travel2service/trips][headers:{}]","queryAll",(headers != null ? headers.toString(): null));
        return ok(service.queryAll(headers));
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/admin_trip")
    public HttpEntity adminQueryAll(@RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Get /api/v1/travel2service/admin_trip][headers:{}]","adminQueryAll",(headers != null ? headers.toString(): null));
        return ok(service.adminQueryAll(headers));
    }

}
