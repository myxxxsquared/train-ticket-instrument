package user.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import user.entity.User;
import user.repository.UserRepository;
import user.service.UserService;


import java.util.UUID;

/**
 * @author fdse
 */
@Component
public class InitUser implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Override
    public void run(String... strings) throws Exception {
        User whetherExistUser = userRepository.findByUserName("fdse_microservice");
        User user = User.builder()
                .userId("4d2a46c7-71cb-4cf1-b5bb-b68406d9da6f")
                .userName("fdse_microservice")
                .password("111111")
                .gender(1)
                .documentType(1)
                .documentNum("2135488099312X")
                .email("trainticket_notify@163.com").build();
        user.setUserId("4d2a46c7-71cb-4cf1-b5bb-b68406d9da6f");
        if (whetherExistUser == null) {
            userRepository.save(user);
        }
        User whetherExistAdmin = userRepository.findByUserName("admin");
        User Admin = User.builder()
                .userId("c4f1da0b-b6c6-412c-944c-d1b4ddb153cf")
                .userName("admin")
                .password("222222")
                .gender(1)
                .documentType(1)
                .documentNum("2135488074882X")
                .email("admin@163.com").build();
        Admin.setUserId("c4f1da0b-b6c6-412c-944c-d1b4ddb153cf");
        if (whetherExistUser == null) {
            userRepository.save(Admin);
        }
    }
}
