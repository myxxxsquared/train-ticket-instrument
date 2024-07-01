package auth.init;

import auth.entity.User;
import auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
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


    @Override
    public void run(String... strings) throws Exception {
        User whetherExistUser = userRepository.findByUsername("fdse_microservice").orElse(new User());
        if (whetherExistUser.getUsername() == null) {
            User user = User.builder()
                    .userId("4d2a46c7-71cb-4cf1-b5bb-b68406d9da6f")
                    .username("fdse_microservice")
                    .password(passwordEncoder.encode("111111"))
                    .roles(new HashSet<>(Arrays.asList("ROLE_USER")))
                    .build();
            userRepository.save(user);
        }

        User whetherExistAdmin = userRepository.findByUsername("admin").orElse(new User());
        if (whetherExistAdmin.getUsername() == null) {
            User admin = User.builder()
                    .userId("c4f1da0b-b6c6-412c-944c-d1b4ddb153cf")
                    .username("admin")
                    .password(passwordEncoder.encode("222222"))
                    .roles(new HashSet<>(Arrays.asList("ROLE_ADMIN")))
                    .build();
            userRepository.save(admin);
        }

        whetherExistAdmin = userRepository.findByUsername("liaoyifan").orElse(new User());
        if (whetherExistAdmin.getUsername() == null) {
            User liaoyifan = User.builder()
                    .userId("c4f1da0b-b6c6-412c-944c-d324ddb153ca")
                    .username("liaoyifan")
                    .password(passwordEncoder.encode("liaoyifan1998"))
                    .roles(new HashSet<>(Arrays.asList("ROLE_USER")))
                    .build();
            userRepository.save(liaoyifan);
        }

        whetherExistAdmin = userRepository.findByUsername("miniship").orElse(new User());
        if (whetherExistAdmin.getUsername() == null) {
            User miniship = User.builder()
                    .userId("c4f1da0b-b6c6-412c-944c-d1b4ddb153ca")
                    .username("miniship")
                    .password(passwordEncoder.encode("miniship"))
                    .roles(new HashSet<>(Arrays.asList("ROLE_ADMIN")))
                    .build();
            userRepository.save(miniship);
        }
    }
}
