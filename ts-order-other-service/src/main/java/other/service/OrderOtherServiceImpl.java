package other.service;

import edu.fudan.common.entity.*;











import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import edu.fudan.common.util.Response;
import edu.fudan.common.util.StringUtils;

import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import other.entity.*;
import other.entity.Order;
import other.entity.OrderAlterInfo;
import other.repository.OrderOtherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author fdse
 */
@Service
public class OrderOtherServiceImpl implements OrderOtherService { 
    private static final Logger logger = LoggerFactory.getLogger(OrderOtherServiceImpl.class);













    @Autowired
    private OrderOtherRepository orderOtherRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DiscoveryClient discoveryClient;

    private String getServiceUrl(String serviceName) {
        logger.info("[function name:{}][serviceName:{}]","getServiceUrl",serviceName);
        return "http://" + serviceName;
    }

//    @Value("${station-service.url}")
//    String station_service_url;

    String success = "Success";
    String orderNotFound = "Order Not Found";

    @Override
    public Response getSoldTickets(Seat seatRequest, HttpHeaders headers) {
        logger.info("[function name:{}][seatRequest:{}, headers:{}]","getSoldTickets",(seatRequest != null ? seatRequest.toString(): null), (headers != null ? headers.toString(): null));
        ArrayList<Order> list = orderOtherRepository.findByTravelDateAndTrainNumber(seatRequest.getTravelDate(),
                seatRequest.getTrainNumber());
      logger.info("[list:{}]", (list != null ? list : null));
      
      
      
      
      
      
      
      
      
      
      
      
        if (list != null && !list.isEmpty()) {
            Set ticketSet = new HashSet();
            for (Order tempOrder : list) {
                Ticket ticket = new Ticket();
                ticket.setSeatNo(Integer.parseInt(tempOrder.getSeatNumber()));
                ticket.setStartStation(tempOrder.getFrom());
                ticket.setDestStation(tempOrder.getTo());
                ticketSet.add(ticket);
            }

            LeftTicketInfo leftTicketInfo = new LeftTicketInfo();
            leftTicketInfo.setSoldTickets(ticketSet);

            return new Response<>(1, success, leftTicketInfo);
        } else {

            OrderOtherServiceImpl.logger.warn("[getSoldTickets][Seat][No content][seat from date: {}, train number: {}",seatRequest.getTravelDate(),seatRequest.getTrainNumber());
            return new Response<>(0, "Seat is Null.", null);
        }
    }

    @Override
    public Response findOrderById(String id, HttpHeaders headers) {
        logger.info("[function name:{}][id:{}, headers:{}]","findOrderById",id, (headers != null ? headers.toString(): null));
        Optional<Order> op = orderOtherRepository.findById(id);
      logger.info("[op:{}]", (op != null ? op : null));
      
      
      
      
      
      
      
      
      
      
      
      
        if (!op.isPresent()) {
            OrderOtherServiceImpl.logger.warn("[findOrderById][Find Order By Id Fail][No content][id: {}]",id);
            return new Response<>(0, "No Content by this id", null);
        } else {
            Order order = op.get();
            return new Response<>(1, success, order);
        }
    }

    @Override
    public Response create(Order order, HttpHeaders headers) {
        logger.info("[function name:{}][order:{}, headers:{}]","create",(order != null ? order.toString(): null), (headers != null ? headers.toString(): null));
        ArrayList<Order> accountOrders = orderOtherRepository.findByAccountId(order.getAccountId());
      logger.info("[accountOrders:{}]", (accountOrders != null ? accountOrders : null));
      
      
      
      
      
      
      
      
      
      
      
      
        if (accountOrders.contains(order)) {
            OrderOtherServiceImpl.logger.error("[create][Order Create Fail][Order already exists][OrderId: {}]", order.getId());
            return new Response<>(0, "Order already exist", order);
        } else {
            order.setId(UUID.randomUUID().toString());
            order=orderOtherRepository.save(order);
            return new Response<>(1, success, order);
        }
    }

