package seat.service;

import edu.fudan.common.util.Response;



















import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import edu.fudan.common.entity.*;

import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * @author fdse
 */
@Service
public class SeatServiceImpl implements SeatService { 
    private static final Logger logger = LogManager.getLogger(SeatServiceImpl.class);



















    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private DiscoveryClient discoveryClient;

    private String getServiceUrl(String serviceName) {
        logger.info("[function name:{}][serviceName:{}]","getServiceUrl",serviceName);
        return "http://" + serviceName;
    }

    @Override
    public Response distributeSeat(Seat seatRequest, HttpHeaders headers) {
        logger.info("[function name:{}][seatRequest:{}, headers:{}]","distributeSeat",(seatRequest != null ? seatRequest.toString(): null), (headers != null ? headers.toString(): null));
        Response<Route> routeResult;

        LeftTicketInfo leftTicketInfo;
        TrainType trainTypeResult = null;
        ResponseEntity<Response<Route>> re;
        ResponseEntity<Response<TrainType>> re2;
        ResponseEntity<Response<LeftTicketInfo>> re3;

        //Distinguish G\D from other trains
        String trainNumber = seatRequest.getTrainNumber();

        if (trainNumber.startsWith("G") || trainNumber.startsWith("D")) {

            HttpEntity requestEntity = new HttpEntity(null);
            //Call the microservice to query for residual Ticket information: the set of the Ticket sold for the specified seat type
            requestEntity = new HttpEntity(seatRequest, null);
            String order_service_url=getServiceUrl("ts-order-service");
            re3 = restTemplate.exchange(
                    order_service_url + "/api/v1/orderservice/order/tickets",
                    HttpMethod.POST,
                    requestEntity,
                    new ParameterizedTypeReference<Response<LeftTicketInfo>>() {
                    });
        logger.info("[status code:{}, url:{}, type:{}, headers:{}]",re3.getStatusCode(),
                    order_service_url + "/api/v1/orderservice/order/tickets","POST",headers);
            leftTicketInfo = re3.getBody().getData();
        } else {
            //Call the microservice to query for residual Ticket information: the set of the Ticket sold for the specified seat type
            HttpEntity requestEntity = new HttpEntity(seatRequest, null);
            String order_other_service_url=getServiceUrl("ts-order-other-service");
            re3 = restTemplate.exchange(
                    order_other_service_url + "/api/v1/orderOtherService/orderOther/tickets",
                    HttpMethod.POST,
                    requestEntity,
                    new ParameterizedTypeReference<Response<LeftTicketInfo>>() {
                    });
        logger.info("[status code:{}, url:{}, type:{}, headers:{}]",re3.getStatusCode(),
                    order_other_service_url + "/api/v1/orderOtherService/orderOther/tickets","POST",headers);
            leftTicketInfo = re3.getBody().getData();
        }

        //Assign seats
        List<String> stationList = seatRequest.getStations();

        int seatTotalNum = seatRequest.getTotalNum();
        String startStation = seatRequest.getStartStation();
        Ticket ticket = new Ticket();
        ticket.setStartStation(startStation);
        ticket.setDestStation(seatRequest.getDestStation());

        //Assign new tickets
        Random rand = new Random();
        int range = seatTotalNum;
        int seat = rand.nextInt(range) + 1;

        if(leftTicketInfo != null) {
            Set<Ticket> soldTickets = leftTicketInfo.getSoldTickets();
            //Give priority to tickets already sold
            for (Ticket soldTicket : soldTickets) {
                String soldTicketDestStation = soldTicket.getDestStation();
                //Tickets can be allocated if the sold ticket's end station before the start station of the request
                if (stationList.indexOf(soldTicketDestStation) < stationList.indexOf(startStation)) {
                    ticket.setSeatNo(soldTicket.getSeatNo());
                    return new Response<>(1, "Use the previous distributed seat number!", ticket);
                }
            }
            while (isContained(soldTickets, seat)) {
                seat = rand.nextInt(range) + 1;
            }
        }
        ticket.setSeatNo(seat);
        return new Response<>(1, "Use a new seat number!", ticket);
    }

    private boolean isContained(Set<Ticket> soldTickets, int seat) {
        logger.info("[function name:{}][soldTickets:{}, seat:{}]","isContained",(soldTickets != null ? soldTickets.toString(): null), seat);
        //Check that the seat number has been used
        boolean result = false;
        for (Ticket soldTicket : soldTickets) {
            if (soldTicket.getSeatNo() == seat) {
                return true;
            }
        }
        return result;
    }

