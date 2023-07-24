package fdse.microservice.controller;

import edu.fudan.common.util.Response;












import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import fdse.microservice.entity.*;
import fdse.microservice.service.StationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/v1/stationservice")
public class StationController { 
    private static final Logger logger = LoggerFactory.getLogger(StationController.class);














    @Autowired
    private StationService stationService;

    @GetMapping(path = "/welcome")
    public String home(@RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:None][headers:{}]","home",(headers != null ? headers.toString(): null));
        return "Welcome to [ Station Service ] !";
    }

    @GetMapping(value = "/stations")
    public HttpEntity query(@RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Get /api/v1/stationservice/stations][headers:{}]","query",(headers != null ? headers.toString(): null));
        return ok(stationService.query(headers));
    }

    @PostMapping(value = "/stations")
    public ResponseEntity<Response> create(@RequestBody Station station, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:None][station:{}, headers:{}]","create",(station != null ? station.toString(): null), (headers != null ? headers.toString(): null));
        return new ResponseEntity<>(stationService.create(station, headers), HttpStatus.CREATED);
    }

    @PutMapping(value = "/stations")
    public HttpEntity update(@RequestBody Station station, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Put /api/v1/stationservice/stations][station:{}, headers:{}]","update",(station != null ? station.toString(): null), (headers != null ? headers.toString(): null));
        return ok(stationService.update(station, headers));
    }

    @DeleteMapping(value = "/stations/{stationsId}")
    public ResponseEntity<Response> delete(@PathVariable String stationsId, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:None][stationsId:{}, headers:{}]","delete",stationsId, (headers != null ? headers.toString(): null));
        return ok(stationService.delete(stationsId, headers));
    }



    // according to station name ---> query station id
    @GetMapping(value = "/stations/id/{stationNameForId}")
    public HttpEntity queryForStationId(@PathVariable(value = "stationNameForId")
                                                String stationName, @RequestHeader HttpHeaders headers) {
        return ok(stationService.queryForId(stationName, headers));
    }

    // according to station name list --->  query all station ids
    @CrossOrigin(origins = "*")
    @PostMapping(value = "/stations/idlist")
    public HttpEntity queryForIdBatch(@RequestBody List<String> stationNameList, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Post /api/v1/stationservice/stations/idlist][stationNameList:{}, headers:{}]","queryForIdBatch",(stationNameList != null ? stationNameList.toString(): null), (headers != null ? headers.toString(): null));
        return ok(stationService.queryForIdBatch(stationNameList, headers));
    }

    // according to station id ---> query station name
    @CrossOrigin(origins = "*")
    @GetMapping(value = "/stations/name/{stationIdForName}")
    public HttpEntity queryById(@PathVariable(value = "stationIdForName")
                                        String stationId, @RequestHeader HttpHeaders headers) {
        // string
        return ok(stationService.queryById(stationId, headers));
    }

    // according to station id list  ---> query all station names
    @CrossOrigin(origins = "*")
    @PostMapping(value = "/stations/namelist")
    public HttpEntity queryForNameBatch(@RequestBody List<String> stationIdList, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Post /api/v1/stationservice/stations/namelist][stationIdList:{}, headers:{}]","queryForNameBatch",(stationIdList != null ? stationIdList.toString(): null), (headers != null ? headers.toString(): null));
        return ok(stationService.queryByIdBatch(stationIdList, headers));
    }

}