    @Override
    public void initOrder(Order order, HttpHeaders headers) {
        logger.info("[function name:{}][order:{}, headers:{}]","initOrder",(order != null ? order.toString(): null), (headers != null ? headers.toString(): null));
        Optional<Order> op = orderOtherRepository.findById(order.getId());
      logger.info("[op:{}]", (op != null ? op : null));
      
      
      
      
      
      
      
      
      
      
      
      
        if (!op.isPresent()) {
            orderOtherRepository.save(order);
        } else {
            Order orderTemp = op.get();
            OrderOtherServiceImpl.logger.error("[initOrder][Init Order Fail][Order Already Exists][OrderId: {}]", order.getId());
        }
    }


    @Override
    public Response alterOrder(OrderAlterInfo oai, HttpHeaders headers) {
        logger.info("[function name:{}][oai:{}, headers:{}]","alterOrder",(oai != null ? oai.toString(): null), (headers != null ? headers.toString(): null));

        String oldOrderId = oai.getPreviousOrderId();

        if (!orderOtherRepository.findById(oldOrderId).isPresent()) {
        logger.info("[Optional<Order>:{}]", (orderOtherRepository.findById(oldOrderId) != null ? orderOtherRepository.findById(oldOrderId) : null));
            OrderOtherServiceImpl.logger.error("[alterOrder][Alter Order Fail][Order do not exist][OrderId: {}]", oldOrderId);
            return new Response<>(0, "Old Order Does Not Exists", null);
        }
        Order oldOrder = orderOtherRepository.findById(oldOrderId).get();
        oldOrder.setStatus(OrderStatus.CANCEL.getCode());
      
      logger.info("[oldOrder:{}]", (oldOrder != null ? oldOrder : null));
      saveChanges(oldOrder, headers);
        Order newOrder = oai.getNewOrderInfo();
        newOrder.setId(UUID.randomUUID().toString());
        Response cor = create(oai.getNewOrderInfo(), headers);
        if (cor.getStatus() == 1) {
            return new Response<>(1, "Alter Order Success", newOrder);
        } else {
            OrderOtherServiceImpl.logger.error("[alterOrder][Alter Order Fail][Create new order fail][newOrderId: {}]", newOrder.getId());
            return new Response<>(0, cor.getMsg(), null);
        }
    }

    @Override
    public Response<ArrayList<Order>> queryOrders(QueryInfo qi, String accountId, HttpHeaders headers) {
        logger.info("[function name:{}][qi:{}, accountId:{}, headers:{}]","queryOrders",(qi != null ? qi.toString(): null), accountId, (headers != null ? headers.toString(): null));
        //1.Get all orders of the user
        ArrayList<Order> list = orderOtherRepository.findByAccountId(accountId);
      logger.info("[list:{}]", (list != null ? list : null));
      
      
      
      
      
      
      
      
      
      
      
      
        //2.Check is these orders fit the requirement/
        if (qi.isEnableStateQuery() || qi.isEnableBoughtDateQuery() || qi.isEnableTravelDateQuery()) {
            ArrayList<Order> finalList = new ArrayList<>();
            for (Order tempOrder : list) {
                boolean statePassFlag = false;
                boolean boughtDatePassFlag = false;
                boolean travelDatePassFlag = false;
                //3.Check order state requirement.
                if (qi.isEnableStateQuery()) {
                    if (tempOrder.getStatus() != qi.getState()) {
                        statePassFlag = false;
                    } else {
                        statePassFlag = true;
                    }
                } else {
                    statePassFlag = true;
                }
                //4.Check order travel date requirement.
                Date boughtDate = StringUtils.String2Date(tempOrder.getBoughtDate());
                Date travelDate = StringUtils.String2Date(tempOrder.getTravelDate());
                Date travelDateEnd = StringUtils.String2Date(qi.getTravelDateEnd());
                Date boughtDateStart = StringUtils.String2Date(qi.getBoughtDateStart());
                Date boughtDateEnd = StringUtils.String2Date(qi.getBoughtDateEnd());
                if (qi.isEnableTravelDateQuery()) {
                    if (travelDate.before(travelDateEnd) &&
                            travelDate.after(boughtDateStart)) {
                        travelDatePassFlag = true;
                    } else {
                        travelDatePassFlag = false;
                    }
                } else {
                    travelDatePassFlag = true;
                }
                //5.Check order bought date requirement.
                if (qi.isEnableBoughtDateQuery()) {
                    if (boughtDate.before(boughtDateEnd) &&
                            boughtDate.after(boughtDateStart)) {
                        boughtDatePassFlag = true;
                    } else {
                        boughtDatePassFlag = false;
                    }
                } else {
                    boughtDatePassFlag = true;
                }
                //6.check if all requirement fits.
                if (statePassFlag && boughtDatePassFlag && travelDatePassFlag) {
                    finalList.add(tempOrder);
                }
            }
            return new Response<>(1, "Get order num", finalList);
        } else {
            OrderOtherServiceImpl.logger.warn("[queryOrders][Orders don't fit the requirement][loginId: {}]", qi.getLoginId());
            return new Response<>(1, "Get order num", list);
        }
    }

