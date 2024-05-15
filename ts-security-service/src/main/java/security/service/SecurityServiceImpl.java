package security.service;

import edu.fudan.common.entity.OrderSecurity;


















import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import edu.fudan.common.util.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import security.entity.SecurityConfig;
import security.repository.SecurityRepository;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author fdse
 */
@Service
public class SecurityServiceImpl implements SecurityService { 
    private static final Logger logger = LogManager.getLogger(SecurityServiceImpl.class);






















    @Autowired
    private SecurityRepository securityRepository;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private DiscoveryClient discoveryClient;

    private String getServiceUrl(String serviceName) {
        return "http://" + serviceName;
    }

    String success = "Success";

    @Override
    public Response findAllSecurityConfig(HttpHeaders headers) {
        ArrayList<SecurityConfig> securityConfigs = securityRepository.findAll();
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
        if (securityConfigs != null && !securityConfigs.isEmpty()) {
            return new Response<>(1, success, securityConfigs);
        }
        SecurityServiceImpl.logger.warn("[findAllSecurityConfig][Find all security config warn][{}]","No content");
        return new Response<>(0, "No Content", null);
    }

    @Override
    public Response addNewSecurityConfig(SecurityConfig info, HttpHeaders headers) {
        SecurityConfig sc = securityRepository.findByName(info.getName());
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
        if (sc != null) {
            SecurityServiceImpl.logger.warn("[addNewSecurityConfig][Add new Security config warn][Security config already exist][SecurityConfigId: {},Name: {}]",sc.getId(),info.getName());
            return new Response<>(0, "Security Config Already Exist", null);
        } else {
            SecurityConfig config = new SecurityConfig();
            config.setId(UUID.randomUUID().toString());
            config.setName(info.getName());
            config.setValue(info.getValue());
            config.setDescription(info.getDescription());
            securityRepository.save(config);
            return new Response<>(1, success, config);
        }
    }

    @Override
    public Response modifySecurityConfig(SecurityConfig info, HttpHeaders headers) {
        SecurityConfig sc = securityRepository.findById(info.getId()).orElse(null);
        if (sc == null) {
            SecurityServiceImpl.logger.error("[modifySecurityConfig][Modify Security config error][Security config not found][SecurityConfigId: {},Name: {}]",info.getId(),info.getName());
            return new Response<>(0, "Security Config Not Exist", null);
        } else {
            sc.setName(info.getName());
            sc.setValue(info.getValue());
            sc.setDescription(info.getDescription());
      securityRepository.save(sc);
            return new Response<>(1, success, sc);
        }
    }

    @Transactional
    @Override
    public Response deleteSecurityConfig(String id, HttpHeaders headers) {
        securityRepository.deleteById(id);
        SecurityConfig sc = securityRepository.findById(id).orElse(null);
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
        if (sc == null) {
            return new Response<>(1, success, id);
        } else {
            SecurityServiceImpl.logger.error("[deleteSecurityConfig][Delete Security config error][Reason not clear][SecurityConfigId: {}]",id);
            return new Response<>(0, "Reason Not clear", id);
        }
    }

    @Override
    public Response check(String accountId, HttpHeaders headers) {
        //1.Get the orders in the past one hour and the total effective votes
        SecurityServiceImpl.logger.debug("[check][Get Order Num Info]");
        OrderSecurity orderResult = getSecurityOrderInfoFromOrder(new Date(), accountId, headers);
        OrderSecurity orderOtherResult = getSecurityOrderOtherInfoFromOrder(new Date(), accountId, headers);
        int orderInOneHour = orderOtherResult.getOrderNumInLastOneHour() + orderResult.getOrderNumInLastOneHour();
        int totalValidOrder = orderOtherResult.getOrderNumOfValidOrder() + orderResult.getOrderNumOfValidOrder();
        //2. get critical configuration information
        SecurityServiceImpl.logger.debug("[check][Get Security Config Info]");
        SecurityConfig configMaxInHour = securityRepository.findByName("max_order_1_hour");
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
        SecurityConfig configMaxNotUse = securityRepository.findByName("max_order_not_use");
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
        int oneHourLine = Integer.parseInt(configMaxInHour.getValue());
        int totalValidLine = Integer.parseInt(configMaxNotUse.getValue());
        if (orderInOneHour > oneHourLine || totalValidOrder > totalValidLine) {
            SecurityServiceImpl.logger.warn("[check][Check Security config warn][Too much order in last one hour or too much valid order][AccountId: {}]",accountId);
            return new Response<>(0, "Too much order in last one hour or too much valid order", accountId);
        } else {
            return new Response<>(1, "Success.r", accountId);
        }
    }

    private OrderSecurity getSecurityOrderInfoFromOrder(Date checkDate, String accountId, HttpHeaders headers) {
        HttpEntity requestEntity = new HttpEntity(null);
        String order_service_url = getServiceUrl("ts-order-service");
        ResponseEntity<Response<OrderSecurity>> re = restTemplate.exchange(
                order_service_url + "/api/v1/orderservice/order/security/" + checkDate + "/" + accountId,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<Response<OrderSecurity>>() {
                });
        Response<OrderSecurity> response = re.getBody();
        OrderSecurity result =  response.getData();
        return result;
    }

    private OrderSecurity getSecurityOrderOtherInfoFromOrder(Date checkDate, String accountId, HttpHeaders headers) {
        HttpEntity requestEntity = new HttpEntity(null);
        String order_other_service_url = getServiceUrl("ts-order-other-service");
        ResponseEntity<Response<OrderSecurity>> re = restTemplate.exchange(
                order_other_service_url + "/api/v1/orderOtherService/orderOther/security/" + checkDate + "/" + accountId,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<Response<OrderSecurity>>() {
                });
        Response<OrderSecurity> response = re.getBody();
        OrderSecurity result =  response.getData();
        return result;
    }

}
