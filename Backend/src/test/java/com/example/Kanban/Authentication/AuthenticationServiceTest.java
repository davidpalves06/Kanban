package com.example.Kanban.Authentication;

import com.example.Kanban.Authentication.dto.AuthResponseDTO;
import com.example.Kanban.Authentication.dto.LoginDTO;
import com.example.Kanban.Authentication.dto.RegisterDTO;
import com.example.Kanban.Authentication.jwt.JwtGenerator;
import com.example.Kanban.UserAccount.UserAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

import static com.example.Kanban.Authentication.validators.UserDetailsValidationErrors.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthenticationServiceTest {
    AuthenticationService authenticationService;
    AuthenticationRepository authenticationRepository = mock(AuthenticationRepository.class);
    UserAccountRepository userAccountRepository = mock(UserAccountRepository.class);
    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);

    AuthenticationManager authenticationManager = mock(AuthenticationManager.class);

    JwtGenerator jwtGenerator = mock(JwtGenerator.class);

    @BeforeEach
    public void setup(){
        authenticationService = new AuthenticationService(authenticationRepository,userAccountRepository,bCryptPasswordEncoder,authenticationManager,jwtGenerator);
    }


    @Test
    public void registerWithSuccess() {
        RegisterDTO userAccountDTO = createRegisterDTO("Teste123", "Teste@gmail.com", "Password123");
        AuthenticationUserDetails mockUser = new AuthenticationUserDetails("Teste@gmail.com", "Password123");
        when(authenticationRepository.insert(any(AuthenticationUserDetails.class))).thenReturn(mockUser);
        ResponseEntity<AuthResponseDTO> registerResponse = authenticationService.register(userAccountDTO);

        assertNotNull(registerResponse.getBody());
        assertEquals(AuthenticationService.REGISTER_SUCCESS_MESSAGE,registerResponse.getBody().getMessage());
        assertEquals("TODO",registerResponse.getBody().getToken());
        assertEquals(HttpStatus.OK,registerResponse.getStatusCode());
    }

    @Test
    public void registerWithoutSuccessInvalidUsername() {
        RegisterDTO userAccountDTO = createRegisterDTO("Test", "Teste@gmail.com", "Password123");
        ResponseEntity<AuthResponseDTO> registerResponse = authenticationService.register(userAccountDTO);

        assertNotNull(registerResponse.getBody());
        assertEquals(INVALID_USERNAME.toString(),registerResponse.getBody().getMessage());
        assertNull(registerResponse.getBody().getToken());
        assertEquals(HttpStatus.BAD_REQUEST,registerResponse.getStatusCode());
    }

    @Test
    public void registerWithoutSuccessInvalidEmail() {
        RegisterDTO userAccountDTO = createRegisterDTO("Teste123", "Teste.gmail.com", "Password123");
        ResponseEntity<AuthResponseDTO> registerResponse = authenticationService.register(userAccountDTO);

        assertNotNull(registerResponse.getBody());
        assertEquals(INVALID_EMAIL.toString(),registerResponse.getBody().getMessage());
        assertNull(registerResponse.getBody().getToken());
        assertEquals(HttpStatus.BAD_REQUEST,registerResponse.getStatusCode());
    }

    @Test
    public void registerWithoutSuccessInvalidPassword() {
        RegisterDTO userAccountDTO = createRegisterDTO("Teste123", "Teste@gmail.com", "password123");
        ResponseEntity<AuthResponseDTO> registerResponse = authenticationService.register(userAccountDTO);

        assertNotNull(registerResponse.getBody());
        assertEquals(INVALID_PASSWORD.toString(),registerResponse.getBody().getMessage());
        assertNull(registerResponse.getBody().getToken());
        assertEquals(HttpStatus.BAD_REQUEST,registerResponse.getStatusCode());
    }

    @Test
    public void registerWithoutSuccessEmailAlreadyTaken() {
        when(authenticationRepository.existsByEmail("Teste@gmail.com")).thenReturn(true);
        RegisterDTO userAccountDTO = createRegisterDTO("Teste123", "Teste@gmail.com", "Password123");
        ResponseEntity<AuthResponseDTO> registerResponse = authenticationService.register(userAccountDTO);

        assertNotNull(registerResponse.getBody());
        assertEquals(AuthenticationService.EMAIL_ALREADY_TAKEN,registerResponse.getBody().getMessage());
        assertNull(registerResponse.getBody().getToken());
        assertEquals(HttpStatus.BAD_REQUEST,registerResponse.getStatusCode());
    }

    @Test
    public void loginShouldSucceed() {
        String encodedPassword = bCryptPasswordEncoder.encode("Password123");
        when(authenticationRepository.findByEmail("Teste@gmail.com"))
                .thenReturn(Optional.of(new AuthenticationUserDetails("Teste@gmail.com", encodedPassword)));
        LoginDTO loginDTO = createLoginDTO("Teste@gmail.com", "Password123");
        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(),loginDTO.getPassword())))
                .thenReturn(new TestingAuthenticationToken(null,null, List.of()));
        ResponseEntity<AuthResponseDTO> loginResponse = authenticationService.login(loginDTO);

        assertNotNull(loginResponse.getBody());
        assertEquals(AuthenticationService.LOGIN_SUCCESS_MESSAGE,loginResponse.getBody().getMessage());
        assertEquals("TODO",loginResponse.getBody().getToken());
        assertEquals(HttpStatus.OK,loginResponse.getStatusCode());

    }

    private RegisterDTO createRegisterDTO(String username, String email, String password) {
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setUsername(username);
        registerDTO.setEmail(email);
        registerDTO.setPassword(password);
        return registerDTO;
    }

    private LoginDTO createLoginDTO(String email, String password) {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail(email);
        loginDTO.setPassword(password);
        return loginDTO;
    }
}