    @Override
    public Response queryOrdersForRefresh(QueryInfo qi, String accountId, HttpHeaders headers) {
        logger.info("[function name:{}][qi:{}, accountId:{}, headers:{}]","queryOrdersForRefresh",(qi != null ? qi.toString(): null), accountId, (headers != null ? headers.toString(): null));
        ArrayList<Order> orders = queryOrders(qi, accountId, headers).getData();
        ArrayList<String> stationIds = new ArrayList<>();
        for (Order order : orders) {
            stationIds.add(order.getFrom());
            stationIds.add(order.getTo());
        }
        for (int i = 0; i < orders.size(); i++) {
            orders.get(i).setFrom(stationIds.get(i * 2));
            orders.get(i).setTo(stationIds.get(i * 2 + 1));
        }
        return new Response<>(1, success, orders);
    }

    public List<String> queryForStationId(List<String> ids, HttpHeaders headers) {
        logger.info("[function name:{}][ids:{}, headers:{}]","queryForStationId",(ids != null ? ids.toString(): null), (headers != null ? headers.toString(): null));

        HttpEntity requestEntity = new HttpEntity(ids, null);
        String station_service_url=getServiceUrl("ts-station-service");
        ResponseEntity<Response<List<String>>> re = restTemplate.exchange(
                station_service_url + "/api/v1/stationservice/stations/namelist",
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<Response<List<String>>>() {
                });
        logger.info("[status code:{}, url:{} and type:{}]",re.getStatusCode(),
                station_service_url + "/api/v1/stationservice/stations/namelist","POST");
        return re.getBody().getData();
    }

    @Override
    public Response saveChanges(Order order, HttpHeaders headers) {
        logger.info("[function name:{}][order:{}, headers:{}]","saveChanges",(order != null ? order.toString(): null), (headers != null ? headers.toString(): null));
        Optional<Order> op = orderOtherRepository.findById(order.getId());
      logger.info("[op:{}]", (op != null ? op : null));
      
      
      
      
      
      
      
      
      
      
      
      
        if (!op.isPresent() ) {
            OrderOtherServiceImpl.logger.error("[saveChanges][Modify Order Fail][Order not found][OrderId: {}]", order.getId());
            return new Response<>(0, orderNotFound, null);
        } else {
            Order oldOrder = op.get();
            oldOrder.setAccountId(order.getAccountId());
            oldOrder.setBoughtDate(order.getBoughtDate());
            oldOrder.setTravelDate(order.getTravelDate());
            oldOrder.setTravelTime(order.getTravelTime());
            oldOrder.setSeatClass(order.getSeatClass());
            oldOrder.setCoachNumber(order.getCoachNumber());

            oldOrder.setSeatNumber(order.getSeatNumber());
            oldOrder.setTo(order.getTo());
            oldOrder.setFrom(order.getFrom());
            oldOrder.setStatus(order.getStatus());
            oldOrder.setTrainNumber(order.getTrainNumber());
            oldOrder.setPrice(order.getPrice());
            oldOrder.setContactsName(order.getContactsName());
            oldOrder.setDocumentType(order.getDocumentType());
            oldOrder.setContactsDocumentNumber(order.getContactsDocumentNumber());

            orderOtherRepository.save(oldOrder);
            return new Response<>(1, success, oldOrder);
        }
    }

