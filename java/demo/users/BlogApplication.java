package demo.blog;

import demo.blog.entities.Role;
import demo.blog.entities.User;
import demo.blog.repositories.RoleRepository;
import demo.blog.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class BlogApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlogApplication.class, args);
	}

	@Bean
	CommandLineRunner run(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			if (roleRepository.findByAuthority("ROLE_ADMIN") != null) {
				return;
			}

			roleRepository.save(new Role("ROLE_USER"));
			roleRepository.save(new Role("ROLE_ADMIN"));

		};
	}

}
