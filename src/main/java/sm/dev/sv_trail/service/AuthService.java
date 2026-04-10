package sm.dev.sv_trail.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import sm.dev.sv_trail.repository.UserRepository;
import sm.dev.sv_trail.model.dto.request.RegisterRequest;
import sm.dev.sv_trail.model.dto.request.LoginRequest;
import sm.dev.sv_trail.model.dto.response.AuthResponse;
import sm.dev.sv_trail.model.entity.User;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())){
            throw new IllegalArgumentException("Username already exists");  
        }
        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .build();

        userRepository.save(user);

        String token = jwtService.generateToken(user);
        return new AuthResponse(token, user.getUsername());
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.username(), 
                request.password()
            ));
        User user = userRepository.findByUsername(request.username())
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
        String token = jwtService.generateToken(user);
        return new AuthResponse(token, user.getUsername());
    }
}
