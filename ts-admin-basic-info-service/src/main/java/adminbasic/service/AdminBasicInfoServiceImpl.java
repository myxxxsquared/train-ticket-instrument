package adminbasic.service;

import adminbasic.entity.*;
















import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import edu.fudan.common.entity.Config;
import edu.fudan.common.entity.Contacts;
import edu.fudan.common.entity.Station;
import edu.fudan.common.entity.TrainType;
import edu.fudan.common.util.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


/**
 * @author fdse
 */
@Service
public class AdminBasicInfoServiceImpl implements AdminBasicInfoService { 
    private static final Logger logger = LoggerFactory.getLogger(AdminBasicInfoServiceImpl.class);


















    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DiscoveryClient discoveryClient;

    private String getServiceUrl(String serviceName) {
        logger.info("[function name:{}][serviceName:{}]","getServiceUrl",serviceName);
        return "http://" + serviceName;
    }

    @Override
    public Response getAllContacts(HttpHeaders headers) {
        logger.info("[function name:{}][headers:{}]","getAllContacts",(headers != null ? headers.toString(): null));
        Response result;
        HttpEntity requestEntity = new HttpEntity(headers);
        String contacts_service_url = getServiceUrl("ts-contacts-service");
        ResponseEntity<Response> re = restTemplate.exchange(
                contacts_service_url + "/api/v1/contactservice/contacts",
                HttpMethod.GET,
                requestEntity,
                Response.class);
        logger.info("[status code:{}, url:{}, type:{}, headers:{}]",re.getStatusCode(),
                contacts_service_url + "/api/v1/contactservice/contacts","GET",headers);
        result = re.getBody();

        return result;
    }

    @Override
    public Response deleteContact(String contactsId, HttpHeaders headers) {
        logger.info("[function name:{}][contactsId:{}, headers:{}]","deleteContact",contactsId, (headers != null ? headers.toString(): null));
        Response result;
        HttpEntity requestEntity = new HttpEntity(headers);
        String contacts_service_url = getServiceUrl("ts-contacts-service");
        ResponseEntity<Response> re = restTemplate.exchange(
                contacts_service_url + "/api/v1/contactservice/contacts/" + contactsId,
                HttpMethod.DELETE,
                requestEntity,
                Response.class);
        logger.info("[status code:{}, url:{}, type:{}, headers:{}]",re.getStatusCode(),
                contacts_service_url + "/api/v1/contactservice/contacts/" + contactsId,"DELETE",headers);
        result = re.getBody();

        return result;
    }

    @Override
    public Response modifyContact(Contacts mci, HttpHeaders headers) {
        logger.info("[function name:{}][mci:{}, headers:{}]","modifyContact",(mci != null ? mci.toString(): null), (headers != null ? headers.toString(): null));
        Response result;
        HttpEntity requestEntity = new HttpEntity(mci, headers);
        String contacts_service_url = getServiceUrl("ts-contacts-service");
        ResponseEntity<Response> re = restTemplate.exchange(
                contacts_service_url + "/api/v1/contactservice/contacts",
                HttpMethod.PUT,
                requestEntity,
                Response.class);
        logger.info("[status code:{}, url:{}, type:{}, headers:{}]",re.getStatusCode(),
                contacts_service_url + "/api/v1/contactservice/contacts","PUT",headers);
        result = re.getBody();

        return result;
    }


    @Override
    public Response addContact(Contacts c, HttpHeaders headers) {
        logger.info("[function name:{}][c:{}, headers:{}]","addContact",(c != null ? c.toString(): null), (headers != null ? headers.toString(): null));
        Response result;
        HttpEntity requestEntity = new HttpEntity(c, headers);
        String contacts_service_url = getServiceUrl("ts-contacts-service");
        ResponseEntity<Response> re = restTemplate.exchange(
                contacts_service_url + "/api/v1/contactservice/contacts/admin",
                HttpMethod.POST,
                requestEntity,
                Response.class);
        logger.info("[status code:{}, url:{}, type:{}, headers:{}]",re.getStatusCode(),
                contacts_service_url + "/api/v1/contactservice/contacts/admin","POST",headers);
        result = re.getBody();

        return result;
    }

