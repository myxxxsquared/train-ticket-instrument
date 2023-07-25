package adminroute.service;

import edu.fudan.common.entity.Route;

















import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import edu.fudan.common.entity.RouteInfo;
import edu.fudan.common.util.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author fdse
 */
@Service
public class AdminRouteServiceImpl implements AdminRouteService { 
    private static final Logger logger = LoggerFactory.getLogger(AdminRouteServiceImpl.class);


















    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private DiscoveryClient discoveryClient;

    private String getServiceUrl(String serviceName) {
        logger.info("[function name:{}][serviceName:{}]","getServiceUrl",serviceName);
        return "http://" + serviceName;
    }

    @Override
    public Response getAllRoutes(HttpHeaders headers) {
        logger.info("[function name:{}][headers:{}]","getAllRoutes",(headers != null ? headers.toString(): null));

        HttpEntity requestEntity = new HttpEntity(null);
        String route_service_url = getServiceUrl("ts-route-service");
        ResponseEntity<Response> re = restTemplate.exchange(
                 route_service_url + "/api/v1/routeservice/routes",
                HttpMethod.GET,
                requestEntity,
                Response.class);
        logger.info("[status code:{}, url:{}, type:{}, headers:{}]",re.getStatusCode(),
                 route_service_url + "/api/v1/routeservice/routes","GET",headers);
        if (re.getStatusCode() != HttpStatus.ACCEPTED) {
            logger.error("[getAllRoutes][receive response][Get routes error][response code: {}]", re.getStatusCodeValue());
        }
        return re.getBody();

    }

    @Override
    public Response createAndModifyRoute(RouteInfo request, HttpHeaders headers) {
        logger.info("[function name:{}][request:{}, headers:{}]","createAndModifyRoute",(request != null ? request.toString(): null), (headers != null ? headers.toString(): null));
        // check stations
        String start = request.getStartStation();
        String end = request.getEndStation();
        List<String> stations = request.getStations();
        if(!stations.contains(start) || !stations.contains(end)){
            logger.error("[createAndModifyRoute][check stations][start or end not included in stationList][start: {}, end: {}]", start, end);
            return new Response(0, "start or end station not include in stationList.", null);
        }

        Response response = checkStationsExists(stations, headers);
        if(response.getStatus() ==0) {
            return response;
        }

        HttpEntity requestEntity = new HttpEntity(request, null);
        String route_service_url = getServiceUrl("ts-route-service");
        ResponseEntity<Response<Route>> re = restTemplate.exchange(
                route_service_url + "/api/v1/routeservice/routes",
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<Response<Route>>() {
                });
        logger.info("[status code:{}, url:{}, type:{}, headers:{}]",re.getStatusCode(),
                route_service_url + "/api/v1/routeservice/routes","POST",headers);
        if (re.getStatusCode() != HttpStatus.ACCEPTED) {
            logger.error("[createAndModifyRoute][receive response][Get status error][response code: {}]", re.getStatusCodeValue());
        }
        return re.getBody();
    }

    @Override
    public Response deleteRoute(String routeId, HttpHeaders headers) {
        logger.info("[function name:{}][routeId:{}, headers:{}]","deleteRoute",routeId, (headers != null ? headers.toString(): null));

        HttpEntity requestEntity = new HttpEntity(null);
        String route_service_url = getServiceUrl("ts-route-service");
        ResponseEntity<Response> re = restTemplate.exchange(
                route_service_url + "/api/v1/routeservice/routes/" + routeId,
                HttpMethod.DELETE,
                requestEntity,
                Response.class);
        logger.info("[status code:{}, url:{}, type:{}, headers:{}]",re.getStatusCode(),
                route_service_url + "/api/v1/routeservice/routes/" + routeId,"DELETE",headers);
        if (re.getStatusCode() != HttpStatus.ACCEPTED) {
            logger.error("[deleteRoute][response response][Delete error][response code: {}]", re.getStatusCodeValue());
        }
        return re.getBody();

    }

    public Response checkStationsExists(List<String> stationNames, HttpHeaders headers) {
        logger.info("[function name:{}][stationNames:{}, headers:{}]","checkStationsExists",(stationNames != null ? stationNames.toString(): null), (headers != null ? headers.toString(): null));
        HttpEntity requestEntity = new HttpEntity(stationNames, null);
        String station_service_url=getServiceUrl("ts-station-service");
        ResponseEntity<Response> re = restTemplate.exchange(
                station_service_url + "/api/v1/stationservice/stations/idlist",
                HttpMethod.POST,
                requestEntity,
                Response.class);
        logger.info("[status code:{}, url:{}, type:{}, headers:{}]",re.getStatusCode(),
                station_service_url + "/api/v1/stationservice/stations/idlist","POST",headers);
        Response<Map<String, String>> r = re.getBody();
        if(r.getStatus() == 0) {
            return r;
        }
        Map<String, String> stationMap = r.getData();
        List<String> notExists = new ArrayList<>();
        for(Map.Entry<String, String> s : stationMap.entrySet()){
            if(s.getValue() == null ){
                // station not exist
                notExists.add(s.getKey());
            }
        }
        if(notExists.size() > 0) {
            return new Response<>(0, "some station not exists", notExists);
        }
        return new Response<>(1, "check stations Exist succeed", null);
    }
}