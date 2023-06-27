package auth.security;

import auth.constant.InfoConstant;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import auth.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

/**
 * @author fdse
 */
@Component("userDetailServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService { 
    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);




    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        logger.info("the Optional<User> is: {}", (userRepository.findByUsername(s) != null ? userRepository.findByUsername(s).toString(): null));
        return userRepository.findByUsername(s)
                .orElseThrow(() -> new UsernameNotFoundException(
                        MessageFormat.format(InfoConstant.USER_NAME_NOT_FOUND_1, s)
                ));
    }
}
