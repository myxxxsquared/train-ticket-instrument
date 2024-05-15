package travelplan.controller;


import org.springframework.beans.factory.annotation.Autowired;
















import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private static final Logger logger = LogManager.getLogger(TravelPlanController.class);




















    @Autowired
    TravelPlanService travelPlanService;

    @GetMapping(path = "/welcome" )
    public String home() {
        return "Welcome to [ TravelPlan Service ] !";
    }

    @PostMapping(value="/travelPlan/transferResult" )
    public HttpEntity getTransferResult(@RequestBody TransferTravelInfo info, @RequestHeader HttpHeaders headers) {
        return ok(travelPlanService.getTransferSearch(info, headers));
    }

    @PostMapping(value="/travelPlan/cheapest")
    public HttpEntity getByCheapest(@RequestBody TripInfo queryInfo, @RequestHeader HttpHeaders headers) {
        return ok(travelPlanService.getCheapest(queryInfo, headers));
    }

    @PostMapping(value="/travelPlan/quickest")
    public HttpEntity getByQuickest(@RequestBody TripInfo queryInfo, @RequestHeader HttpHeaders headers) {
        return ok(travelPlanService.getQuickest(queryInfo, headers));
    }

    @PostMapping(value="/travelPlan/minStation")
    public HttpEntity getByMinStation(@RequestBody TripInfo queryInfo, @RequestHeader HttpHeaders headers) {
        return ok(travelPlanService.getMinStation(queryInfo, headers));
    }

}
