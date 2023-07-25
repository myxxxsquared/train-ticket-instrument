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
        logger.info("[function name:{}, API:Get /api/v1/adminbasicservice/welcome][headers:{}]","home",(headers != null ? headers.toString(): null));
        return "Welcome to [ AdminBasicInfo Service ] !";
    }

    @CrossOrigin(origins = "*")
    @GetMapping(path = "/adminbasic/contacts")
    public HttpEntity getAllContacts(@RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Get /api/v1/adminbasicservice/adminbasic/contacts][headers:{}]","getAllContacts",(headers != null ? headers.toString(): null));
        return ok(adminBasicInfoService.getAllContacts(headers));
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping(path = "/adminbasic/contacts/{contactsId}")
    public HttpEntity deleteContacts(@PathVariable String contactsId, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Delete /api/v1/adminbasicservice/adminbasic/contacts/{contactsId}][contactsId:{}, headers:{}]","deleteContacts",contactsId, (headers != null ? headers.toString(): null));
        return ok(adminBasicInfoService.deleteContact(contactsId, headers));
    }

    @CrossOrigin(origins = "*")
    @PutMapping(path = "/adminbasic/contacts")
    public HttpEntity modifyContacts(@RequestBody Contacts mci, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Put /api/v1/adminbasicservice/adminbasic/contacts][mci:{}, headers:{}]","modifyContacts",(mci != null ? mci.toString(): null), (headers != null ? headers.toString(): null));
        return ok(adminBasicInfoService.modifyContact(mci, headers));
    }

    @CrossOrigin(origins = "*")
    @PostMapping(path = "/adminbasic/contacts")
    public HttpEntity addContacts(@RequestBody Contacts c, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Post /api/v1/adminbasicservice/adminbasic/contacts][c:{}, headers:{}]","addContacts",(c != null ? c.toString(): null), (headers != null ? headers.toString(): null));
        return ok(adminBasicInfoService.addContact(c, headers));
    }

    @CrossOrigin(origins = "*")
    @GetMapping(path = "/adminbasic/stations")
    public HttpEntity getAllStations(@RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Get /api/v1/adminbasicservice/adminbasic/stations][headers:{}]","getAllStations",(headers != null ? headers.toString(): null));
        return ok(adminBasicInfoService.getAllStations(headers));
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping(path = "/adminbasic/stations/{id}")
    public HttpEntity deleteStation(@PathVariable String id, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Delete /api/v1/adminbasicservice/adminbasic/stations/{id}][id:{}, headers:{}]","deleteStation",id, (headers != null ? headers.toString(): null));
        return ok(adminBasicInfoService.deleteStation(id, headers));
    }

    @CrossOrigin(origins = "*")
    @PutMapping(path = "/adminbasic/stations")
    public HttpEntity modifyStation(@RequestBody Station s, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Put /api/v1/adminbasicservice/adminbasic/stations][s:{}, headers:{}]","modifyStation",(s != null ? s.toString(): null), (headers != null ? headers.toString(): null));
        return ok(adminBasicInfoService.modifyStation(s, headers));
    }

    @CrossOrigin(origins = "*")
    @PostMapping(path = "/adminbasic/stations")
    public HttpEntity addStation(@RequestBody Station s, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Post /api/v1/adminbasicservice/adminbasic/stations][s:{}, headers:{}]","addStation",(s != null ? s.toString(): null), (headers != null ? headers.toString(): null));
        return ok(adminBasicInfoService.addStation(s, headers));
    }

    @CrossOrigin(origins = "*")
    @GetMapping(path = "/adminbasic/trains")
    public HttpEntity getAllTrains(@RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Get /api/v1/adminbasicservice/adminbasic/trains][headers:{}]","getAllTrains",(headers != null ? headers.toString(): null));
        return ok(adminBasicInfoService.getAllTrains(headers));
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping(path = "/adminbasic/trains/{id}")
    public HttpEntity deleteTrain(@PathVariable String id, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Delete /api/v1/adminbasicservice/adminbasic/trains/{id}][id:{}, headers:{}]","deleteTrain",id, (headers != null ? headers.toString(): null));
        return ok(adminBasicInfoService.deleteTrain(id, headers));
    }

    @CrossOrigin(origins = "*")
    @PutMapping(path = "/adminbasic/trains")
    public HttpEntity modifyTrain(@RequestBody TrainType t, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Put /api/v1/adminbasicservice/adminbasic/trains][t:{}, headers:{}]","modifyTrain",(t != null ? t.toString(): null), (headers != null ? headers.toString(): null));
        return ok(adminBasicInfoService.modifyTrain(t, headers));
    }

    @CrossOrigin(origins = "*")
    @PostMapping(path = "/adminbasic/trains")
    public HttpEntity addTrain(@RequestBody TrainType t, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Post /api/v1/adminbasicservice/adminbasic/trains][t:{}, headers:{}]","addTrain",(t != null ? t.toString(): null), (headers != null ? headers.toString(): null));
        return ok(adminBasicInfoService.addTrain(t, headers));
    }

    @CrossOrigin(origins = "*")
    @GetMapping(path = "/adminbasic/configs")
    public HttpEntity getAllConfigs(@RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Get /api/v1/adminbasicservice/adminbasic/configs][headers:{}]","getAllConfigs",(headers != null ? headers.toString(): null));
        return ok(adminBasicInfoService.getAllConfigs(headers));
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping(path = "/adminbasic/configs/{name}")
    public HttpEntity deleteConfig(@PathVariable String name, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Delete /api/v1/adminbasicservice/adminbasic/configs/{name}][name:{}, headers:{}]","deleteConfig",name, (headers != null ? headers.toString(): null));
        return ok(adminBasicInfoService.deleteConfig(name, headers));
    }

    @CrossOrigin(origins = "*")
    @PutMapping(path = "/adminbasic/configs")
    public HttpEntity modifyConfig(@RequestBody Config c, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Put /api/v1/adminbasicservice/adminbasic/configs][c:{}, headers:{}]","modifyConfig",(c != null ? c.toString(): null), (headers != null ? headers.toString(): null));
        return ok(adminBasicInfoService.modifyConfig(c, headers));
    }

    @CrossOrigin(origins = "*")
    @PostMapping(path = "/adminbasic/configs")
    public HttpEntity addConfig(@RequestBody Config c, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Post /api/v1/adminbasicservice/adminbasic/configs][c:{}, headers:{}]","addConfig",(c != null ? c.toString(): null), (headers != null ? headers.toString(): null));
        return ok(adminBasicInfoService.addConfig(c, headers));
    }

    @CrossOrigin(origins = "*")
    @GetMapping(path = "/adminbasic/prices")
    public HttpEntity getAllPrices(@RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Get /api/v1/adminbasicservice/adminbasic/prices][headers:{}]","getAllPrices",(headers != null ? headers.toString(): null));
        return ok(adminBasicInfoService.getAllPrices(headers));
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping(path = "/adminbasic/prices/{pricesId}")
    public HttpEntity deletePrice(@PathVariable String pricesId, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Delete /api/v1/adminbasicservice/adminbasic/prices/{pricesId}][pricesId:{}, headers:{}]","deletePrice",pricesId, (headers != null ? headers.toString(): null));
        return ok(adminBasicInfoService.deletePrice(pricesId, headers));
    }

    @CrossOrigin(origins = "*")
    @PutMapping(path = "/adminbasic/prices")
    public HttpEntity modifyPrice(@RequestBody PriceInfo pi, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Put /api/v1/adminbasicservice/adminbasic/prices][pi:{}, headers:{}]","modifyPrice",(pi != null ? pi.toString(): null), (headers != null ? headers.toString(): null));
        return ok(adminBasicInfoService.modifyPrice(pi, headers));
    }

    @CrossOrigin(origins = "*")
    @PostMapping(path = "/adminbasic/prices")
    public HttpEntity addPrice(@RequestBody PriceInfo pi, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Post /api/v1/adminbasicservice/adminbasic/prices][pi:{}, headers:{}]","addPrice",(pi != null ? pi.toString(): null), (headers != null ? headers.toString(): null));
        return ok(adminBasicInfoService.addPrice(pi, headers));
    }

}
