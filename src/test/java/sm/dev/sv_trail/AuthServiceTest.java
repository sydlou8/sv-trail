package sm.dev.sv_trail;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import sm.dev.sv_trail.model.dto.request.RegisterRequest;
import sm.dev.sv_trail.repository.UserRepository;
import sm.dev.sv_trail.service.AuthService;
import sm.dev.sv_trail.service.JwtService;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock UserRepository userRepository;
    @Mock JwtService jwtService;
    @Mock PasswordEncoder passwordEncoder;
    @Mock AuthenticationManager authenticationManager;
    @InjectMocks AuthService authService;

    @Test
    void register_throwsOnDuplicateUsername() {
        when(userRepository.existsByUsername("taken")).thenReturn(true);

        assertThatThrownBy(() ->
            authService.register(new RegisterRequest("taken", "a@b.com", "password")))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Username already exists");
    }
}