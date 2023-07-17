package route.service;

import edu.fudan.common.util.Response;










import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import route.entity.Route;
import route.entity.RouteInfo;
import route.repository.RouteRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author fdse
 */
@Service
public class RouteServiceImpl implements RouteService { 
    private static final Logger logger = LoggerFactory.getLogger(RouteServiceImpl.class);











    @Autowired
    private RouteRepository routeRepository;

    String success = "Success";

    @Override
    public Response createAndModify(RouteInfo info, HttpHeaders headers) {
        logger.info("[function name:{}][info:{}, headers:{}]","createAndModify",(info != null ? info.toString(): null), (headers != null ? headers.toString(): null));

        String[] stations = info.getStationList().split(",");
        String[] distances = info.getDistanceList().split(",");
        List<String> stationList = new ArrayList<>();
        List<Integer> distanceList = new ArrayList<>();
        if (stations.length != distances.length) {
            RouteServiceImpl.logger.error("[createAndModify][Create and modify error][Station number not equal to distance number][RouteId: {}]",info.getId());
            return new Response<>(0, "Station Number Not Equal To Distance Number", null);
        }
        for (int i = 0; i < stations.length; i++) {
            stationList.add(stations[i]);
            distanceList.add(Integer.parseInt(distances[i]));
        }
        int maxIdArrayLen = 32;
        Route route = new Route();
        if (info.getId() == null || info.getId().length() < maxIdArrayLen) {
            route.setId(UUID.randomUUID().toString());
        }else{
            Optional<Route> routeOld = routeRepository.findById(info.getId());
      logger.info("[routeOld:{}]", (routeOld != null ? routeOld : null));
      
      
      
      
      
      
      
      
      
      
            if(routeOld.isPresent()) {
                route = routeOld.get();
            } else {
                route.setId(info.getId());
            }
        }
        route.setStartStation(info.getStartStation());
        route.setEndStation(info.getEndStation());
        route.setStations(stationList);
        route.setDistances(distanceList);
        routeRepository.save(route);
        return new Response<>(1, "Save and Modify success", route);
    }

    @Override
    @Transactional
    public Response deleteRoute(String routeId, HttpHeaders headers) {
        logger.info("[function name:{}][routeId:{}, headers:{}]","deleteRoute",routeId, (headers != null ? headers.toString(): null));
        routeRepository.removeRouteById(routeId);
        Optional<Route> route = routeRepository.findById(routeId);
      logger.info("[route:{}]", (route != null ? route : null));
      
      
      
      
      
      
      
      
      
      
        if (!route.isPresent()) {
            return new Response<>(1, "Delete Success", routeId);
        } else {
            RouteServiceImpl.logger.error("[deleteRoute][Delete error][Route not found][RouteId: {}]",routeId);
            return new Response<>(0, "Delete failed, Reason unKnown with this routeId", routeId);
        }
    }

    @Override
    public Response getRouteById(String routeId, HttpHeaders headers) {
        logger.info("[function name:{}][routeId:{}, headers:{}]","getRouteById",routeId, (headers != null ? headers.toString(): null));
        Optional<Route> route = routeRepository.findById(routeId);
      logger.info("[route:{}]", (route != null ? route : null));
      
      
      
      
      
      
            
        if (!route.isPresent()) {
            RouteServiceImpl.logger.error("[getRouteById][Find route error][Route not found][RouteId: {}]",routeId);
            return new Response<>(0, "No content with the routeId", null);
        } else {
            return new Response<>(1, success, route);
        }

    }

    @Override
    public Response getRouteByIds(List<String> routeIds, HttpHeaders headers) {
        logger.info("[function name:{}][routeIds:{}, headers:{}]","getRouteByIds",(routeIds != null ? routeIds.toString(): null), (headers != null ? headers.toString(): null));
        List<Route> routes = routeRepository.findByIds(routeIds);
      logger.info("[routes:{}]", (routes != null ? routes : null));
      
      
      
      
      
      
      
      
      
      
        if (routes == null || routes.isEmpty()) {
            RouteServiceImpl.logger.error("[getRouteById][Find route error][Route not found][RouteIds: {}]",routeIds);
            return new Response<>(0, "No content with the routeIds", null);
        } else {
            return new Response<>(1, success, routes);
        }
    }

    @Override
    public Response getRouteByStartAndEnd(String startId, String terminalId, HttpHeaders headers) {
        logger.info("[function name:{}][startId:{}, terminalId:{}, headers:{}]","getRouteByStartAndEnd",startId, terminalId, (headers != null ? headers.toString(): null));
        ArrayList<Route> routes = routeRepository.findAll();
      logger.info("[routes:{}]", (routes != null ? routes : null));
      
      logger.info("[routes:{}]", (routes != null ? routes : null));
      
      
      
      
      
      
      
      
      
      
        List<Route> resultList = new ArrayList<>();
        for (Route route : routes) {
            if (route.getStations().contains(startId) &&
                    route.getStations().contains(terminalId) &&
                    route.getStations().indexOf(startId) < route.getStations().indexOf(terminalId)) {
                resultList.add(route);
            }
        }
        if (!resultList.isEmpty()) {
            return new Response<>(1, success, resultList);
        } else {
            RouteServiceImpl.logger.warn("[getRouteByStartAndEnd][Find by start and terminal warn][Routes not found][startId: {},terminalId: {}]",startId,terminalId);
            return new Response<>(0, "No routes with the startId and terminalId", null);
        }
    }

    @Override
    public Response getAllRoutes(HttpHeaders headers) {
        logger.info("[function name:{}][headers:{}]","getAllRoutes",(headers != null ? headers.toString(): null));
        ArrayList<Route> routes = routeRepository.findAll();
      logger.info("[routes:{}]", (routes != null ? routes : null));
      
      logger.info("[routes:{}]", (routes != null ? routes : null));
      
      
      
      
      
      
      
      
      
      
        if (routes != null && !routes.isEmpty()) {
            return new Response<>(1, success, routes);
        } else {
            RouteServiceImpl.logger.warn("[getAllRoutes][Find all routes warn][{}]","No Content");
            return new Response<>(0, "No Content", null);
        }
    }

}