    @Override
    public Response getAllStations(HttpHeaders headers) {
        logger.info("[function name:{}][headers:{}]","getAllStations",(headers != null ? headers.toString(): null));
        HttpEntity requestEntity = new HttpEntity(headers);
        String station_service_url = getServiceUrl("ts-station-service");
        String stations = station_service_url + "/api/v1/stationservice/stations";
        ResponseEntity<Response> re = restTemplate.exchange(
                stations,
                HttpMethod.GET,
                requestEntity,
                Response.class);
        logger.info("[status code:{}, url:{}, type:{}, headers:{}]",re.getStatusCode(),
                stations,"GET",headers);

        return re.getBody();


    }

    @Override
    public Response addStation(Station s, HttpHeaders headers) {
        logger.info("[function name:{}][s:{}, headers:{}]","addStation",(s != null ? s.toString(): null), (headers != null ? headers.toString(): null));
        Response result;
        HttpEntity requestEntity = new HttpEntity(s, headers);
        String station_service_url = getServiceUrl("ts-station-service");
        String stations = station_service_url + "/api/v1/stationservice/stations";
        ResponseEntity<Response> re = restTemplate.exchange(
                stations,
                HttpMethod.POST,
                requestEntity,
                Response.class);
        logger.info("[status code:{}, url:{}, type:{}, headers:{}]",re.getStatusCode(),
                stations,"POST",headers);
        result = re.getBody();
        return result;
    }

    @Override
    public Response deleteStation(String id, HttpHeaders headers) {
        logger.info("[function name:{}][id:{}, headers:{}]","deleteStation",id, (headers != null ? headers.toString(): null));
        Response result;
        HttpEntity requestEntity = new HttpEntity(headers);
        String station_service_url = getServiceUrl("ts-station-service");
        String path = station_service_url + "/api/v1/stationservice/stations/" + id;
        ResponseEntity<Response> re = restTemplate.exchange(
                path,
                HttpMethod.DELETE,
                requestEntity,
                Response.class);
        logger.info("[status code:{}, url:{}, type:{}, headers:{}]",re.getStatusCode(),
                path,"DELETE",headers);
        logger.info("[status code:{}, url:{}, type:{}, headers:{}]",re.getStatusCode(),
                path,"DELETE",headers);
        result = re.getBody();
        return result;
    }

    @Override
    public Response modifyStation(Station s, HttpHeaders headers) {
        logger.info("[function name:{}][s:{}, headers:{}]","modifyStation",(s != null ? s.toString(): null), (headers != null ? headers.toString(): null));
        Response result;
        HttpEntity requestEntity = new HttpEntity(s, headers);
        String station_service_url = getServiceUrl("ts-station-service");
        String stations = station_service_url + "/api/v1/stationservice/stations";
        ResponseEntity<Response> re = restTemplate.exchange(
                stations,
                HttpMethod.PUT,
                requestEntity,
                Response.class);
        logger.info("[status code:{}, url:{}, type:{}, headers:{}]",re.getStatusCode(),
                stations,"PUT",headers);
        result = re.getBody();

        return result;

    }

    @Override
    public Response getAllTrains(HttpHeaders headers) {
        logger.info("[function name:{}][headers:{}]","getAllTrains",(headers != null ? headers.toString(): null));
        HttpEntity requestEntity = new HttpEntity(headers);
        String train_service_url = getServiceUrl("ts-train-service");
        String trains = train_service_url + "/api/v1/trainservice/trains";
        ResponseEntity<Response> re = restTemplate.exchange(
                trains,
                HttpMethod.GET,
                requestEntity,
                Response.class);
        logger.info("[status code:{}, url:{}, type:{}, headers:{}]",re.getStatusCode(),
                trains,"GET",headers);

        return re.getBody();

    }

