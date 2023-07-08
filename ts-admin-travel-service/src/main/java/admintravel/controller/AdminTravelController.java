package admintravel.controller;

import admintravel.service.AdminTravelService;





import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import edu.fudan.common.entity.TravelInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.*;

/**
 * @author fdse
 */
@RestController
@RequestMapping("/api/v1/admintravelservice")
public class AdminTravelController { 
    private static final Logger logger = LoggerFactory.getLogger(AdminTravelController.class);






    @Autowired
    AdminTravelService adminTravelService;

    @GetMapping(path = "/welcome")
    public String home(@RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][HttpHeaders:{}]","home",(headers != null ? headers.toString(): null));
        return "Welcome to [ AdminTravel Service ] !";
    }

    @CrossOrigin(origins = "*")
    @GetMapping(path = "/admintravel")
    public HttpEntity getAllTravels(@RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][HttpHeaders:{}]","getAllTravels",(headers != null ? headers.toString(): null));
        return ok(adminTravelService.getAllTravels(headers));
    }

    @PostMapping(value = "/admintravel")
    public HttpEntity addTravel(@RequestBody TravelInfo request, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][TravelInfo:{}, HttpHeaders:{}]","addTravel",(request != null ? request.toString(): null), (headers != null ? headers.toString(): null));
        return ok(adminTravelService.addTravel(request, headers));
    }

    @PutMapping(value = "/admintravel")
    public HttpEntity updateTravel(@RequestBody TravelInfo request, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][TravelInfo:{}, HttpHeaders:{}]","updateTravel",(request != null ? request.toString(): null), (headers != null ? headers.toString(): null));
        return ok(adminTravelService.updateTravel(request, headers));
    }

    @DeleteMapping(value = "/admintravel/{tripId}")
    public HttpEntity deleteTravel(@PathVariable String tripId, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][String:{}, HttpHeaders:{}]","deleteTravel",tripId, (headers != null ? headers.toString(): null));
        return ok(adminTravelService.deleteTravel(tripId, headers));
    }

}
