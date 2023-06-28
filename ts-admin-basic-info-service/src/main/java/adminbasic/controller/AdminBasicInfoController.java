package adminbasic.controller;

import adminbasic.entity.*;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import adminbasic.service.AdminBasicInfoService;
import edu.fudan.common.entity.Config;
import edu.fudan.common.entity.Contacts;
import edu.fudan.common.entity.Station;
import edu.fudan.common.entity.TrainType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

/**
 * @author fdse
 */
@RestController
@RequestMapping("/api/v1/adminbasicservice")
public class AdminBasicInfoController { 
    private static final Logger logger = LoggerFactory.getLogger(AdminBasicInfoController.class);





    @Autowired
    AdminBasicInfoService adminBasicInfoService;

    @GetMapping(path = "/welcome")
    public String home(@RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][HttpHeaders:{}]","home",(headers != null ? headers.toString(): null));
        return "Welcome to [ AdminBasicInfo Service ] !";
    }

    @CrossOrigin(origins = "*")
    @GetMapping(path = "/adminbasic/contacts")
    public HttpEntity getAllContacts(@RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][HttpHeaders:{}]","getAllContacts",(headers != null ? headers.toString(): null));
        return ok(adminBasicInfoService.getAllContacts(headers));
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping(path = "/adminbasic/contacts/{contactsId}")
    public HttpEntity deleteContacts(@PathVariable String contactsId, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][String:{}, HttpHeaders:{}]","deleteContacts",contactsId, (headers != null ? headers.toString(): null));
        return ok(adminBasicInfoService.deleteContact(contactsId, headers));
    }

    @CrossOrigin(origins = "*")
    @PutMapping(path = "/adminbasic/contacts")
    public HttpEntity modifyContacts(@RequestBody Contacts mci, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][Contacts:{}, HttpHeaders:{}]","modifyContacts",(mci != null ? mci.toString(): null), (headers != null ? headers.toString(): null));
        return ok(adminBasicInfoService.modifyContact(mci, headers));
    }

    @CrossOrigin(origins = "*")
    @PostMapping(path = "/adminbasic/contacts")
    public HttpEntity addContacts(@RequestBody Contacts c, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][Contacts:{}, HttpHeaders:{}]","addContacts",(c != null ? c.toString(): null), (headers != null ? headers.toString(): null));
        return ok(adminBasicInfoService.addContact(c, headers));
    }

    @CrossOrigin(origins = "*")
    @GetMapping(path = "/adminbasic/stations")
    public HttpEntity getAllStations(@RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][HttpHeaders:{}]","getAllStations",(headers != null ? headers.toString(): null));
        return ok(adminBasicInfoService.getAllStations(headers));
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping(path = "/adminbasic/stations/{id}")
    public HttpEntity deleteStation(@PathVariable String id, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][String:{}, HttpHeaders:{}]","deleteStation",id, (headers != null ? headers.toString(): null));
        return ok(adminBasicInfoService.deleteStation(id, headers));
    }

    @CrossOrigin(origins = "*")
    @PutMapping(path = "/adminbasic/stations")
    public HttpEntity modifyStation(@RequestBody Station s, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][Station:{}, HttpHeaders:{}]","modifyStation",(s != null ? s.toString(): null), (headers != null ? headers.toString(): null));
        return ok(adminBasicInfoService.modifyStation(s, headers));
    }

    @CrossOrigin(origins = "*")
    @PostMapping(path = "/adminbasic/stations")
    public HttpEntity addStation(@RequestBody Station s, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][Station:{}, HttpHeaders:{}]","addStation",(s != null ? s.toString(): null), (headers != null ? headers.toString(): null));
        return ok(adminBasicInfoService.addStation(s, headers));
    }

    @CrossOrigin(origins = "*")
    @GetMapping(path = "/adminbasic/trains")
    public HttpEntity getAllTrains(@RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][HttpHeaders:{}]","getAllTrains",(headers != null ? headers.toString(): null));
        return ok(adminBasicInfoService.getAllTrains(headers));
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping(path = "/adminbasic/trains/{id}")
    public HttpEntity deleteTrain(@PathVariable String id, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][String:{}, HttpHeaders:{}]","deleteTrain",id, (headers != null ? headers.toString(): null));
        return ok(adminBasicInfoService.deleteTrain(id, headers));
    }

    @CrossOrigin(origins = "*")
    @PutMapping(path = "/adminbasic/trains")
    public HttpEntity modifyTrain(@RequestBody TrainType t, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][TrainType:{}, HttpHeaders:{}]","modifyTrain",(t != null ? t.toString(): null), (headers != null ? headers.toString(): null));
        return ok(adminBasicInfoService.modifyTrain(t, headers));
    }

    @CrossOrigin(origins = "*")
    @PostMapping(path = "/adminbasic/trains")
    public HttpEntity addTrain(@RequestBody TrainType t, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][TrainType:{}, HttpHeaders:{}]","addTrain",(t != null ? t.toString(): null), (headers != null ? headers.toString(): null));
        return ok(adminBasicInfoService.addTrain(t, headers));
    }

    @CrossOrigin(origins = "*")
    @GetMapping(path = "/adminbasic/configs")
    public HttpEntity getAllConfigs(@RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][HttpHeaders:{}]","getAllConfigs",(headers != null ? headers.toString(): null));
        return ok(adminBasicInfoService.getAllConfigs(headers));
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping(path = "/adminbasic/configs/{name}")
    public HttpEntity deleteConfig(@PathVariable String name, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][String:{}, HttpHeaders:{}]","deleteConfig",name, (headers != null ? headers.toString(): null));
        return ok(adminBasicInfoService.deleteConfig(name, headers));
    }

    @CrossOrigin(origins = "*")
    @PutMapping(path = "/adminbasic/configs")
    public HttpEntity modifyConfig(@RequestBody Config c, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][Config:{}, HttpHeaders:{}]","modifyConfig",(c != null ? c.toString(): null), (headers != null ? headers.toString(): null));
        return ok(adminBasicInfoService.modifyConfig(c, headers));
    }

    @CrossOrigin(origins = "*")
    @PostMapping(path = "/adminbasic/configs")
    public HttpEntity addConfig(@RequestBody Config c, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][Config:{}, HttpHeaders:{}]","addConfig",(c != null ? c.toString(): null), (headers != null ? headers.toString(): null));
        return ok(adminBasicInfoService.addConfig(c, headers));
    }

    @CrossOrigin(origins = "*")
    @GetMapping(path = "/adminbasic/prices")
    public HttpEntity getAllPrices(@RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][HttpHeaders:{}]","getAllPrices",(headers != null ? headers.toString(): null));
        return ok(adminBasicInfoService.getAllPrices(headers));
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping(path = "/adminbasic/prices/{pricesId}")
    public HttpEntity deletePrice(@PathVariable String pricesId, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][String:{}, HttpHeaders:{}]","deletePrice",pricesId, (headers != null ? headers.toString(): null));
        return ok(adminBasicInfoService.deletePrice(pricesId, headers));
    }

    @CrossOrigin(origins = "*")
    @PutMapping(path = "/adminbasic/prices")
    public HttpEntity modifyPrice(@RequestBody PriceInfo pi, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][PriceInfo:{}, HttpHeaders:{}]","modifyPrice",(pi != null ? pi.toString(): null), (headers != null ? headers.toString(): null));
        return ok(adminBasicInfoService.modifyPrice(pi, headers));
    }

    @CrossOrigin(origins = "*")
    @PostMapping(path = "/adminbasic/prices")
    public HttpEntity addPrice(@RequestBody PriceInfo pi, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][PriceInfo:{}, HttpHeaders:{}]","addPrice",(pi != null ? pi.toString(): null), (headers != null ? headers.toString(): null));
        return ok(adminBasicInfoService.addPrice(pi, headers));
    }

}
