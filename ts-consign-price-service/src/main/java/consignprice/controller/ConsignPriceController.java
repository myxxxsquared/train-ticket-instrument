package consignprice.controller;

import consignprice.entity.ConsignPrice;














import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import consignprice.service.ConsignPriceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

/**
 * @author fdse
 */
@RestController
@RequestMapping("/api/v1/consignpriceservice")
public class ConsignPriceController { 
    private static final Logger logger = LoggerFactory.getLogger(ConsignPriceController.class);
















    @Autowired
    ConsignPriceService service;

    @GetMapping(path = "/welcome")
    public String home(@RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:None][headers:{}]","home",(headers != null ? headers.toString(): null));
        return "Welcome to [ ConsignPrice Service ] !";
    }

    @GetMapping(value = "/consignprice/{weight}/{isWithinRegion}")
    public HttpEntity getPriceByWeightAndRegion(@PathVariable String weight, @PathVariable String isWithinRegion,
                                                @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Get /api/v1/consignpriceservice/consignprice/{weight}/{isWithinRegion}][weight:{}, isWithinRegion:{}, headers:{}]","getPriceByWeightAndRegion",weight, isWithinRegion, (headers != null ? headers.toString(): null));
        return ok(service.getPriceByWeightAndRegion(Double.parseDouble(weight),
                Boolean.parseBoolean(isWithinRegion), headers));
    }

    @GetMapping(value = "/consignprice/price")
    public HttpEntity getPriceInfo(@RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Get /api/v1/consignpriceservice/consignprice/price][headers:{}]","getPriceInfo",(headers != null ? headers.toString(): null));
        return ok(service.queryPriceInformation(headers));
    }

    @GetMapping(value = "/consignprice/config")
    public HttpEntity getPriceConfig(@RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Get /api/v1/consignpriceservice/consignprice/config][headers:{}]","getPriceConfig",(headers != null ? headers.toString(): null));
        return ok(service.getPriceConfig(headers));
    }

    @PostMapping(value = "/consignprice")
    public HttpEntity modifyPriceConfig(@RequestBody ConsignPrice priceConfig,
                                        @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Post /api/v1/consignpriceservice/consignprice][priceConfig:{}, headers:{}]","modifyPriceConfig",(priceConfig != null ? priceConfig.toString(): null), (headers != null ? headers.toString(): null));
        return ok(service.createAndModifyPrice(priceConfig, headers));
    }
}
