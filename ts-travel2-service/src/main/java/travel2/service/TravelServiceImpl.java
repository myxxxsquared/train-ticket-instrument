package travel2.service;

import com.fasterxml.jackson.core.type.TypeReference;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.fudan.common.entity.*;
import edu.fudan.common.util.JsonUtils;
import edu.fudan.common.util.Response;
import edu.fudan.common.util.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import travel2.entity.AdminTrip;
import travel2.entity.Trip;
import travel2.entity.Travel;
import travel2.entity.TripAllDetail;
import travel2.repository.TripRepository;

import javax.transaction.Transactional;
import java.util.*;

/**
 * @author fdse
 */
@Service
public class TravelServiceImpl implements TravelService { 
    private static final Logger logger = LoggerFactory.getLogger(TravelServiceImpl.class);





    @Autowired
    TripRepository repository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DiscoveryClient discoveryClient;

    private String getServiceUrl(String serviceName) {
        return "http://" + serviceName; }

    String success = "Success";
    String noCnontent = "No Content";

    @Override
    public Response getRouteByTripId(String tripId, HttpHeaders headers) {
        logger.info("[function name:{}][tripId:{}, headers:{}]","getRouteByTripId",tripId, (headers != null ? headers.toString(): null));
        TripId tripId1 = new TripId(tripId);

        Trip trip = repository.findByTripId(tripId1);
      logger.info("the trip is: {}", (trip != null ? trip : null));
      
      
      
      
        if (trip == null) {
            TravelServiceImpl.logger.error("[getRouteByTripId][Get Route By Trip ID Fail][Trip Not Found][TripId: {}]", tripId);
            return new Response<>(0, "\"[Get Route By Trip ID] Trip Not Found:\" + tripId", null);
        } else {
            Route route = getRouteByRouteId(trip.getRouteId(), headers);
            if (route == null) {
                TravelServiceImpl.logger.error("[getRouteByTripId][Get route by Trip id error][Route not found][RouteId: {}]",trip.getRouteId());
                return new Response<>(0, "\"[Get Route By Trip ID] Route Not Found:\" + trip.getRouteId()", null);
            } else {
                return new Response<>(1, "[Get Route By Trip ID] Success", route);
            }
        }
    }


    @Override
    public Response getTrainTypeByTripId(String tripId, HttpHeaders headers) {
        logger.info("[function name:{}][tripId:{}, headers:{}]","getTrainTypeByTripId",tripId, (headers != null ? headers.toString(): null));
        TripId tripId1 = new TripId(tripId);
        TrainType trainType = null;
        Trip trip = repository.findByTripId(tripId1);
      logger.info("the trip is: {}", (trip != null ? trip : null));
      
      
      
      
        if (trip != null) {
            trainType = getTrainTypeByName(trip.getTrainTypeName(), headers);
        }
        else {
            TravelServiceImpl.logger.error("[getTrainTypeByTripId[]Get Train Type by Trip id error][Trip not found][TripId: {}]",tripId);
        }
        if (trainType != null) {
            return new Response<>(1, "Success query Train by trip id", trainType);
        } else {
            TravelServiceImpl.logger.error("[getTrainTypeByTripId][Get Train Type by Trip id error][Train Type not found][TripId: {}]",tripId);
            return new Response<>(0, noCnontent, null);
        }
    }

    @Override
    public Response getTripByRoute(ArrayList<String> routeIds, HttpHeaders headers) {
        logger.info("[function name:{}][routeIds:{}, headers:{}]","getTripByRoute",(routeIds != null ? routeIds.toString(): null), (headers != null ? headers.toString(): null));
        ArrayList<ArrayList<Trip>> tripList = new ArrayList<>();
        for (String routeId : routeIds) {
            ArrayList<Trip> tempTripList = repository.findByRouteId(routeId);
      logger.info("the tempTripList is: {}", (tempTripList != null ? tempTripList : null));
      
      
      
      
            if (tempTripList == null) {
                tempTripList = new ArrayList<>();
            }
            tripList.add(tempTripList);
        }
        if (!tripList.isEmpty()) {
            return new Response<>(1, success, tripList);
        } else {
            TravelServiceImpl.logger.warn("[getTripByRoute][Get Trips by Route ids warn][Trips not found][RouteIdNumber: {}]",routeIds.size());
            return new Response<>(0, noCnontent, null);
        }
    }

