package user.service.impl;

import edu.fudan.common.util.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import user.dto.AuthDto;
import user.dto.UserDto;
import user.entity.User;
import user.repository.UserRepository;
import user.service.UserService;
import java.util.List;
import java.util.UUID;

/**
 * @author fdse
 */
@Service
public class UserServiceImpl implements UserService { 
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private RestTemplate restTemplate;

    private String getServiceUrl(String serviceName) {
        logger.info("[function name:{}][serviceName:{}]","getServiceUrl",serviceName);
        return "http://" + serviceName;
    }

    @Override
    public Response saveUser(UserDto userDto, HttpHeaders headers) {
        logger.info("[function name:{}][userDto:{}, headers:{}]","saveUser",(userDto != null ? userDto.toString(): null), (headers != null ? headers.toString(): null));
        String userId = userDto.getUserId();
        if (userDto.getUserId() == null) {
            userId = UUID.randomUUID().toString();
        }
        if (userDto.getUserName().contains("_admin")) {
            String oldusername = userDto.getUserName().replace("_admin", "");
            User oldUser = userRepository.findByUserName(oldusername);
      logger.info("[oldUser:{},headers:{}]", (oldUser != null ? oldUser : null));

            userId = oldUser.getUserId();
            deleteUser(oldUser.getUserId(), headers);
        }
        User user = User.builder()
                .userId(userId)
                .userName(userDto.getUserName())
                .password(userDto.getPassword())
                .gender(userDto.getGender())
                .documentType(userDto.getDocumentType())
                .documentNum(userDto.getDocumentNum())
                .email(userDto.getEmail()).build();

        // avoid same user name
        User user1 = userRepository.findByUserName(userDto.getUserName());
      logger.info("[user1:{},headers:{}]", (user1 != null ? user1 : null));

        if (user1 == null) {

            createDefaultAuthUser(AuthDto.builder().userId(userId + "")
                    .userName(user.getUserName())
                    .password(user.getPassword()).build());

            User userSaveResult = userRepository.save(user);

            return new Response<>(1, "REGISTER USER SUCCESS", userSaveResult);
        } else {
            UserServiceImpl.logger.error("[saveUser][Save user error][User already exists][UserId: {}]",userDto.getUserId());
            return new Response<>(0, "USER HAS ALREADY EXISTS", null);
        }
    }

    private Response createDefaultAuthUser(AuthDto dto) {
        logger.info("[function name:{}][dto:{}]","createDefaultAuthUser",(dto != null ? dto.toString(): null));
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<AuthDto> entity = new HttpEntity<>(dto, null);
        String auth_service_url = getServiceUrl("ts-auth-service");

        List<ServiceInstance> auth_svcs = discoveryClient.getInstances("ts-auth-service");
        if(auth_svcs.size() >0 ){
            ServiceInstance auth_svc = auth_svcs.get(0);
        }else{
        }

        ResponseEntity<Response<AuthDto>> res  = restTemplate.exchange(auth_service_url + "/api/v1/auth",
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<Response<AuthDto>>() {
                });
        logger.info("[status code:{}, url:{}, type:{}, headers:{}]",res.getStatusCode(),auth_service_url + "/api/v1/auth","POST",headers);
        return res.getBody();
    }

    @Override
    public Response getAllUsers(HttpHeaders headers) {
        logger.info("[function name:{}][headers:{}]","getAllUsers",(headers != null ? headers.toString(): null));
        List<User> users = userRepository.findAll();
      logger.info("[users:{},headers:{}]", (users != null ? users : null));

        if (users != null && !users.isEmpty()) {
            return new Response<>(1, "Success", users);
        }
        UserServiceImpl.logger.warn("[getAllUsers][Get all users warn: {}]","No Content");
        return new Response<>(0, "NO User", null);
    }

