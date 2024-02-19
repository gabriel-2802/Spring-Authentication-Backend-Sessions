package demo.blog.services;

import demo.blog.dto.LoginDto;
import demo.blog.dto.RegisterDto;
import demo.blog.entities.Role;
import demo.blog.entities.User;
import demo.blog.repositories.RoleRepository;
import demo.blog.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class AuthServiceV1 implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final Logger logger = LoggerFactory.getLogger(AuthServiceV1.class);

    @Override
    public ResponseEntity<String> register(RegisterDto registerDto) {
        if (userRepository.existsByUsername(registerDto.getUsername())) {
            return ResponseEntity.badRequest().body("Username is already taken");
        }

        if (userRepository.existsByEmail(registerDto.getEmail())) {
            return ResponseEntity.badRequest().body("Email is already taken");
        }

        User user = new User();
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        Role role = roleRepository.findByAuthority("ROLE_USER");
        user.getRoles().add(role);

        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully");
    }

    public ResponseEntity<String> login(LoginDto loginDto) {
        try {
            Authentication requestAuthentication = new UsernamePasswordAuthenticationToken(
                    loginDto.getUsername(), loginDto.getPassword());
            Authentication authentication = authenticationManager.authenticate(requestAuthentication);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Debugging: Print authentication details
            Authentication debugAuthentication = SecurityContextHolder.getContext().getAuthentication();
            if (debugAuthentication != null) {
                System.out.println("Authentication successful. Principal: {}" +  debugAuthentication.getName());
                System.out.println("Authorities: {}" +  debugAuthentication.getAuthorities());
                System.out.println("Details: {}" +  debugAuthentication.getDetails());
            } else {
                System.out.println("Authentication object is null.");
            }

            return ResponseEntity.ok("User logged in successfully");
        } catch (AuthenticationException e) {
            System.out.println("Authentication failed: {}" + e.getMessage());
            return ResponseEntity.badRequest().body("Invalid username or password");
        }
    }
}
