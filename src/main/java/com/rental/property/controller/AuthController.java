package com.rental.property.controller;
import com.rental.property.dto.AuthRequestDTO;
import com.rental.property.dto.AuthResponseDTO;
import com.rental.property.dto.CustomUserDetails;
import com.rental.property.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    @PostMapping("/login")
    @Operation(security = {@SecurityRequirement(name = "" )})
    public ResponseEntity<?> login(@RequestBody AuthRequestDTO authRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            log.error("BadCredentialsException {}", e.getMessage());
            return ResponseEntity.status(401).body("Invalid username or password");
        } catch (Exception e) {
            log.error("Exception {}", e.getMessage());
            return ResponseEntity.status(500).body("Authentication error: " + e.getMessage());
        }
        final CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(authRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);
        String role =  userDetails.getAuthorities().stream().findFirst().map(GrantedAuthority::getAuthority).get();
        AuthResponseDTO authResponseDTO =  new AuthResponseDTO(userDetails.getUserId(),jwt,userDetails.getUsername(),
                userDetails.getEmail(),
                userDetails.getFirstName(),userDetails.getLastName(),role
        );
        return ResponseEntity.ok(authResponseDTO);
    }
}
