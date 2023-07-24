package food.controller;

import food.service.StationFoodService;















import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/v1/stationfoodservice")
public class StationFoodController { 
    private static final Logger logger = LoggerFactory.getLogger(StationFoodController.class);
















    @Autowired
    StationFoodService stationFoodService;

    @GetMapping(path = "/stationfoodstores/welcome")
    public String home() {
        logger.info("[function name:home, API:None]");
        return "Welcome to [ Food store Service ] !";
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/stationfoodstores")
    public HttpEntity getAllFoodStores(@RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Get /api/v1/stationfoodservice/stationfoodstores][headers:{}]","getAllFoodStores",(headers != null ? headers.toString(): null));
        return ok(stationFoodService.listFoodStores(headers));
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/stationfoodstores/{stationId}")
    public HttpEntity getFoodStoresOfStation(@PathVariable String stationName, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Get /api/v1/stationfoodservice/stationfoodstores/{stationId}][stationName:{}, headers:{}]","getFoodStoresOfStation",stationName, (headers != null ? headers.toString(): null));
        return ok(stationFoodService.listFoodStoresByStationName(stationName, headers));
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/stationfoodstores")
    public HttpEntity getFoodStoresByStationNames(@RequestBody List<String> stationNameList) {
        logger.info("[function name:{}, API:Post /api/v1/stationfoodservice/stationfoodstores][stationNameList:{}]","getFoodStoresByStationNames",(stationNameList != null ? stationNameList.toString(): null));
        return ok(stationFoodService.getFoodStoresByStationNames(stationNameList));
    }
    @GetMapping("/stationfoodstores/bystoreid/{stationFoodStoreId}")
    public HttpEntity getFoodListByStationFoodStoreId(@PathVariable String stationFoodStoreId, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Get /api/v1/stationfoodservice/stationfoodstores/bystoreid/{stationFoodStoreId}][stationFoodStoreId:{}, headers:{}]","getFoodListByStationFoodStoreId",stationFoodStoreId, (headers != null ? headers.toString(): null));
        return ok(stationFoodService.getStaionFoodStoreById(stationFoodStoreId));
    }
}