    @Override
    public Response cancelOrder(String accountId, String orderId, HttpHeaders headers) {
        logger.info("[function name:{}][accountId:{}, orderId:{}, headers:{}]","cancelOrder",accountId, orderId, (headers != null ? headers.toString(): null));

        Optional<Order> op = orderOtherRepository.findById(orderId);
      logger.info("[op:{}]", (op != null ? op : null));
      
      
      
      
      
      
      
      
      
      
      
      
        if (!op.isPresent()) {
            OrderOtherServiceImpl.logger.error("[cancelOrder][Cancel Order Fail][Order not found][OrderId: {}]", orderId);
            return new Response<>(0, orderNotFound, null);
        } else {
            Order oldOrder = op.get();
            oldOrder.setStatus(OrderStatus.CANCEL.getCode());
            orderOtherRepository.save(oldOrder);
            return new Response<>(1, success, oldOrder);
        }
    }

    @Override
    public Response queryAlreadySoldOrders(Date travelDate, String trainNumber, HttpHeaders headers) {
        logger.info("[function name:{}][travelDate:{}, trainNumber:{}, headers:{}]","queryAlreadySoldOrders",(travelDate != null ? travelDate.toString(): null), trainNumber, (headers != null ? headers.toString(): null));
        ArrayList<Order> orders = orderOtherRepository.findByTravelDateAndTrainNumber(StringUtils.Date2String(travelDate), trainNumber);
      logger.info("[orders:{}]", (orders != null ? orders : null));
      
      
      
      
      
      
      
      
      
      
      
      
        SoldTicket cstr = new SoldTicket();
        cstr.setTravelDate(travelDate);
        cstr.setTrainNumber(trainNumber);
        for (Order order : orders) {
            if (order.getStatus() >= OrderStatus.CHANGE.getCode()) {
                continue;
            }
            if (order.getSeatClass() == SeatClass.NONE.getCode()) {
                cstr.setNoSeat(cstr.getNoSeat() + 1);
            } else if (order.getSeatClass() == SeatClass.BUSINESS.getCode()) {
                cstr.setBusinessSeat(cstr.getBusinessSeat() + 1);
            } else if (order.getSeatClass() == SeatClass.FIRSTCLASS.getCode()) {
                cstr.setFirstClassSeat(cstr.getFirstClassSeat() + 1);
            } else if (order.getSeatClass() == SeatClass.SECONDCLASS.getCode()) {
                cstr.setSecondClassSeat(cstr.getSecondClassSeat() + 1);
            } else if (order.getSeatClass() == SeatClass.HARDSEAT.getCode()) {
                cstr.setHardSeat(cstr.getHardSeat() + 1);
            } else if (order.getSeatClass() == SeatClass.SOFTSEAT.getCode()) {
                cstr.setSoftSeat(cstr.getSoftSeat() + 1);
            } else if (order.getSeatClass() == SeatClass.HARDBED.getCode()) {
                cstr.setHardBed(cstr.getHardBed() + 1);
            } else if (order.getSeatClass() == SeatClass.SOFTBED.getCode()) {
                cstr.setSoftBed(cstr.getSoftBed() + 1);
            } else if (order.getSeatClass() == SeatClass.HIGHSOFTBED.getCode()) {
                cstr.setHighSoftBed(cstr.getHighSoftBed() + 1);
            } else {
            }
        }
        return new Response<>(1, success, cstr);
    }

    @Override
    public Response getAllOrders(HttpHeaders headers) {
        logger.info("[function name:{}][headers:{}]","getAllOrders",(headers != null ? headers.toString(): null));
        ArrayList<Order> orders = orderOtherRepository.findAll();
      logger.info("[orders:{}]", (orders != null ? orders : null));
      
      
      
      
      
      
      
      
      
      
      
      
        if (orders == null) {
            OrderOtherServiceImpl.logger.warn("[getAllOrders][Find all orders warn][{}]","No content");
            return new Response<>(0, "No Content", null);
        } else {
            return new Response<>(1, success, orders);
        }
    }

