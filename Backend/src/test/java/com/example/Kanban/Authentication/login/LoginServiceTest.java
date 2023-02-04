package com.example.Kanban.Authentication.login;

import com.example.Kanban.Authentication.dto.AuthResponseDTO;
import com.example.Kanban.Authentication.jwt.JwtGenerator;
import com.example.Kanban.Authentication.login.dto.LoginDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @Mock
    AuthenticationManager authenticationManager;
    @Mock
    JwtGenerator jwtGenerator;
    @InjectMocks
    LoginService loginService;

    @Test
    public void loginShouldSucceed() {
        LoginDTO loginDTO = createLoginDTO("Teste@gmail.com", "Password123");
        TestingAuthenticationToken authenticationToken = new TestingAuthenticationToken(null, null, List.of());
        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(),loginDTO.getPassword())))
                .thenReturn(authenticationToken);
        when(jwtGenerator.generateToken(authenticationToken)).thenReturn("TOKEN");
        ResponseEntity<AuthResponseDTO> loginResponse = loginService.login(loginDTO);

        assertNotNull(loginResponse.getBody());
        assertEquals(LoginService.LOGIN_SUCCESS_MESSAGE,loginResponse.getBody().getMessage());
        assertEquals("TOKEN",loginResponse.getBody().getToken());
        assertEquals(HttpStatus.OK,loginResponse.getStatusCode());

    }

    @Test
    public void loginShouldFail() {
        LoginDTO loginDTO = createLoginDTO("Teste123@gmail.com", "Password");
        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(),loginDTO.getPassword())))
                .thenThrow(BadCredentialsException.class);
        ResponseEntity<AuthResponseDTO> loginResponse = loginService.login(loginDTO);

        assertNotNull(loginResponse.getBody());
        assertEquals(LoginService.LOGIN_INSUCCESS_MESSAGE,loginResponse.getBody().getMessage());
        assertNull(loginResponse.getBody().getToken());
        assertEquals(HttpStatus.UNAUTHORIZED,loginResponse.getStatusCode());

    }

    private LoginDTO createLoginDTO(String email, String password) {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail(email);
        loginDTO.setPassword(password);
        return loginDTO;
    }
}