    @Override
    public Response addTrain(TrainType t, HttpHeaders headers) {
        logger.info("[function name:{}][t:{}, headers:{}]","addTrain",(t != null ? t.toString(): null), (headers != null ? headers.toString(): null));
        Response result;
        HttpEntity requestEntity = new HttpEntity(t, headers);
        String train_service_url = getServiceUrl("ts-train-service");
        String trains = train_service_url + "/api/v1/trainservice/trains";
        ResponseEntity<Response> re = restTemplate.exchange(
                trains,
                HttpMethod.POST,
                requestEntity,
                Response.class);
        logger.info("[status code:{}, url:{}, type:{}, headers:{}]",re.getStatusCode(),
                trains,"POST",headers);
        result = re.getBody();
        return result;

    }

    @Override
    public Response deleteTrain(String id, HttpHeaders headers) {
        logger.info("[function name:{}][id:{}, headers:{}]","deleteTrain",id, (headers != null ? headers.toString(): null));
        Response result;
        HttpEntity requestEntity = new HttpEntity(headers);
        String train_service_url = getServiceUrl("ts-train-service");
        ResponseEntity<Response> re = restTemplate.exchange(
                train_service_url + "/api/v1/trainservice/trains/" + id,
                HttpMethod.DELETE,
                requestEntity,
                Response.class);
        logger.info("[status code:{}, url:{}, type:{}, headers:{}]",re.getStatusCode(),
                train_service_url + "/api/v1/trainservice/trains/" + id,"DELETE",headers);
        result = re.getBody();
        return result;
    }

    @Override
    public Response modifyTrain(TrainType t, HttpHeaders headers) {
        logger.info("[function name:{}][t:{}, headers:{}]","modifyTrain",(t != null ? t.toString(): null), (headers != null ? headers.toString(): null));
        Response result;
        HttpEntity requestEntity = new HttpEntity(t, headers);
        String train_service_url = getServiceUrl("ts-train-service");
        String trains = train_service_url + "/api/v1/trainservice/trains";
        ResponseEntity<Response> re = restTemplate.exchange(
                trains,
                HttpMethod.PUT,
                requestEntity,
                Response.class);
        logger.info("[status code:{}, url:{}, type:{}, headers:{}]",re.getStatusCode(),
                trains,"PUT",headers);
        result = re.getBody();
        return result;
    }

    @Override
    public Response getAllConfigs(HttpHeaders headers) {
        logger.info("[function name:{}][headers:{}]","getAllConfigs",(headers != null ? headers.toString(): null));
        HttpEntity requestEntity = new HttpEntity(headers);
        String config_service_url = getServiceUrl("ts-config-service");
        String configs = config_service_url + "/api/v1/configservice/configs";
        ResponseEntity<Response> re = restTemplate.exchange(
                configs,
                HttpMethod.GET,
                requestEntity,
                Response.class);
        logger.info("[status code:{}, url:{}, type:{}, headers:{}]",re.getStatusCode(),
                configs,"GET",headers);

        return re.getBody();
    }

    @Override
    public Response addConfig(Config c, HttpHeaders headers) {
        logger.info("[function name:{}][c:{}, headers:{}]","addConfig",(c != null ? c.toString(): null), (headers != null ? headers.toString(): null));
        HttpEntity requestEntity = new HttpEntity(c, headers);
        String config_service_url = getServiceUrl("ts-config-service");
        String configs = config_service_url + "/api/v1/configservice/configs";
        ResponseEntity<Response> re = restTemplate.exchange(
                configs,
                HttpMethod.POST,
                requestEntity,
                Response.class);
        logger.info("[status code:{}, url:{}, type:{}, headers:{}]",re.getStatusCode(),
                configs,"POST",headers);
        return re.getBody();
    }

    @Override
    public Response deleteConfig(String name, HttpHeaders headers) {
        logger.info("[function name:{}][name:{}, headers:{}]","deleteConfig",name, (headers != null ? headers.toString(): null));
        HttpEntity requestEntity = new HttpEntity(headers);
        String config_service_url = getServiceUrl("ts-config-service");
        ResponseEntity<Response> re = restTemplate.exchange(
                config_service_url + "/api/v1/configservice/configs/" + name,
                HttpMethod.DELETE,
                requestEntity,
                Response.class);
        logger.info("[status code:{}, url:{}, type:{}, headers:{}]",re.getStatusCode(),
                config_service_url + "/api/v1/configservice/configs/" + name,"DELETE",headers);
        return re.getBody();
    }