    @Override
    public Response modifyOrder(String orderId, int status, HttpHeaders headers) {
        logger.info("[function name:{}][orderId:{}, status:{}, headers:{}]","modifyOrder",orderId, status, (headers != null ? headers.toString(): null));
        Optional<Order> op = orderOtherRepository.findById(orderId);
      logger.info("[op:{}]", (op != null ? op : null));
      
      
      
      
      
      
      
      
      
      
      
      
        if (!op.isPresent()) {
            OrderOtherServiceImpl.logger.error("[modifyOrder][Modify order Fail][Order not found][OrderId: {}]",orderId);
            return new Response<>(0, orderNotFound, null);
        } else {
            Order order = op.get();
            order.setStatus(status);
            orderOtherRepository.save(order);
            return new Response<>(1, success, order);
        }
    }

    @Override
    public Response getOrderPrice(String orderId, HttpHeaders headers) {
        logger.info("[function name:{}][orderId:{}, headers:{}]","getOrderPrice",orderId, (headers != null ? headers.toString(): null));
        Optional<Order> op = orderOtherRepository.findById(orderId);
      logger.info("[op:{}]", (op != null ? op : null));
      
      
      
      
      
      
      
      
      
      
      
      
        if (!op.isPresent()) {
            OrderOtherServiceImpl.logger.error("[getOrderPrice][Get order price Fail][Order not found][OrderId: {}]",orderId);
            return new Response<>(0, orderNotFound, "-1.0");
        } else {
            Order order = op.get();
            return new Response<>(1, success, order.getPrice());
        }
    }

    @Override
    public Response payOrder(String orderId, HttpHeaders headers) {
        logger.info("[function name:{}][orderId:{}, headers:{}]","payOrder",orderId, (headers != null ? headers.toString(): null));
        Optional<Order> op = orderOtherRepository.findById(orderId);
      logger.info("[op:{}]", (op != null ? op : null));
      
      
      
      
      
      
      
      
      
      
      
      
        if (!op.isPresent()) {
            OrderOtherServiceImpl.logger.error("[payOrder][Pay order Fail][Order not found][OrderId: {}]",orderId);
            return new Response<>(0, orderNotFound, null);
        } else {
            Order order = op.get();
            order.setStatus(OrderStatus.PAID.getCode());
            orderOtherRepository.save(order);
            return new Response<>(1, success, order);
        }
    }

    @Override
    public Response getOrderById(String orderId, HttpHeaders headers) {
        logger.info("[function name:{}][orderId:{}, headers:{}]","getOrderById",orderId, (headers != null ? headers.toString(): null));
        Optional<Order> op = orderOtherRepository.findById(orderId);
      logger.info("[op:{}]", (op != null ? op : null));
      
      
      
      
      
      
      
      
      
      
      
      

        if(!op.isPresent()) {
            OrderOtherServiceImpl.logger.error("[getOrderById][Get Order By ID Fail][Order not found][OrderId: {}]",orderId);
            return new Response<>(0, orderNotFound, null);
        } else {
            Order order = op.get();
            return new Response<>(1, success, order);
        }
    }

    @Override
    public Response checkSecurityAboutOrder(Date dateFrom, String accountId, HttpHeaders headers) {
        logger.info("[function name:{}][dateFrom:{}, accountId:{}, headers:{}]","checkSecurityAboutOrder",(dateFrom != null ? dateFrom.toString(): null), accountId, (headers != null ? headers.toString(): null));
        OrderSecurity result = new OrderSecurity();
        ArrayList<Order> orders = orderOtherRepository.findByAccountId(accountId);
      logger.info("[orders:{}]", (orders != null ? orders : null));
      
      
      
      
      
      
      
      
      
      
      
      
        int countOrderInOneHour = 0;
        int countTotalValidOrder = 0;
        Calendar ca = Calendar.getInstance();
        ca.setTime(dateFrom);
        ca.add(Calendar.HOUR_OF_DAY, -1);
        dateFrom = ca.getTime();
        for (Order order : orders) {
            if (order.getStatus() == OrderStatus.NOTPAID.getCode() ||
                    order.getStatus() == OrderStatus.PAID.getCode() ||
                    order.getStatus() == OrderStatus.COLLECTED.getCode()) {
                countTotalValidOrder += 1;
            }
            Date boughtDate = StringUtils.String2Date(order.getBoughtDate());
            if (boughtDate.after(dateFrom)) {
                countOrderInOneHour += 1;
            }
        }
        result.setOrderNumInLastOneHour(countOrderInOneHour);
        result.setOrderNumOfValidOrder(countTotalValidOrder);
        return new Response<>(1, success, result);
    }

