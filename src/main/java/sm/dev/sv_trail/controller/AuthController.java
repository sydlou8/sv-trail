package sm.dev.sv_trail.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import sm.dev.sv_trail.model.dto.request.LoginRequest;
import sm.dev.sv_trail.model.dto.request.RegisterRequest;
import sm.dev.sv_trail.model.dto.response.AuthResponse;
import sm.dev.sv_trail.service.AuthService;

@Tag(name = "Authentication", description = "Register a new account or log in to receive a JWT token. Include the token in all subsequent requests as: `Authorization: Bearer <token>`")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    
    @Operation(summary = "Register a new user",
        description = "Creates a new account. Returns a JWT token on success.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "User registered successfully"),
        @ApiResponse(responseCode = "400", description = "Username already exists or validation failed")
    })
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse register(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(examples = @ExampleObject(value = "{\"username\":\"johndoe\",\"email\":\"john@example.com\",\"password\":\"secret123\"}")))
        @RequestBody @Valid RegisterRequest request) {
        return authService.register(request);
    }

    @Operation(summary = "Login and receive a JWT token",
        description = "Authenticates an existing user. Copy the returned `token` and use it as `Authorization: Bearer <token>` in all game endpoints.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Login successful"),
        @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @PostMapping("/login")
    public AuthResponse login(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(examples = @ExampleObject(value = "{\"username\":\"johndoe\",\"password\":\"secret123\"}")))
        @RequestBody @Valid LoginRequest request) {
        return authService.login(request);
    }
}