    @Override
    public Response create(edu.fudan.common.entity.TravelInfo info, HttpHeaders headers) {
        logger.info("[function name:{}][info:{}, headers:{}]","create",(info != null ? info.toString(): null), (headers != null ? headers.toString(): null));
        TripId ti = new TripId(info.getTripId());
        if (repository.findByTripId(ti) == null) {
        logger.info("the Trip is: {}", (repository.findByTripId(ti) != null ? repository.findByTripId(ti) : null));
            Trip trip = new Trip(ti, info.getTrainTypeName(), info.getStartStationName(),
                    info.getStationsName(), info.getTerminalStationName(), info.getStartTime(), info.getEndTime());
            trip.setRouteId(info.getRouteId());
            repository.save(trip);
            return new Response<>(1, "Create trip info:" + ti.toString() + ".", null);
        } else {
            TravelServiceImpl.logger.error("[getTripByRoute][Create trip error][Trip already exists][TripId: {}]",info.getTripId());
            return new Response<>(1, "Trip " + info.getTripId() + " already exists", null);
        }
    }

    @Override
    public Response retrieve(String tripId, HttpHeaders headers) {
        logger.info("[function name:{}][tripId:{}, headers:{}]","retrieve",tripId, (headers != null ? headers.toString(): null));
        TripId ti = new TripId(tripId);
        Trip trip = repository.findByTripId(ti);
      logger.info("the trip is: {}", (trip != null ? trip : null));
      
      
      
      
        if (trip != null) {
            return new Response<>(1, "Search Trip Success by Trip Id " + tripId, trip);
        } else {
            TravelServiceImpl.logger.error("[retrieve][Retrieve trip error][Trip not found][TripId: {}]",tripId);
            return new Response<>(0, "No Content according to tripId" + tripId, null);
        }
    }

    @Override
    public Response update(edu.fudan.common.entity.TravelInfo info, HttpHeaders headers) {
        logger.info("[function name:{}][info:{}, headers:{}]","update",(info != null ? info.toString(): null), (headers != null ? headers.toString(): null));
        TripId ti = new TripId(info.getTripId());
        Trip t = repository.findByTripId(ti);
        if (t != null) {
            t.setStartStationName(info.getTrainTypeName());
            t.setStartStationName( info.getStartStationName());
            t.setStationsName(info.getStationsName());
            t.setTerminalStationName(info.getTerminalStationName());
            t.setStartTime(info.getStartTime());
            t.setEndTime(info.getEndTime());
            t.setRouteId(info.getRouteId());
      
      logger.info("the t is: {}", (t != null ? t : null));
      repository.save(t);
            return new Response<>(1, "Update trip info:" + ti.toString(), t);
        } else {
            TravelServiceImpl.logger.error("[update][Update trip error][Trip not found][TripId: {}]",info.getTripId());
            return new Response<>(1, "Trip" + info.getTripId() + "doesn 't exists", null);
        }
    }

    @Override
    @Transactional
    public Response delete(String tripId, HttpHeaders headers) {
        logger.info("[function name:{}][tripId:{}, headers:{}]","delete",tripId, (headers != null ? headers.toString(): null));
        TripId ti = new TripId(tripId);
        if (repository.findByTripId(ti) != null) {
        logger.info("the Trip is: {}", (repository.findByTripId(ti) != null ? repository.findByTripId(ti) : null));
            repository.deleteByTripId(ti);
            return new Response<>(1, "Delete trip:" + tripId + ".", tripId);
        } else {
            TravelServiceImpl.logger.error("[delete][Delete trip error][Trip not found][TripId: {}]",tripId);
            return new Response<>(0, "Trip " + tripId + " doesn't exist.", null);
        }
    }