    @Override
    public Response deleteOrder(String orderId, HttpHeaders headers) {
        logger.info("[function name:{}][orderId:{}, headers:{}]","deleteOrder",orderId, (headers != null ? headers.toString(): null));
        String orderUuid = UUID.fromString(orderId).toString();
        Optional<Order> op = orderOtherRepository.findById(orderUuid);
      logger.info("[op:{}]", (op != null ? op : null));
      
      
      
      
      
      
      
      
      
      
      
      
        if(!op.isPresent()) {
            OrderOtherServiceImpl.logger.error("[deleteOrder][Delete order Fail][Order not found][OrderId: {}]",orderId);
            return new Response<>(0, "Order Not Exist.", null);
        } else {
            Order order = op.get();
            orderOtherRepository.deleteById(orderUuid);
            return new Response<>(1, success, orderUuid);
        }
    }

    @Override
    public Response addNewOrder(Order order, HttpHeaders headers) {
        logger.info("[function name:{}][order:{}, headers:{}]","addNewOrder",(order != null ? order.toString(): null), (headers != null ? headers.toString(): null));
        ArrayList<Order> accountOrders = orderOtherRepository.findByAccountId(order.getAccountId());
      logger.info("[accountOrders:{}]", (accountOrders != null ? accountOrders : null));
      
      
      
      
      
      
      
      
      
      
      
      
        if (accountOrders.contains(order)) {
            OrderOtherServiceImpl.logger.error("[addNewOrder][Admin Add Order Fail][Order already exists][OrderId: {}]",order.getId());
            return new Response<>(0, "Order already exist", null);
        } else {
            order.setId(UUID.randomUUID().toString());
            orderOtherRepository.save(order);
            return new Response<>(1, success, order);
        }
    }

    @Override
    public Response updateOrder(Order order, HttpHeaders headers) {
        logger.info("[function name:{}][order:{}, headers:{}]","updateOrder",(order != null ? order.toString(): null), (headers != null ? headers.toString(): null));

        Optional<Order> op = orderOtherRepository.findById(order.getId());
      logger.info("[op:{}]", (op != null ? op : null));
      
      
      
      
      
      
      
      
      
      
      
      
        if(!op.isPresent()) {
            OrderOtherServiceImpl.logger.error("[updateOrder][Admin Update Order Fail][Order not found][OrderId: {}]",order.getId());
            return new Response<>(0, orderNotFound, null);
        } else {
            Order oldOrder = op.get();
            oldOrder.setAccountId(order.getAccountId());
            oldOrder.setBoughtDate(order.getBoughtDate());
            oldOrder.setTravelDate(order.getTravelDate());
            oldOrder.setTravelTime(order.getTravelTime());
            oldOrder.setCoachNumber(order.getCoachNumber());
            oldOrder.setSeatClass(order.getSeatClass());
            oldOrder.setSeatNumber(order.getSeatNumber());
            oldOrder.setFrom(order.getFrom());
            oldOrder.setTo(order.getTo());
            oldOrder.setStatus(order.getStatus());
            oldOrder.setTrainNumber(order.getTrainNumber());
            oldOrder.setPrice(order.getPrice());
            oldOrder.setContactsName(order.getContactsName());
            oldOrder.setContactsDocumentNumber(order.getContactsDocumentNumber());
            oldOrder.setDocumentType(order.getDocumentType());
            orderOtherRepository.save(oldOrder);
            return new Response<>(1, success, oldOrder);
        }
    }
}

