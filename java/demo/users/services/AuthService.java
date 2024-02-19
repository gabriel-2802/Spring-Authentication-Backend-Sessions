package demo.blog.services;

import demo.blog.dto.LoginDto;
import demo.blog.dto.RegisterDto;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<String> register(RegisterDto registerDto);

    ResponseEntity<String> login(LoginDto loginDto);
}