    @Override
    public Response modifyConfig(Config c, HttpHeaders headers) {
        logger.info("[function name:{}][c:{}, headers:{}]","modifyConfig",(c != null ? c.toString(): null), (headers != null ? headers.toString(): null));
        HttpEntity requestEntity = new HttpEntity(c, headers);
        String config_service_url = getServiceUrl("ts-config-service");
        String configs = config_service_url + "/api/v1/configservice/configs";
        ResponseEntity<Response> re = restTemplate.exchange(
                configs,
                HttpMethod.PUT,
                requestEntity,
                Response.class);
        logger.info("[status code:{}, url:{}, type:{}, headers:{}]",re.getStatusCode(),
                configs,"PUT",headers);
        return re.getBody();
    }

    @Override
    public Response getAllPrices(HttpHeaders headers) {
        logger.info("[function name:{}][headers:{}]","getAllPrices",(headers != null ? headers.toString(): null));
        HttpEntity requestEntity = new HttpEntity(headers);
        String price_service_url = getServiceUrl("ts-price-service");
        String prices = price_service_url + "/api/v1/priceservice/prices";
        ResponseEntity<Response> re = restTemplate.exchange(
                prices,
                HttpMethod.GET,
                requestEntity,
                Response.class);
        logger.info("[status code:{}, url:{}, type:{}, headers:{}]",re.getStatusCode(),
                prices,"GET",headers);
        return re.getBody();
    }

    @Override
    public Response addPrice(PriceInfo pi, HttpHeaders headers) {
        logger.info("[function name:{}][pi:{}, headers:{}]","addPrice",(pi != null ? pi.toString(): null), (headers != null ? headers.toString(): null));
        HttpEntity requestEntity = new HttpEntity(pi, headers);
        String price_service_url = getServiceUrl("ts-price-service");
        String prices = price_service_url + "/api/v1/priceservice/prices";
        ResponseEntity<Response> re = restTemplate.exchange(
                prices,
                HttpMethod.POST,
                requestEntity,
                Response.class);
        logger.info("[status code:{}, url:{}, type:{}, headers:{}]",re.getStatusCode(),
                prices,"POST",headers);
        return re.getBody();

    }

    @Override
    public Response deletePrice(String pricesId, HttpHeaders headers) {
        logger.info("[function name:{}][pricesId:{}, headers:{}]","deletePrice",pricesId, (headers != null ? headers.toString(): null));
        HttpEntity requestEntity = new HttpEntity(headers);
        String price_service_url = getServiceUrl("ts-price-service");
        String path = price_service_url + "/api/v1/priceservice/prices/" + pricesId;
        ResponseEntity<Response> re = restTemplate.exchange(
                path,
                HttpMethod.DELETE,
                requestEntity,
                Response.class);
        logger.info("[status code:{}, url:{}, type:{}, headers:{}]",re.getStatusCode(),
                path,"DELETE",headers);
        logger.info("[status code:{}, url:{}, type:{}, headers:{}]",re.getStatusCode(),
                path,"DELETE",headers);

        return re.getBody();
    }

    @Override
    public Response modifyPrice(PriceInfo pi, HttpHeaders headers) {
        logger.info("[function name:{}][pi:{}, headers:{}]","modifyPrice",(pi != null ? pi.toString(): null), (headers != null ? headers.toString(): null));
        HttpEntity requestEntity = new HttpEntity(pi, headers);
        String price_service_url = getServiceUrl("ts-price-service");
        String prices = price_service_url + "/api/v1/priceservice/prices";
        ResponseEntity<Response> re = restTemplate.exchange(
                prices,
                HttpMethod.PUT,
                requestEntity,
                Response.class);
        logger.info("[status code:{}, url:{}, type:{}, headers:{}]",re.getStatusCode(),
                prices,"PUT",headers);
        return re.getBody();
    }
}
