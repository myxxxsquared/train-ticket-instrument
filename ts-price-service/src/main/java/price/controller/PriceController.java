package price.controller;


import org.springframework.beans.factory.annotation.Autowired;
















import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import price.entity.PriceConfig;
import price.service.PriceService;

import java.util.List;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;

/**
 * @author fdse
 */
@RestController
@RequestMapping("/api/v1/priceservice")
public class PriceController { 
    private static final Logger logger = LogManager.getLogger(PriceController.class);




















    @Autowired
    PriceService service;

    @GetMapping(path = "/prices/welcome")
    public String home() {
        return "Welcome to [ Price Service ] !";
    }

    @GetMapping(value = "/prices/{routeId}/{trainType}")
    public HttpEntity findByRouteIdAndTrainType(@PathVariable String routeId, @PathVariable String trainType,
                            @RequestHeader HttpHeaders headers) {
        return ok(service.findByRouteIdAndTrainType(routeId, trainType, headers));
    }

    @PostMapping(value = "/prices/byRouteIdsAndTrainTypes")
    public HttpEntity findByRouteIdsAndTrainTypes(@RequestBody List<String> ridsAndTts,
                            @RequestHeader HttpHeaders headers) {
        return ok(service.findByRouteIdsAndTrainTypes(ridsAndTts, headers));
    }

    @GetMapping(value = "/prices")
    public HttpEntity queryAll(@RequestHeader HttpHeaders headers) {
        return ok(service.findAllPriceConfig(headers));
    }

    @PostMapping(value = "/prices")
    public HttpEntity<?> create(@RequestBody PriceConfig info,
                                @RequestHeader HttpHeaders headers) {
        return new ResponseEntity<>(service.createNewPriceConfig(info, headers), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/prices/{pricesId}")
    public HttpEntity delete(@PathVariable String pricesId, @RequestHeader HttpHeaders headers) {
        return ok(service.deletePriceConfig(pricesId, headers));
    }

    @PutMapping(value = "/prices")
    public HttpEntity update(@RequestBody PriceConfig info, @RequestHeader HttpHeaders headers) {
        return ok(service.updatePriceConfig(info, headers));
    }
}
