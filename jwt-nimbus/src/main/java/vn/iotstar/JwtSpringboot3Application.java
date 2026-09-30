package vn.iotstar;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import vn.iotstar.entity.User;
import vn.iotstar.repository.UserRepository;

@SpringBootApplication
public class JwtSpringboot3Application {
    public static void main(String[] args) { SpringApplication.run(JwtSpringboot3Application.class, args); }

    @Bean
    CommandLineRunner seed(UserRepository repository, PasswordEncoder encoder) {
        return args -> repository.findByEmail("admin@example.com").orElseGet(() -> {
            User user = new User();
            user.setFullName("Quan tri vien"); user.setEmail("admin@example.com");
            user.setPassword(encoder.encode("123456")); user.setImages("https://placehold.co/160x160?text=User");
            return repository.save(user);
        });
    }
}