    @Override
    public Response queryByBatch(TripInfo info, HttpHeaders headers) {
        logger.info("[function name:{}][info:{}, headers:{}]","queryByBatch",(info != null ? info.toString(): null), (headers != null ? headers.toString(): null));

        //Gets the start and arrival stations of the train number to query. The originating and arriving stations received here are both station names, so two requests need to be sent to convert to station ids
        String startPlaceName = info.getStartPlace();
        String endPlaceName = info.getEndPlace();

        //This is the final result
        List<TripResponse> list = new ArrayList<>();

        //Check all train info
        List<Trip> allTripList = repository.findAll();
      logger.info("the allTripList is: {}", (allTripList != null ? allTripList : null));
      
      
      
      
        list = getTicketsByBatch(allTripList, startPlaceName, endPlaceName, info.getDepartureTime(), headers);
        return new Response<>(1, success, list);
    }

    @Override
    public Response query(TripInfo info, HttpHeaders headers) {
        logger.info("[function name:{}][info:{}, headers:{}]","query",(info != null ? info.toString(): null), (headers != null ? headers.toString(): null));

        //Gets the start and arrival stations of the train number to query. The originating and arriving stations received here are both station names, so two requests need to be sent to convert to station ids
        String StartPlaceName = info.getStartPlace();
        String endPlaceName = info.getEndPlace();

        //This is the final result
        ArrayList<TripResponse> list = new ArrayList<>();

        //Check all train info
        ArrayList<Trip> allTripList = repository.findAll();
      logger.info("the allTripList is: {}", (allTripList != null ? allTripList : null));
      
      logger.info("the allTripList is: {}", (allTripList != null ? allTripList : null));
      
      
      
      
        if(allTripList != null){
            for (Trip tempTrip : allTripList) {
                //Get the detailed route list of this train
                TripResponse response = getTickets(tempTrip, null, StartPlaceName, endPlaceName, info.getDepartureTime(), headers);
                if (response == null) {
                    TravelServiceImpl.logger.warn("[query][Query trip error][Tickets not found][start: {},end: {},time: {}]", StartPlaceName, endPlaceName, info.getDepartureTime());
                }else{
                    list.add(response);
                }
            }
        }
        return new Response<>(1, "Success Query", list);
    }

    @Override
    public Response getTripAllDetailInfo(TripAllDetailInfo gtdi, HttpHeaders headers) {
        logger.info("[function name:{}][gtdi:{}, headers:{}]","getTripAllDetailInfo",(gtdi != null ? gtdi.toString(): null), (headers != null ? headers.toString(): null));
        TripAllDetail gtdr = new TripAllDetail();
        TravelServiceImpl.logger.debug("[getTripAllDetailInfo][gtdi info: {}]", gtdi.toString());
        Trip trip = repository.findByTripId(new TripId(gtdi.getTripId()));
      logger.info("the trip is: {}", (trip != null ? trip : null));
      
      
      
      
        if (trip == null) {
            gtdr.setTripResponse(null);
            gtdr.setTrip(null);
            TravelServiceImpl.logger.error("[getTripAllDetailInfo][Get trip detail error][Trip not found][TripId: {}]",gtdi.getTripId());
            return new Response<>(0, "Trip not found", gtdr);
        } else {
            String endPlaceName = gtdi.getTo();
            String StartPlaceName = gtdi.getFrom();
            TripResponse tripResponse = getTickets(trip, null, gtdi.getFrom(), gtdi.getTo(), gtdi.getTravelDate(), headers);
            if (tripResponse == null) {
                gtdr.setTrip(null);
                gtdr.setTripResponse(null);
                TravelServiceImpl.logger.warn("[getTripAllDetailInfo][Query trip error][Tickets not found][start: {},end: {}]", gtdi.getTo(), gtdi.getFrom());
                return new Response<>(0, "getTickets failed", gtdr);
            } else {
                gtdr.setTripResponse(tripResponse);
                gtdr.setTrip(repository.findByTripId(new TripId(gtdi.getTripId())));
            }
        }
        return new Response<>(1, success, gtdr);
    }