    @Override
    public Response findByUserName(String userName, HttpHeaders headers) {
        logger.info("[function name:{}][userName:{}, headers:{}]","findByUserName",userName, (headers != null ? headers.toString(): null));
        User user = userRepository.findByUserName(userName);
      logger.info("[user:{},headers:{}]", (user != null ? user : null));
      
        if (user != null) {
            return new Response<>(1, "Find User Success", user);
        }
        UserServiceImpl.logger.warn("[findByUserName][Get user by name warn,user is null][UserName: {}]",userName);
        return new Response<>(0, "No User", null);
    }

    @Override
    public Response findByUserId(String userId, HttpHeaders headers) {
        logger.info("[function name:{}][userId:{}, headers:{}]","findByUserId",userId, (headers != null ? headers.toString(): null));
        User user = userRepository.findByUserId(userId);
      logger.info("[user:{},headers:{}]", (user != null ? user : null));

        if (user != null) {
            return new Response<>(1, "Find User Success", user);
        }
        UserServiceImpl.logger.error("[findByUserId][Get user by id error,user is null][UserId: {}]",userId);
        return new Response<>(0, "No User", null);
    }

    @Override
    @Transactional
    public Response deleteUser(String userId, HttpHeaders headers) {
        logger.info("[function name:{}][userId:{}, headers:{}]","deleteUser",userId, (headers != null ? headers.toString(): null));
        User user = userRepository.findByUserId(userId);
      logger.info("[user:{},headers:{}]", (user != null ? user : null));

        if (user != null) {
            // first  only admin token can delete success
            deleteUserAuth(userId, headers);
            // second
            userRepository.deleteByUserId(userId);
            return new Response<>(1, "DELETE SUCCESS", null);
        } else {
            UserServiceImpl.logger.error("[deleteUser][Delete user error][User not found][UserId: {}]",userId);
            return new Response<>(0, "USER NOT EXISTS", null);
        }
    }

    @Override
    @Transactional
    public Response updateUser(UserDto userDto, HttpHeaders headers) {
        logger.info("[function name:{}][userDto:{}, headers:{}]","updateUser",(userDto != null ? userDto.toString(): null), (headers != null ? headers.toString(): null));
        User oldUser = userRepository.findByUserName(userDto.getUserName());
        logger.info("[oldUser:{},headers:{}]", (oldUser != null ? oldUser : null));

        if (oldUser != null) {
            User newUser = User.builder().email(userDto.getEmail())
                    .password(userDto.getPassword())
                    .userId(oldUser.getUserId())
                    .userName(userDto.getUserName())
                    .gender(userDto.getGender())
                    .documentNum(userDto.getDocumentNum())
                    .documentType(userDto.getDocumentType()).build();
            userRepository.deleteByUserId(oldUser.getUserId());
            userRepository.save(newUser);
            return new Response<>(1, "SAVE USER SUCCESS", newUser);
        } else {
            UserServiceImpl.logger.error("[updateUser][Update user error][User not found][UserId: {}]",userDto.getUserId());
            return new Response(0, "USER NOT EXISTS", null);
        }
    }

    public void deleteUserAuth(String userId, HttpHeaders headers) {
        logger.info("[function name:{}][userId:{}, headers:{}]","deleteUserAuth",userId, (headers != null ? headers.toString(): null));

        HttpHeaders newHeaders = new HttpHeaders();
        String token = headers.getFirst(HttpHeaders.AUTHORIZATION);
        newHeaders.set(HttpHeaders.AUTHORIZATION, token);

        HttpEntity<Response> httpEntity = new HttpEntity<>(newHeaders);

        String auth_service_url = getServiceUrl("ts-auth-service");
        String AUTH_SERVICE_URI = auth_service_url + "/api/v1";
        restTemplate.exchange(AUTH_SERVICE_URI + "/users/" + userId,
                HttpMethod.DELETE,
                httpEntity,
                Response.class);
        logger.info("[status code:{}, url:{}, headers:{}]",AUTH_SERVICE_URI + "/users/" + userId,"DELETE",headers);
    }
}