    @Override
    public Response getLeftTicketOfInterval(Seat seatRequest, HttpHeaders headers) {
        logger.info("[function name:{}][seatRequest:{}, headers:{}]","getLeftTicketOfInterval",(seatRequest != null ? seatRequest.toString(): null), (headers != null ? headers.toString(): null));
        int numOfLeftTicket = 0;
        LeftTicketInfo leftTicketInfo;
        ResponseEntity<Response<LeftTicketInfo>> re3;

        //Distinguish G\D from other trains
        String trainNumber = seatRequest.getTrainNumber();
        if (trainNumber.startsWith("G") || trainNumber.startsWith("D")) {

            //Call the micro service to query all the station information for the trains
            HttpEntity requestEntity = new HttpEntity(seatRequest, null);
            String order_service_url=getServiceUrl("ts-order-service");
            re3 = restTemplate.exchange(
                    order_service_url + "/api/v1/orderservice/order/tickets",
                    HttpMethod.POST,
                    requestEntity,
                    new ParameterizedTypeReference<Response<LeftTicketInfo>>() {
                    });
        logger.info("[status code:{}, url:{}, type:{}, headers:{}]",re3.getStatusCode(),
                    order_service_url + "/api/v1/orderservice/order/tickets","POST",headers);
            leftTicketInfo = re3.getBody().getData();
        } else {
            //Call the micro service to query all the station information for the trains
            HttpEntity requestEntity = new HttpEntity(null);
            //Call the micro service to query for residual Ticket information: the set of the Ticket sold for the specified seat type
            requestEntity = new HttpEntity(seatRequest, null);
            String order_other_service_url=getServiceUrl("ts-order-other-service");
            re3 = restTemplate.exchange(
                    order_other_service_url + "/api/v1/orderOtherService/orderOther/tickets",
                    HttpMethod.POST,
                    requestEntity,
                    new ParameterizedTypeReference<Response<LeftTicketInfo>>() {
                    });
        logger.info("[status code:{}, url:{}, type:{}, headers:{}]",re3.getStatusCode(),
                    order_other_service_url + "/api/v1/orderOtherService/orderOther/tickets","POST",headers);
            leftTicketInfo = re3.getBody().getData();
        }

        //Counting the seats remaining in certain sections
        List<String> stationList = seatRequest.getStations();
        int seatTotalNum = seatRequest.getTotalNum();
        int solidTicketSize = 0;
        if (leftTicketInfo != null) {
            String startStation = seatRequest.getStartStation();
            Set<Ticket> soldTickets = leftTicketInfo.getSoldTickets();
            solidTicketSize = soldTickets.size();
            //To find out if tickets already sold are available
            for (Ticket soldTicket : soldTickets) {
                String soldTicketDestStation = soldTicket.getDestStation();
                //Tickets can be allocated if the sold ticket's end station before the start station of the request
                if (stationList.indexOf(soldTicketDestStation) < stationList.indexOf(startStation)) {
                    numOfLeftTicket++;
                }
            }
        }
        //Count the unsold tickets

        double direstPart = getDirectProportion(headers);

        if (stationList.get(0).equals(seatRequest.getStartStation()) &&
                stationList.get(stationList.size() - 1).equals(seatRequest.getDestStation())) {
            //do nothing
        } else {
            direstPart = 1.0 - direstPart;
        }

        int unusedNum = (int) (seatTotalNum * direstPart) - solidTicketSize;
        numOfLeftTicket += unusedNum;

        return new Response<>(1, "Get Left Ticket of Internal Success", numOfLeftTicket);
    }

    private double getDirectProportion(HttpHeaders headers) {
        logger.info("[function name:{}][headers:{}]","getDirectProportion",(headers != null ? headers.toString(): null));

        String configName = "DirectTicketAllocationProportion";
        HttpEntity requestEntity = new HttpEntity(null);
        String config_service_url = getServiceUrl("ts-config-service");
        ResponseEntity<Response<Config>> re = restTemplate.exchange(
                config_service_url + "/api/v1/configservice/configs/" + configName,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<Response<Config>>() {
                });
        logger.info("[status code:{}, url:{}, type:{}, headers:{}]",re.getStatusCode(),
                config_service_url + "/api/v1/configservice/configs/" + configName,"GET",headers);
        Response<Config> configValue = re.getBody();
        return Double.parseDouble(configValue.getData().getValue());
    }
}