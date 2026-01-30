package com.tfxsoftware.memserver.auth;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.tfxsoftware.memserver.auth.dto.SignInResponse;
import com.tfxsoftware.memserver.auth.dto.SignUpDto;
import com.tfxsoftware.memserver.auth.dto.SignInDto;
import com.tfxsoftware.memserver.users.UserService;
import com.tfxsoftware.memserver.users.dto.CreateUserDto;
import com.tfxsoftware.memserver.users.User;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signUp(SignUpDto dto) {
        String email = dto.getEmail().trim().toLowerCase();
        String username = dto.getUsername().trim();

        if (userService.existsByEmail(email)){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
        }

        if (userService.existsByUsername(username)){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already in use");
        }

        String hashedPassword = passwordEncoder.encode(dto.getPassword());

        CreateUserDto createUserDto = new CreateUserDto(email, username , hashedPassword);

        try {
            userService.createUser(createUserDto);
        
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error!");
        }
    }

    public SignInResponse signIn(SignInDto dto){
        String email = dto.getEmail().trim().toLowerCase();
    
        User user;

        try {
            user = userService.getUserByEmail(email);
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }

        if (!passwordEncoder.matches(dto.getPassword(), user.getHashedPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }

        return new SignInResponse("teste");
    }
}
