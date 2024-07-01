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

        whetherExistUser = userRepository.findByUserName("miniship");
        User miniship = User.builder()
                .userId("c4f1da0b-b6c6-412c-944c-d1b4ddb153ca")
                .userName("miniship")
                .password("miniship")
                .gender(2)
                .documentType(1)
                .documentNum("212882X")
                .email("miniship@163.com").build();
        miniship.setUserId("c4f1da0b-b6c6-412c-944c-d1b4ddb153ca");
        if (whetherExistUser == null) {
            userRepository.save(miniship);
        }

        whetherExistUser = userRepository.findByUserName("liaoyifan");
        User liaoyifan = User.builder()
                .userId("c4f1da0b-b6c6-412c-944c-d324ddb153ca")
                .userName("liaoyifan")
                .password("liaoyifan1998")
                .gender(1)
                .documentType(2)
                .documentNum("323232")
                .email("liaoyifan@gmail.com").build();
        liaoyifan.setUserId("c4f1da0b-b6c6-412c-944c-d324ddb153ca");
        if (whetherExistUser == null) {
            userRepository.save(liaoyifan);
        }
    }
}
