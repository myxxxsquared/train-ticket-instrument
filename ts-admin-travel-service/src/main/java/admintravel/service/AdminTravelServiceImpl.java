package admintravel.service;

import edu.fudan.common.entity.AdminTrip;





import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import edu.fudan.common.entity.Route;
import edu.fudan.common.entity.TrainType;
import edu.fudan.common.entity.TravelInfo;
import edu.fudan.common.util.JsonUtils;
import edu.fudan.common.util.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author fdse
 */
@Service
public class AdminTravelServiceImpl implements AdminTravelService { 
    private static final Logger logger = LoggerFactory.getLogger(AdminTravelServiceImpl.class);







    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private DiscoveryClient discoveryClient;

    private String getServiceUrl(String serviceName) {
        return "http://" + serviceName;
    }

    @Override
    public Response getAllTravels(HttpHeaders headers) {
        logger.info("[function name:{}][headers:{}]","getAllTravels",(headers != null ? headers.toString(): null));
        Response<ArrayList<AdminTrip>> result;
        ArrayList<AdminTrip> trips = new ArrayList<>();
        HttpEntity requestEntity = new HttpEntity(headers);
        String travel_service_url = getServiceUrl("ts-travel-service");
        ResponseEntity<Response<ArrayList<AdminTrip>>> re = restTemplate.exchange(
                travel_service_url + "/api/v1/travelservice/admin_trip",
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<Response<ArrayList<AdminTrip>>>() {
                });
        logger.info("the client API's status code and url are: {} {} {}",re.getStatusCode(),
                travel_service_url + "/api/v1/travelservice/admin_trip","GET");
        result = re.getBody();

        if (result.getStatus() == 1) {
            ArrayList<AdminTrip> adminTrips = result.getData();
            trips.addAll(adminTrips);
        } else {
            AdminTravelServiceImpl.logger.error("[getAllTravels][receive response][Get Travel From ts-travel-service fail!]");
        }

        HttpEntity requestEntity2 = new HttpEntity(headers);
        String travel2_service_url = getServiceUrl("ts-travel2-service");
        ResponseEntity<Response<ArrayList<AdminTrip>>> re2 = restTemplate.exchange(
                travel2_service_url + "/api/v1/travel2service/admin_trip",
                HttpMethod.GET,
                requestEntity2,
                new ParameterizedTypeReference<Response<ArrayList<AdminTrip>>>() {
                });
        logger.info("the client API's status code and url are: {} {} {}",re2.getStatusCode(),
                travel2_service_url + "/api/v1/travel2service/admin_trip","GET");
        result = re2.getBody();

        if (result.getStatus() == 1) {
            ArrayList<AdminTrip> adminTrips = result.getData();
            trips.addAll(adminTrips);
        } else {
            AdminTravelServiceImpl.logger.error("[getAllTravels][receive response][Get Travel From ts-travel2-service fail!]");
        }
        result.setData(trips);

        return result;
    }

    @Override
    public Response addTravel(TravelInfo request, HttpHeaders headers) {
        logger.info("[function name:{}][request:{}, headers:{}]","addTravel",(request != null ? request.toString(): null), (headers != null ? headers.toString(): null));
        // check for travel info
        Response response = checkTravelInfo(request, headers);
        if(response.getStatus() == 0){
            return response;
        }

        Response result;
        String requestUrl;

        String travel_service_url = getServiceUrl("ts-travel-service");
        String travel2_service_url = getServiceUrl("ts-travel2-service");
        String tripId = request.getTripId();
        if (tripId.charAt(0) == 'G' || tripId.charAt(0) == 'D'){
            requestUrl = travel_service_url + "/api/v1/travelservice/trips";
        } else {
            requestUrl = travel2_service_url + "/api/v1/travel2service/trips";
        }
        HttpEntity requestEntity = new HttpEntity(request, headers);
        ResponseEntity<Response> re = restTemplate.exchange(
                requestUrl,
                HttpMethod.POST,
                requestEntity,
                Response.class);
        logger.info("the client API's status code and url are: {} {} {}",re.getStatusCode(),
                requestUrl,"POST");
        result = re.getBody();

        if (result.getStatus() == 1) {
            return new Response<>(1, "[Admin add new travel]", null);
        } else {
            AdminTravelServiceImpl.logger.error("[addTravel][receive response][Admin add new travel failed][trip id: {}]", request.getTripId());
            return new Response<>(0, "Admin add new travel failed", null);
        }
    }

    @Override
    public Response updateTravel(TravelInfo request, HttpHeaders headers) {
        logger.info("[function name:{}][request:{}, headers:{}]","updateTravel",(request != null ? request.toString(): null), (headers != null ? headers.toString(): null));
        // check for travel info
        Response response = checkTravelInfo(request, headers);
        if(response.getStatus() == 0){
            return response;
        }

        Response result;
        String requestUrl = "";
        String travel_service_url = getServiceUrl("ts-travel-service");
        String travel2_service_url = getServiceUrl("ts-travel2-service");
        String tripId = request.getTripId();
        if (tripId.charAt(0) == 'G' || tripId.charAt(0) == 'D'){
            requestUrl = travel_service_url + "/api/v1/travelservice/trips";
        } else {
            requestUrl = travel2_service_url + "/api/v1/travel2service/trips";
        }
        HttpEntity requestEntity = new HttpEntity(request, headers);
        ResponseEntity<Response> re = restTemplate.exchange(
                requestUrl,
                HttpMethod.PUT,
                requestEntity,
                Response.class);
        logger.info("the client API's status code and url are: {} {} {}",re.getStatusCode(),
                requestUrl,"PUT");

        result = re.getBody();
        if (result.getStatus() != 1)  {
            return new Response<>(0, "Admin update travel failed", null);
        }
        return result;
    }