    private List<TripResponse> getTicketsByBatch(List<Trip> trips, String startPlaceName, String endPlaceName, String departureTime, HttpHeaders headers) {
        List<TripResponse> responses = new ArrayList<>();
        //Determine if the date checked is the same day and after
        if (!afterToday(departureTime)) {
            return responses;
        }

        List<Travel> infos = new ArrayList<>();
        Map<String, Trip> tripMap = new HashMap<>();
        for(Trip trip: trips){
            Travel query = new Travel();
            query.setTrip(trip);
            query.setStartPlace(startPlaceName);
            query.setEndPlace(endPlaceName);
            query.setDepartureTime(departureTime);

            infos.add(query);
            tripMap.put(trip.getTripId().toString(), trip);
        }

        HttpEntity requestEntity = new HttpEntity(infos, null);
        String basic_service_url = getServiceUrl("ts-basic-service");
        ResponseEntity<Response> re = restTemplate.exchange(
                basic_service_url + "/api/v1/basicservice/basic/travels",
                HttpMethod.POST,
                requestEntity,
                Response.class);
        logger.info("the client API's status code and url are: {} {} {}",re.getStatusCode(),
                basic_service_url + "/api/v1/basicservice/basic/travels","POST");

        Response r = re.getBody();
        if(r.getStatus() == 0){
            return responses;
        }
        Map<String, TravelResult> trMap;
        ObjectMapper mapper = new ObjectMapper();
        try{
            trMap = mapper.readValue(JsonUtils.object2Json(r.getData()), new TypeReference<Map<String, TravelResult>>(){});
        }catch(Exception e) {
            TravelServiceImpl.logger.warn("[getTicketsByBatch][Ts-basic-service convert data failed][Fail msg: {}]", e.getMessage());
            return responses;
        }

        for(Map.Entry<String, TravelResult> trEntry: trMap.entrySet()){
            //Set the returned ticket information
            String tripNumber = trEntry.getKey();
            TravelResult tr = trEntry.getValue();
            Trip trip = tripMap.get(tripNumber);

            TripResponse response = setResponse(trip, tr, startPlaceName, endPlaceName, departureTime, headers);
            responses.add(response);
        }
        return responses;
    }


    private TripResponse getTickets(Trip trip, Route route1, String startPlaceName, String endPlaceName, String departureTime, HttpHeaders headers) {

        //Determine if the date checked is the same day and after
        if (!afterToday(departureTime)) {
            return null;
        }

        Travel query = new Travel();
        query.setTrip(trip);
        query.setStartPlace(startPlaceName);
        query.setEndPlace(endPlaceName);
        query.setDepartureTime(departureTime);

        HttpEntity requestEntity = new HttpEntity(query, null);
        String basic_service_url = getServiceUrl("ts-basic-service");
        ResponseEntity<Response<TravelResult>> re = restTemplate.exchange(
                basic_service_url + "/api/v1/basicservice/basic/travel",
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<Response<edu.fudan.common.entity.TravelResult>>() {
                });
        logger.info("the client API's status code and url are: {} {} {}",re.getStatusCode(),
                basic_service_url + "/api/v1/basicservice/basic/travel","POST");
        Response r = re.getBody();
        if(r.getStatus() == 0){
            return null;
        }
        TravelResult resultForTravel =  re.getBody().getData();

        //Set the returned ticket information
        return setResponse(trip, resultForTravel, startPlaceName, endPlaceName, departureTime, headers);
    }

