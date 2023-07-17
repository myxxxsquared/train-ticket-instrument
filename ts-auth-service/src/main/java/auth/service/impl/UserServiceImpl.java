package auth.service.impl;

import auth.constant.AuthConstant;














import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import auth.constant.InfoConstant;
import auth.dto.AuthDto;
import auth.entity.User;
import auth.exception.UserOperationException;
import auth.repository.UserRepository;
import auth.service.UserService;
import edu.fudan.common.util.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.*;

/**
 * @author fdse
 */
@Service
public class UserServiceImpl implements UserService { 
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);













    @Autowired
    private UserRepository userRepository;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Override
    public User saveUser(User user) {
        logger.info("[function name:{}][user:{}]","saveUser",(user != null ? user.toString(): null));
        return null;
    }

    @Override
    public List<User> getAllUser(HttpHeaders headers) {
        logger.info("[function name:{}][headers:{}]","getAllUser",(headers != null ? headers.toString(): null));
        return (List<User>) userRepository.findAll();
    }

    /**
     * create  a user with default role of user
     *
     * @param dto
     * @return
     */
    @Override
    public User createDefaultAuthUser(AuthDto dto) {
        logger.info("[function name:{}][dto:{}]","createDefaultAuthUser",(dto != null ? dto.toString(): null));
        User user = null;
    
        if (!dto.getUserName().contains("admin")) {
            user = User.builder()
                    .userId(dto.getUserId())
                    .username(dto.getUserName())
                    .password(passwordEncoder.encode(dto.getPassword()))
                    .roles(new HashSet<>(Arrays.asList(AuthConstant.ROLE_USER)))
                    .build();
    
        try {
            checkUserCreateInfo(user);
        } catch (UserOperationException e) {
            logger.error("[createDefaultAuthUser][Create default auth user][UserOperationException][message: {}]", e.getMessage());
        }
        
        if (user != null) {
            return userRepository.save(user);
        }} 
        else {
            String username = dto.getUserName().replace("_admin", "");
            user = User.builder()
            .userId(dto.getUserId())
            .username(username)
            .password(passwordEncoder.encode(dto.getPassword()))
            .roles(new HashSet<>(Arrays.asList(AuthConstant.ROLE_ADMIN)))
            .build();
            try {
                checkUserCreateInfo(user);
            } catch (UserOperationException e) {
                logger.error("[createDefaultAuthUser][Create default auth user][UserOperationException][message: {}]", e.getMessage());
            }
                return userRepository.save(user);
    }
        return userRepository.save(user);
        }

    @Override
    @Transactional
    public Response deleteByUserId(String userId, HttpHeaders headers) {
        logger.info("[function name:{}][userId:{}, headers:{}]","deleteByUserId",userId, (headers != null ? headers.toString(): null));
        userRepository.deleteByUserId(userId);
        return new Response(1, "DELETE USER SUCCESS", null);
    }

    /**
     * check Whether user info is empty
     *
     * @param user
     */
    private void checkUserCreateInfo(User user) throws UserOperationException {
        logger.info("[function name:{}][user:{}]","checkUserCreateInfo",(user != null ? user.toString(): null));
        List<String> infos = new ArrayList<>();

        if (null == user.getUsername() || "".equals(user.getUsername())) {
            infos.add(MessageFormat.format(InfoConstant.PROPERTIES_CANNOT_BE_EMPTY_1, InfoConstant.USERNAME));
        }

        int passwordMaxLength = 6;
        if (null == user.getPassword()) {
            infos.add(MessageFormat.format(InfoConstant.PROPERTIES_CANNOT_BE_EMPTY_1, InfoConstant.PASSWORD));
        } else if (user.getPassword().length() < passwordMaxLength) {
            infos.add(MessageFormat.format(InfoConstant.PASSWORD_LEAST_CHAR_1, 6));
        }

        if (null == user.getRoles() || user.getRoles().isEmpty()) {
            infos.add(MessageFormat.format(InfoConstant.PROPERTIES_CANNOT_BE_EMPTY_1, InfoConstant.ROLES));
        }

        if (!infos.isEmpty()) {
            logger.warn(infos.toString());
            throw new UserOperationException(infos.toString());
        }
    }

}