    @Override
    public Response deleteTravel(String tripId, HttpHeaders headers) {
        logger.info("[function name:{}][tripId:{}, headers:{}]","deleteTravel",tripId, (headers != null ? headers.toString(): null));

        Response result;
        String requestUtl = "";
        String travel_service_url = getServiceUrl("ts-travel-service");
        String travel2_service_url = getServiceUrl("ts-travel2-service");
        if (tripId.charAt(0) == 'G' || tripId.charAt(0) == 'D') {
            requestUtl = travel_service_url + "/api/v1/travelservice/trips/" + tripId;
        } else {
            requestUtl = travel2_service_url + "/api/v1/travel2service/trips/" + tripId;
        }
        HttpEntity requestEntity = new HttpEntity(headers);
        ResponseEntity<Response> re = restTemplate.exchange(
                requestUtl,
                HttpMethod.DELETE,
                requestEntity,
                Response.class);
        logger.info("the client API's status code and url are: {} {} {}",re.getStatusCode(),
                requestUtl,"DELETE");

        result = re.getBody();
        if (result.getStatus() != 1) {
            AdminTravelServiceImpl.logger.error("[deleteTravel][receive response][Admin delete travel failed][trip id: {}]", tripId);
            return new Response<>(0, "Admin delete travel failed", null);
        }
        return result;
    }

    public Response checkTravelInfo(TravelInfo info, HttpHeaders headers) {
        logger.info("[function name:{}][info:{}, headers:{}]","checkTravelInfo",(info != null ? info.toString(): null), (headers != null ? headers.toString(): null));
        String start = info.getStartStationName();
        String end = info.getTerminalStationName();
        List<String> stations = new ArrayList<>();
        stations.add(start);
        stations.add(end);
        Response response = checkStationsExists(stations, headers);
        if(response.getStatus() ==0) {
            return response;
        }

        TrainType trainType = queryTrainTypeByName(info.getTrainTypeName(), headers);
        if (trainType == null) {
            AdminTravelServiceImpl.logger.warn(
                    "[queryForTravel][traintype doesn't exist][trainTypeName: {}]",
                    info.getTrainTypeName());
            response.setStatus(0);
            response.setMsg("Train type doesn't exist");
            return response;
        }
        String routeId = info.getRouteId();
        Route route = getRouteByRouteId(routeId, headers);
        if (route == null) {
            response.setStatus(0);
            response.setMsg("Route doesn't exist");
            return response;
        }

        // Check the route list for this train. Check that the required start and arrival stations are
        // in the list of stops that are not on the route, and check that the location of the start
        // station is before the stop
        if (!route.getStations().contains(start)
                || !route.getStations().contains(end)
                || (route.getStations().indexOf(start) >= route.getStations().indexOf(end))) {
            response.setStatus(0);
            response.setMsg("Station not correct in Route");
            return response;
        }
        response.setStatus(1);
        return response;
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
        logger.info("the client API's status code and url are: {} {} {}",re.getStatusCode(),
                station_service_url + "/api/v1/stationservice/stations/idlist","POST");
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

    public TrainType queryTrainTypeByName(String trainTypeName, HttpHeaders headers) {
        logger.info("[function name:{}][trainTypeName:{}, headers:{}]","queryTrainTypeByName",trainTypeName, (headers != null ? headers.toString(): null));
        HttpEntity requestEntity = new HttpEntity(null);
        String train_service_url=getServiceUrl("ts-train-service");
        ResponseEntity<Response> re = restTemplate.exchange(
                train_service_url + "/api/v1/trainservice/trains/byName/" + trainTypeName,
                HttpMethod.GET,
                requestEntity,
                Response.class);
        logger.info("the client API's status code and url are: {} {} {}",re.getStatusCode(),
                train_service_url + "/api/v1/trainservice/trains/byName/" + trainTypeName,"GET");
        Response  response = re.getBody();

        return JsonUtils.conveterObject(response.getData(), TrainType.class);
    }

    private Route getRouteByRouteId(String routeId, HttpHeaders headers) {
        HttpEntity requestEntity = new HttpEntity(null);
        String route_service_url=getServiceUrl("ts-route-service");
        ResponseEntity<Response> re = restTemplate.exchange(
                route_service_url + "/api/v1/routeservice/routes/" + routeId,
                HttpMethod.GET,
                requestEntity,
                Response.class);
        logger.info("the client API's status code and url are: {} {} {}",re.getStatusCode(),
                route_service_url + "/api/v1/routeservice/routes/" + routeId,"GET");
        Response result = re.getBody();
        if ( result.getStatus() == 0) {
            AdminTravelServiceImpl.logger.warn("[getRouteByRouteId][Get Route By Id Failed][Fail msg: {}]", result.getMsg());
            return null;
        } else {
            return JsonUtils.conveterObject(result.getData(), Route.class);
        }
    }

}