    private TripResponse setResponse(Trip trip, TravelResult tr, String startPlaceName, String endPlaceName, String departureTime, HttpHeaders headers){
        //Set the returned ticket information
        TripResponse response = new TripResponse();
        response.setConfortClass(50);
        response.setEconomyClass(50);

        Route route = tr.getRoute();
        List<String> stationList = route.getStations();

        int firstClassTotalNum = tr.getTrainType().getConfortClass();
        int secondClassTotalNum = tr.getTrainType().getEconomyClass();

        int first = getRestTicketNumber(departureTime, trip.getTripId().toString(),
                startPlaceName, endPlaceName, SeatClass.FIRSTCLASS.getCode(), firstClassTotalNum, stationList, headers);

        int second = getRestTicketNumber(departureTime, trip.getTripId().toString(),
                startPlaceName, endPlaceName, SeatClass.SECONDCLASS.getCode(), secondClassTotalNum, stationList, headers);
        response.setConfortClass(first);
        response.setEconomyClass(second);

        response.setStartStation(startPlaceName);
        response.setTerminalStation(endPlaceName);

        //Calculate the distance from the starting point
        int indexStart = route.getStations().indexOf(startPlaceName);
        int indexEnd = route.getStations().indexOf(endPlaceName);
        int distanceStart = route.getDistances().get(indexStart) - route.getDistances().get(0);
        int distanceEnd = route.getDistances().get(indexEnd) - route.getDistances().get(0);
        TrainType trainType = tr.getTrainType();
        //Train running time is calculated according to the average running speed of the train
        int minutesStart = 60 * distanceStart / trainType.getAverageSpeed();
        int minutesEnd = 60 * distanceEnd / trainType.getAverageSpeed();

        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTime(StringUtils.String2Date(trip.getStartTime()));
        calendarStart.add(Calendar.MINUTE, minutesStart);
        response.setStartTime(StringUtils.Date2String(calendarStart.getTime()));

        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.setTime(StringUtils.String2Date(trip.getStartTime()));
        calendarEnd.add(Calendar.MINUTE, minutesEnd);
        response.setEndTime(StringUtils.Date2String(calendarEnd.getTime()));

        response.setTripId(trip.getTripId());
        response.setTrainTypeName(trip.getTrainTypeName());
        response.setPriceForConfortClass(tr.getPrices().get("confortClass"));
        response.setPriceForEconomyClass(tr.getPrices().get("economyClass"));

        return response;
    }

    @Override
    public Response queryAll(HttpHeaders headers) {
        logger.info("[function name:{}][headers:{}]","queryAll",(headers != null ? headers.toString(): null));
        List<Trip> tripList = repository.findAll();
      logger.info("the tripList is: {}", (tripList != null ? tripList : null));
      
      
      
      
        if (tripList != null && !tripList.isEmpty()) {
            return new Response<>(1, success, tripList);
        }
        TravelServiceImpl.logger.warn("[queryAll][Query all trips warn][{}]","No Content");
        return new Response<>(0, noCnontent, null);
    }

