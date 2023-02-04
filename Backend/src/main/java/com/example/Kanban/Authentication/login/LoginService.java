package com.example.Kanban.Authentication.login;

import com.example.Kanban.Authentication.dto.AuthResponseDTO;
import com.example.Kanban.Authentication.login.dto.LoginDTO;
import com.example.Kanban.Authentication.jwt.JwtGenerator;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LoginService {
    public static final String LOGIN_SUCCESS_MESSAGE = "Login was successful!";
    public static final String LOGIN_INSUCCESS_MESSAGE = "Bad credentials!";

    private final AuthenticationManager authenticationManager;
    private JwtGenerator jwtGenerator;

    public ResponseEntity<AuthResponseDTO> login(LoginDTO loginDTO) {
            Authentication authentication = determineAuthentication(loginDTO);

            if (authentication != null) {
                return finishLoginProcess(authentication);
            } else {
                return createLoginResponse(LOGIN_INSUCCESS_MESSAGE, null, HttpStatus.UNAUTHORIZED);
            }
    }

    private ResponseEntity<AuthResponseDTO> finishLoginProcess(Authentication authentication) {
        setSecurityContextAuthentication(authentication);
        String token = generateJWT(authentication);
        return createLoginResponse(LOGIN_SUCCESS_MESSAGE, token, HttpStatus.OK);
    }

    private Authentication determineAuthentication(LoginDTO loginDTO){
        try{
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));
            return authentication;
        } catch (AuthenticationException e) {
            return null;
        }
    }

    private void setSecurityContextAuthentication(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String generateJWT(Authentication authentication) {
        return jwtGenerator.generateToken(authentication);
    }

    private ResponseEntity<AuthResponseDTO> createLoginResponse(String loginSuccessMessage, String token, HttpStatus status) {
        AuthResponseDTO authResponseDTO = createAuthResponseDTO(loginSuccessMessage, token);
        return new ResponseEntity<>(authResponseDTO, status);
    }

    private AuthResponseDTO createAuthResponseDTO(String message,String token) {
        AuthResponseDTO authResponseDTO = new AuthResponseDTO(message);
        authResponseDTO.setToken(token);
        return authResponseDTO;
    }
}
