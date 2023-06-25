package travelplan.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import edu.fudan.common.entity.TripInfo;
import travelplan.entity.TransferTravelInfo;
import travelplan.service.TravelPlanService;

import static org.springframework.http.ResponseEntity.ok;

/**
 * @author fdse
 */
@RestController
@RequestMapping("api/v1/travelplanservice")
public class TravelPlanController { 
    private static final Logger logger = LoggerFactory.getLogger(TravelPlanController.class);


    @Autowired
    TravelPlanService travelPlanService;

    @GetMapping(path = "/welcome" )
    public String home() {
        logger.info("[function name:home]");
        return "Welcome to [ TravelPlan Service ] !";
    }

    @PostMapping(value="/travelPlan/transferResult" )
    public HttpEntity getTransferResult(@RequestBody TransferTravelInfo info, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][TransferTravelInfo:{}, HttpHeaders:{}]","getTransferResult",info.toString(), headers.toString());
        return ok(travelPlanService.getTransferSearch(info, headers));
    }

    @PostMapping(value="/travelPlan/cheapest")
    public HttpEntity getByCheapest(@RequestBody TripInfo queryInfo, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][TripInfo:{}, HttpHeaders:{}]","getByCheapest",queryInfo.toString(), headers.toString());
        return ok(travelPlanService.getCheapest(queryInfo, headers));
    }

    @PostMapping(value="/travelPlan/quickest")
    public HttpEntity getByQuickest(@RequestBody TripInfo queryInfo, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][TripInfo:{}, HttpHeaders:{}]","getByQuickest",queryInfo.toString(), headers.toString());
        return ok(travelPlanService.getQuickest(queryInfo, headers));
    }

    @PostMapping(value="/travelPlan/minStation")
    public HttpEntity getByMinStation(@RequestBody TripInfo queryInfo, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][TripInfo:{}, HttpHeaders:{}]","getByMinStation",queryInfo.toString(), headers.toString());
        return ok(travelPlanService.getMinStation(queryInfo, headers));
    }

}