    private static boolean afterToday(String date) {
        Calendar calDateA = Calendar.getInstance();
        Date today = new Date();
        calDateA.setTime(today);

        Calendar calDateB = Calendar.getInstance();
        calDateB.setTime(StringUtils.String2Date(date));

        if (calDateA.get(Calendar.YEAR) > calDateB.get(Calendar.YEAR)) {
            return false;
        } else if (calDateA.get(Calendar.YEAR) == calDateB.get(Calendar.YEAR)) {
            if (calDateA.get(Calendar.MONTH) > calDateB.get(Calendar.MONTH)) {
                return false;
            } else if (calDateA.get(Calendar.MONTH) == calDateB.get(Calendar.MONTH)) {
                return calDateA.get(Calendar.DAY_OF_MONTH) <= calDateB.get(Calendar.DAY_OF_MONTH);
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    private TrainType getTrainTypeByName(String trainTypeName, HttpHeaders headers) {
        HttpEntity requestEntity = new HttpEntity(null);
        String train_service_url = getServiceUrl("ts-train-service");
        ResponseEntity<Response<TrainType>> re = restTemplate.exchange(
                train_service_url + "/api/v1/trainservice/trains/byName/" + trainTypeName,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<Response<TrainType>>() {
                });
        logger.info("the client API's status code and url are: {} {} {}",re.getStatusCode(),
                train_service_url + "/api/v1/trainservice/trains/byName/" + trainTypeName,"GET");

        return re.getBody().getData();
    }

    private Route getRouteByRouteId(String routeId, HttpHeaders headers) {
        TravelServiceImpl.logger.debug("[getRouteByRouteId][Get Route By Id][Route ID：{}]", routeId);
        HttpEntity requestEntity = new HttpEntity(null);
        String route_service_url = getServiceUrl("ts-route-service");
        ResponseEntity<Response> re = restTemplate.exchange(
                route_service_url + "/api/v1/routeservice/routes/" + routeId,
                HttpMethod.GET,
                requestEntity,
                Response.class);
        logger.info("the client API's status code and url are: {} {} {}",re.getStatusCode(),
                route_service_url + "/api/v1/routeservice/routes/" + routeId,"GET");
        Response result = re.getBody();

        if (result.getStatus() == 0 ) {
            TravelServiceImpl.logger.error("[getRouteByRouteId][Get Route By Id Fail][Route not found][RouteId: {}]", routeId);
            return null;
        } else {
            return JsonUtils.conveterObject(result.getData(), Route.class);
        }
    }

    private int getRestTicketNumber(String travelDate, String trainNumber, String startStationName, String endStationName, int seatType, int totalNum, List<String> stationList, HttpHeaders headers) {
        Seat seatRequest = new Seat();

        seatRequest.setDestStation(endStationName);
        seatRequest.setStartStation(startStationName);
        seatRequest.setTrainNumber(trainNumber);
        seatRequest.setTravelDate(travelDate);
        seatRequest.setSeatType(seatType);
        seatRequest.setTotalNum(totalNum);
        seatRequest.setStations(stationList);

        HttpEntity requestEntity = new HttpEntity(seatRequest, null);
        String seat_service_url = getServiceUrl("ts-seat-service");
        ResponseEntity<Response<Integer>> re = restTemplate.exchange(
                seat_service_url + "/api/v1/seatservice/seats/left_tickets",
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<Response<Integer>>() {
                });
        logger.info("the client API's status code and url are: {} {} {}",re.getStatusCode(),
                seat_service_url + "/api/v1/seatservice/seats/left_tickets","POST");

        return re.getBody().getData();
    }

    @Override
    public Response adminQueryAll(HttpHeaders headers) {
        logger.info("[function name:{}][headers:{}]","adminQueryAll",(headers != null ? headers.toString(): null));
        List<Trip> trips = repository.findAll();
        ArrayList<AdminTrip> adminTrips = new ArrayList<>();
        if(trips != null){
            for (Trip trip : trips) {
                AdminTrip adminTrip = new AdminTrip();
                adminTrip.setRoute(getRouteByRouteId(trip.getRouteId(), headers));
                adminTrip.setTrainType(getTrainTypeByName(trip.getTrainTypeName(), headers));
                adminTrip.setTrip(trip);
                adminTrips.add(adminTrip);
            }
        }

        if (!adminTrips.isEmpty()) {
            return new Response<>(1, "Travel Service Admin Query All Travel Success", adminTrips);
        } else {
            TravelServiceImpl.logger.warn("[adminQueryAll][Admin query all trips warn][{}]","No Content");
            return new Response<>(0, noCnontent, null);
        }
    }

}

