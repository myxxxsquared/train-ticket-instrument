package adminuser.service;

import adminuser.dto.UserDto;











import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import edu.fudan.common.entity.User;
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


import java.util.List;

/**
 * @author fdse
 */
@Service
public class AdminUserServiceImpl implements AdminUserService { 
    private static final Logger logger = LoggerFactory.getLogger(AdminUserServiceImpl.class);












    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private DiscoveryClient discoveryClient;
//    @Value("${user-service.url}")
//    String user_service_url;
//    private final String USER_SERVICE_IP_URI = user_service_url + "/api/v1/userservice/users";

    private String getServiceUrl(String serviceName) {
        logger.info("[function name:{}][serviceName:{}]","getServiceUrl",serviceName);
        return "http://" + serviceName;
    }

    @Override
    public Response getAllUsers(HttpHeaders headers) {
        logger.info("[function name:{}][headers:{}]","getAllUsers",(headers != null ? headers.toString(): null));
        HttpEntity requestEntity = new HttpEntity(null);
        String user_service_url = getServiceUrl("ts-user-service");
        String USER_SERVICE_IP_URI = user_service_url + "/api/v1/userservice/users";
        ResponseEntity<Response<List<User>>> re = restTemplate.exchange(
                USER_SERVICE_IP_URI,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<Response<List<User>>>() {
                });
        logger.info("[status code:{}, url:{} and type:{}]",re.getStatusCode(),
                USER_SERVICE_IP_URI,"GET");
        if (re.getBody() == null || re.getBody().getStatus() != 1) {
            AdminUserServiceImpl.logger.error("[getAllUsers][receive response][Get All Users error]");
            return new Response<>(0, "get all users error", null);
        }
        return re.getBody();
    }


    @Override
    public Response deleteUser(String userId, HttpHeaders headers) {
        logger.info("[function name:{}][userId:{}, headers:{}]","deleteUser",userId, (headers != null ? headers.toString(): null));
        HttpHeaders newHeaders = new HttpHeaders();
        String token = headers.getFirst(HttpHeaders.AUTHORIZATION);
        newHeaders.set(HttpHeaders.AUTHORIZATION, token);

        HttpEntity<Response> requestEntity = new HttpEntity<>(newHeaders);

        String user_service_url = getServiceUrl("ts-user-service");
        String USER_SERVICE_IP_URI = user_service_url + "/api/v1/userservice/users";
        ResponseEntity<Response> re = restTemplate.exchange(
                USER_SERVICE_IP_URI + "/" + userId,
                HttpMethod.DELETE,
                requestEntity,
                Response.class);
        logger.info("[status code:{}, url:{} and type:{}]",re.getStatusCode(),
                USER_SERVICE_IP_URI + "/" + userId,"DELETE");
        if (re.getBody() == null || re.getBody().getStatus() != 1) {
            AdminUserServiceImpl.logger.error("[deleteUser][receive response][Delete user error][userId: {}]", userId);
            return new Response<>(0, "delete user error", null);
        }
        return re.getBody();
    }

    @Override
    public Response updateUser(UserDto userDto, HttpHeaders headers) {
        logger.info("[function name:{}][userDto:{}, headers:{}]","updateUser",(userDto != null ? userDto.toString(): null), (headers != null ? headers.toString(): null));

        HttpHeaders newHeaders = new HttpHeaders();
        String token = headers.getFirst(HttpHeaders.AUTHORIZATION);
        newHeaders.set(HttpHeaders.AUTHORIZATION, token);

        HttpEntity requestEntity = new HttpEntity(userDto, newHeaders);
        String user_service_url = getServiceUrl("ts-user-service");
        String USER_SERVICE_IP_URI = user_service_url + "/api/v1/userservice/users";
        ResponseEntity<Response> re = restTemplate.exchange(
                USER_SERVICE_IP_URI,
                HttpMethod.PUT,
                requestEntity,
                Response.class);
        logger.info("[status code:{}, url:{} and type:{}]",re.getStatusCode(),
                USER_SERVICE_IP_URI,"PUT");

        String userName = userDto.getUserName();
        if (re.getBody() == null || re.getBody().getStatus() != 1) {
            AdminUserServiceImpl.logger.error("[updateUser][receive response][Update user error][userName: {}]", userName);
            return new Response<>(0, "Update user error", null);
        }
        return re.getBody();
    }

    @Override
    public Response addUser(UserDto userDto, HttpHeaders headers) {
        logger.info("[function name:{}][userDto:{}, headers:{}]","addUser",(userDto != null ? userDto.toString(): null), (headers != null ? headers.toString(): null));
        HttpEntity requestEntity = new HttpEntity(userDto, null);
        String user_service_url = getServiceUrl("ts-user-service");
        String USER_SERVICE_IP_URI = user_service_url + "/api/v1/userservice/users";
        ResponseEntity<Response<User>> re = restTemplate.exchange(
                USER_SERVICE_IP_URI + "/register",
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<Response<User>>() {
                });
        logger.info("[status code:{}, url:{} and type:{}]",re.getStatusCode(),
                USER_SERVICE_IP_URI + "/register","POST");

        String userName = userDto.getUserName();
        if (re.getBody() == null || re.getBody().getStatus() != 1) {
            AdminUserServiceImpl.logger.error("[addUser][receive response][Add user error][userName: {}]", userName);
            return new Response<>(0, "Add user error", null);
        }
        return re.getBody();
    }
}
