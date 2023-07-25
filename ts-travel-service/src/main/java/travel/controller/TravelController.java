package travel.controller;

import edu.fudan.common.entity.TravelInfo;

















import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import edu.fudan.common.entity.TripAllDetailInfo;
import edu.fudan.common.entity.TripInfo;
import edu.fudan.common.entity.TripResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import edu.fudan.common.entity.TravelInfo;
import travel.entity.*;
import travel.service.TravelService;

import java.util.ArrayList;

import static org.springframework.http.ResponseEntity.ok;

/**
 * @author fdse
 */
@RestController
@RequestMapping("/api/v1/travelservice")

public class TravelController { 
    private static final Logger logger = LoggerFactory.getLogger(TravelController.class);



















    @Autowired
    private TravelService travelService;

    @GetMapping(path = "/welcome")
    public String home(@RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Get /api/v1/travelservice/welcome][headers:{}]","home",(headers != null ? headers.toString(): null));
        return "Welcome to [ Travel Service ] !";
    }

    @GetMapping(value = "/train_types/{tripId}")
    public HttpEntity getTrainTypeByTripId(@PathVariable String tripId,
                                           @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Get /api/v1/travelservice/train_types/{tripId}][tripId:{}, headers:{}]","getTrainTypeByTripId",tripId, (headers != null ? headers.toString(): null));
        return ok(travelService.getTrainTypeByTripId(tripId, headers));
    }

    @GetMapping(value = "/routes/{tripId}")
    public HttpEntity getRouteByTripId(@PathVariable String tripId,
                                       @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Get /api/v1/travelservice/routes/{tripId}][tripId:{}, headers:{}]","getRouteByTripId",tripId, (headers != null ? headers.toString(): null));
        //Route
        return ok(travelService.getRouteByTripId(tripId, headers));
    }

    @PostMapping(value = "/trips/routes")
    public HttpEntity getTripsByRouteId(@RequestBody ArrayList<String> routeIds,
                                        @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Post /api/v1/travelservice/trips/routes][routeIds:{}, headers:{}]","getTripsByRouteId",(routeIds != null ? routeIds.toString(): null), (headers != null ? headers.toString(): null));
        return ok(travelService.getTripByRoute(routeIds, headers));
    }

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/trips")
    public HttpEntity<?> createTrip(@RequestBody TravelInfo routeIds, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Post /api/v1/travelservice/trips][routeIds:{}, headers:{}]","createTrip",(routeIds != null ? routeIds.toString(): null), (headers != null ? headers.toString(): null));
        return new ResponseEntity<>(travelService.create(routeIds, headers), HttpStatus.CREATED);
    }

    /**
     * Return Trip only, no left ticket information
     *
     * @param tripId  trip id
     * @param headers headers
     * @return HttpEntity
     */
    @CrossOrigin(origins = "*")
    @GetMapping(value = "/trips/{tripId}")
    public HttpEntity retrieve(@PathVariable String tripId, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Get /api/v1/travelservice/trips/{tripId}][tripId:{}, headers:{}]","retrieve",tripId, (headers != null ? headers.toString(): null));
        return ok(travelService.retrieve(tripId, headers));
    }

    @CrossOrigin(origins = "*")
    @PutMapping(value = "/trips")
    public HttpEntity updateTrip(@RequestBody TravelInfo info, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Put /api/v1/travelservice/trips][info:{}, headers:{}]","updateTrip",(info != null ? info.toString(): null), (headers != null ? headers.toString(): null));
        return ok(travelService.update(info, headers));
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping(value = "/trips/{tripId}")
    public HttpEntity deleteTrip(@PathVariable String tripId, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Delete /api/v1/travelservice/trips/{tripId}][tripId:{}, headers:{}]","deleteTrip",tripId, (headers != null ? headers.toString(): null));
        return ok(travelService.delete(tripId, headers));
    }

    /**
     * Return Trips and the remaining tickets
     *
     * @param info    trip info
     * @param headers headers
     * @return HttpEntity
     */
    @CrossOrigin(origins = "*")
    @PostMapping(value = "/trips/left")
    public HttpEntity queryInfo(@RequestBody TripInfo info, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Post /api/v1/travelservice/trips/left][info:{}, headers:{}]","queryInfo",(info != null ? info.toString(): null), (headers != null ? headers.toString(): null));
        if (info.getStartPlace() == null || info.getStartPlace().length() == 0 ||
                info.getEndPlace() == null || info.getEndPlace().length() == 0 ||
                info.getDepartureTime() == null) {
            ArrayList<TripResponse> errorList = new ArrayList<>();
            return ok(errorList);
        }
        return ok(travelService.queryByBatch(info, headers));
    }

    /**
     * Return Trips and the remaining tickets
     *
     * @param info    trip info
     * @param headers headers
     * @return HttpEntity
     */
    @CrossOrigin(origins = "*")
    @PostMapping(value = "/trips/left_parallel")
    public HttpEntity queryInfoInparallel(@RequestBody TripInfo info, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Post /api/v1/travelservice/trips/left_parallel][info:{}, headers:{}]","queryInfoInparallel",(info != null ? info.toString(): null), (headers != null ? headers.toString(): null));
        if (info.getStartPlace() == null || info.getStartPlace().length() == 0 ||
                info.getEndPlace() == null || info.getEndPlace().length() == 0 ||
                info.getDepartureTime() == null) {
            ArrayList<TripResponse> errorList = new ArrayList<>();
            return ok(errorList);
        }
        return ok(travelService.queryInParallel(info, headers));
    }

    /**
     * Return a Trip and the remaining
     *
     * @param gtdi    trip all detail info
     * @param headers headers
     * @return HttpEntity
     */
    @CrossOrigin(origins = "*")
    @PostMapping(value = "/trip_detail")
    public HttpEntity getTripAllDetailInfo(@RequestBody TripAllDetailInfo gtdi, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Post /api/v1/travelservice/trip_detail][gtdi:{}, headers:{}]","getTripAllDetailInfo",(gtdi != null ? gtdi.toString(): null), (headers != null ? headers.toString(): null));
        return ok(travelService.getTripAllDetailInfo(gtdi, headers));
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/trips")
    public HttpEntity queryAll(@RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Get /api/v1/travelservice/trips][headers:{}]","queryAll",(headers != null ? headers.toString(): null));
        return ok(travelService.queryAll(headers));
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/admin_trip")
    public HttpEntity adminQueryAll(@RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Get /api/v1/travelservice/admin_trip][headers:{}]","adminQueryAll",(headers != null ? headers.toString(): null));
        return ok(travelService.adminQueryAll(headers));
    }

}
