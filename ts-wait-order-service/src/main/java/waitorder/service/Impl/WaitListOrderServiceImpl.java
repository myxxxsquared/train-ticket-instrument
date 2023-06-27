package waitorder.service.Impl;

import edu.fudan.common.util.Response;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import waitorder.entity.WaitListOrder;
import waitorder.entity.WaitListOrderStatus;
import waitorder.entity.WaitListOrderVO;
import waitorder.repository.WaitListOrderRepository;
import waitorder.service.WaitListOrderService;
import waitorder.utils.PollThread;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class WaitListOrderServiceImpl implements WaitListOrderService { 
    private static final Logger logger = LoggerFactory.getLogger(WaitListOrderServiceImpl.class);




    @Autowired
    private WaitListOrderRepository waitListOrderRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DiscoveryClient discoveryClient;

    String success = "Success";

    @Override
    public Response findOrderById(String id, HttpHeaders headers) {
        logger.info("[function name:{}][id:{}, headers:{}]","findOrderById",id, (headers != null ? headers.toString(): null));
        Optional<WaitListOrder> op = waitListOrderRepository.findById(id);
      logger.info("the op is: {}", (op != null ? op.toString(): null));
      
      
      
        if(!op.isPresent()){
            logger.warn("[findWaitOrderById][Find Order By Id Fail][No content][id: {}] ",id);
            return new Response<>(0, "No Content by this id", null);
        } else {
            WaitListOrder wo = op.get();
            return new Response<>(1, success, wo);
        }
    }

    @Transactional
    @Override
    public Response create(WaitListOrderVO orderVO, HttpHeaders headers) {
        logger.info("[function name:{}][orderVO:{}, headers:{}]","create",(orderVO != null ? orderVO.toString(): null), (headers != null ? headers.toString(): null));
        Response<WaitListOrder> response=saveNewOrder(orderVO,headers);
        if(response.getStatus()==0){
            //未能正常保存到数据库
            return response;
        } else {
            //已保存到数据库 开始轮询
            return triggerThread(response.getData(),orderVO,headers);
        }
    }

    @Override
    public Response getAllOrders(HttpHeaders headers) {
        logger.info("[function name:{}][headers:{}]","getAllOrders",(headers != null ? headers.toString(): null));
        List<WaitListOrder> orderList= waitListOrderRepository.findAll();
      logger.info("the orderList is: {}", (orderList != null ? orderList.toString(): null));
      
      
      
        if (orderList != null && !orderList.isEmpty()) {
            WaitListOrderServiceImpl.logger.warn("[getAllOrders][Find all orders Success][size:{}]",orderList.size());
            return new Response<>(1, "Success.", orderList);
        } else {
            logger.warn("[getAllOrders][Find All Wait List Orders Fail][{}]","No content");
            return new Response<>(0, "No Content.", null);
        }
    }

    @Override
    public Response getAllWaitListOrders(HttpHeaders headers) {
        logger.info("[function name:{}][headers:{}]","getAllWaitListOrders",(headers != null ? headers.toString(): null));
        List<WaitListOrder> orderList= waitListOrderRepository.findAll();
      logger.info("the orderList is: {}", (orderList != null ? orderList.toString(): null));
      
      
      
        if (orderList != null && !orderList.isEmpty()) {
            WaitListOrderServiceImpl.logger.warn("[getAllWaitListOrders][Find all orders Success][size:{}]",orderList.size());
            List<Integer> filterList=new ArrayList<>();
            filterList.add(WaitListOrderStatus.NOTPAID.getCode());
            filterList.add(WaitListOrderStatus.PAID.getCode());
            //Only orders in the wait list will be selected
            orderList=orderList.stream()
                    .filter(WaitListOrder -> filterList.contains(WaitListOrder.getStatus()))
                    .collect(Collectors.toList());
            return new Response<>(1, "Success.", orderList);
        } else {
            logger.warn("[getAllWaitListOrders][Find All Wait List Orders Fail][{}]","No content");
            return new Response<>(0, "No Content.", null);
        }
    }

    @Transactional
    @Override
    public Response updateOrder(WaitListOrder order, HttpHeaders headers) {
        logger.info("[function name:{}][order:{}, headers:{}]","updateOrder",(order != null ? order.toString(): null), (headers != null ? headers.toString(): null));
        Optional<WaitListOrder> op = waitListOrderRepository.findById(order.getId());
      logger.info("the op is: {}", (op != null ? op.toString(): null));
      
      
      
        if(!op.isPresent()){
            logger.error("[updateOrder][Update Order Info Fail][Order not found][OrderId: {}]",order.getId());
            return new Response<>(0, "Order Not Found, Can't update", null);
        } else {
            WaitListOrder old = op.get();
            BeanUtils.copyProperties(old,order);
            waitListOrderRepository.save(old);
            return new Response<>(1, "Update Wait List Order Success", old);
        }
    }

    @Transactional
    @Override
    public Response modifyWaitListOrderStatus(int status, String orderId) {
        logger.info("[function name:{}][status:{}, orderId:{}]","modifyWaitListOrderStatus",status, orderId);
        Optional<WaitListOrder> op = waitListOrderRepository.findById(orderId);
      logger.info("the op is: {}", (op != null ? op.toString(): null));
      
      
      
        if(!op.isPresent()){
            logger.error("[modifyWaitListOrderStatus][Modify Order Status Fail][Order not found][OrderId: {}]",orderId);
            return new Response<>(0, "Order Not Found, Can't update", null);
        } else {
            WaitListOrder old = op.get();
            old.setStatus(status);
            waitListOrderRepository.save(old);
            return new Response<>(1, "Modify Wait List Order Status Success", old);
        }
    }

    private Response<WaitListOrder> saveNewOrder(WaitListOrderVO orderVO, HttpHeaders headers) {
        ArrayList<WaitListOrder> accountOrders= waitListOrderRepository.findByAccountId(orderVO.getAccountId());
        //if the order already exist
        if(WaitListOrderExist(accountOrders,orderVO)){
            WaitListOrderServiceImpl.logger.error("[create][Create Wait Order Fail][Order already exists][AccountId: {} , TripId: {}]", orderVO.getAccountId(),orderVO.getTripId());
            return new Response<>(0, "Order already exist", null);
        } else {
            WaitListOrder newWaitListOrder=new WaitListOrder();
            newWaitListOrder.setId(UUID.randomUUID().toString());
            BeanUtils.copyProperties(newWaitListOrder,orderVO);
            newWaitListOrder.setTrainNumber(orderVO.getTripId());
            waitListOrderRepository.save(newWaitListOrder);
            return new Response<>(1,success,newWaitListOrder);
        }
    }

    private Boolean WaitListOrderExist(List<WaitListOrder> orderList,WaitListOrderVO newOrder){
        for(WaitListOrder order: orderList){
            if(Objects.equals(order.getAccountId(), newOrder.getAccountId())
                    && Objects.equals(order.getContactsId(), newOrder.getContactsId())
                    && Objects.equals(order.getTrainNumber(), newOrder.getTripId())
                    && Objects.equals(order.getTravelTime(),newOrder.getDate())
                    && Objects.equals(order.getFrom(),newOrder.getFrom())
                    && Objects.equals(order.getTo(),newOrder.getTo())){
                return true;
            }
        }
        return false;
    }

    private Response triggerThread(WaitListOrder orderPO,WaitListOrderVO orderVO,HttpHeaders headers){
        PollThread pollThread;
        try{
            pollThread =new PollThread(orderPO.getWaitUtilTime(),this,orderVO,restTemplate, headers);
            pollThread.start();
        } catch (Exception e){
            return new Response<>(0, "Fail To Run A New Thread", null);
        }
        return new Response<>(1,"Thread Start Success",null);
    }


}
