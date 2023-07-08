package price.controller;


import org.springframework.beans.factory.annotation.Autowired;




import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(PriceController.class);






    @Autowired
    PriceService service;

    @GetMapping(path = "/prices/welcome")
    public String home() {
        logger.info("[function name:home]");
        return "Welcome to [ Price Service ] !";
    }

    @GetMapping(value = "/prices/{routeId}/{trainType}")
    public HttpEntity findByRouteIdAndTrainType(@PathVariable String routeId, @PathVariable String trainType,
                            @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][String:{}, String:{}, HttpHeaders:{}]","findByRouteIdAndTrainType",routeId, trainType, (headers != null ? headers.toString(): null));
        return ok(service.findByRouteIdAndTrainType(routeId, trainType, headers));
    }

    @PostMapping(value = "/prices/byRouteIdsAndTrainTypes")
    public HttpEntity findByRouteIdsAndTrainTypes(@RequestBody List<String> ridsAndTts,
                            @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][List<String>:{}, HttpHeaders:{}]","findByRouteIdsAndTrainTypes",(ridsAndTts != null ? ridsAndTts.toString(): null), (headers != null ? headers.toString(): null));
        return ok(service.findByRouteIdsAndTrainTypes(ridsAndTts, headers));
    }

    @GetMapping(value = "/prices")
    public HttpEntity queryAll(@RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][HttpHeaders:{}]","queryAll",(headers != null ? headers.toString(): null));
        return ok(service.findAllPriceConfig(headers));
    }

    @PostMapping(value = "/prices")
    public HttpEntity<?> create(@RequestBody PriceConfig info,
                                @RequestHeader HttpHeaders headers) {
        return new ResponseEntity<>(service.createNewPriceConfig(info, headers), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/prices/{pricesId}")
    public HttpEntity delete(@PathVariable String pricesId, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][String:{}, HttpHeaders:{}]","delete",pricesId, (headers != null ? headers.toString(): null));
        return ok(service.deletePriceConfig(pricesId, headers));
    }

    @PutMapping(value = "/prices")
    public HttpEntity update(@RequestBody PriceConfig info, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][PriceConfig:{}, HttpHeaders:{}]","update",(info != null ? info.toString(): null), (headers != null ? headers.toString(): null));
        return ok(service.updatePriceConfig(info, headers));
    }
}
