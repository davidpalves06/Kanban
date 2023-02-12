package com.example.Kanban.Authentication;

import com.example.Kanban.Authentication.dto.AuthResponseDTO;
import com.example.Kanban.Authentication.login.LoginService;
import com.example.Kanban.Authentication.login.dto.LoginDTO;
import com.example.Kanban.Authentication.password.PasswordService;
import com.example.Kanban.Authentication.password.dto.PasswordDTO;
import com.example.Kanban.Authentication.register.RegisterService;
import com.example.Kanban.Authentication.register.dto.RegisterDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.Mockito.*;

import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@WebMvcTest(controllers = AuthenticationController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {
    private static final String TEST_USERNAME = "Teste123";
    private static final String TEST_EMAIL = "Teste@gmail.com";
    private static final String TEST_PASSWORD = "Password123";
    public static final String OLD_PASSWORD = "Password_123";
    public static final String NEW_PASSWORD = "Password_456";
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AuthenticationService authenticationService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void registerShouldSucceed() throws Exception {
        RegisterDTO registerDTO = createRegisterDTO(TEST_USERNAME,TEST_EMAIL,TEST_PASSWORD);
        when(authenticationService.register(ArgumentMatchers.any())).thenReturn(
                new ResponseEntity<AuthResponseDTO>(
                        createAuthResponseDTO(RegisterService.REGISTER_SUCCESS_MESSAGE,
                                "Need to login."), HttpStatus.OK));

        ResultActions response = mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerDTO)));

        byte[] contentAsByteArray = response.andReturn().getResponse().getContentAsByteArray();
        AuthResponseDTO authResponseDTO = objectMapper.readValue(contentAsByteArray, AuthResponseDTO.class);

        response.andExpect(MockMvcResultMatchers.status().isOk());
        assertEquals(RegisterService.REGISTER_SUCCESS_MESSAGE,authResponseDTO.getMessage());
        assertEquals("Need to login.",authResponseDTO.getToken());
    }

    @Test
    public void registerShouldFail() throws Exception {
        RegisterDTO registerDTO = createRegisterDTO(TEST_USERNAME,TEST_EMAIL,TEST_PASSWORD);
        when(authenticationService.register(ArgumentMatchers.any())).thenReturn(
                new ResponseEntity<>(
                        createAuthResponseDTO(RegisterService.EMAIL_ALREADY_TAKEN,
                                null), HttpStatus.CONFLICT));

        ResultActions response = mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerDTO)));

        byte[] contentAsByteArray = response.andReturn().getResponse().getContentAsByteArray();
        AuthResponseDTO authResponseDTO = objectMapper.readValue(contentAsByteArray, AuthResponseDTO.class);

        response.andExpect(MockMvcResultMatchers.status().isConflict());
        assertEquals(RegisterService.EMAIL_ALREADY_TAKEN,authResponseDTO.getMessage());
        assertNull(authResponseDTO.getToken());
    }

    @Test
    public void loginShouldSucceed() throws Exception {
        LoginDTO loginDTO = createLoginDTO(TEST_EMAIL,TEST_PASSWORD);
        when(authenticationService.login(ArgumentMatchers.any())).thenReturn(
                new ResponseEntity<>(
                        createAuthResponseDTO(LoginService.LOGIN_SUCCESS_MESSAGE,
                                "GeneratedToken"), HttpStatus.OK));

        ResultActions response = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDTO)));

        byte[] contentAsByteArray = response.andReturn().getResponse().getContentAsByteArray();
        AuthResponseDTO authResponseDTO = objectMapper.readValue(contentAsByteArray, AuthResponseDTO.class);

        response.andExpect(MockMvcResultMatchers.status().isOk());
        assertEquals(LoginService.LOGIN_SUCCESS_MESSAGE,authResponseDTO.getMessage());
        assertEquals("GeneratedToken",authResponseDTO.getToken());
    }

    @Test
    public void loginShouldFail() throws Exception {
        LoginDTO loginDTO = createLoginDTO(TEST_EMAIL,TEST_PASSWORD);
        when(authenticationService.login(ArgumentMatchers.any())).thenReturn(
                new ResponseEntity<>(
                        createAuthResponseDTO(LoginService.LOGIN_INSUCCESS_MESSAGE,
                                null), HttpStatus.CONFLICT));

        ResultActions response = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDTO)));

        byte[] contentAsByteArray = response.andReturn().getResponse().getContentAsByteArray();
        AuthResponseDTO authResponseDTO = objectMapper.readValue(contentAsByteArray, AuthResponseDTO.class);

        response.andExpect(MockMvcResultMatchers.status().isConflict());
        assertEquals(LoginService.LOGIN_INSUCCESS_MESSAGE,authResponseDTO.getMessage());
        assertNull(authResponseDTO.getToken());
    }

    @Test
    public void passwordChangeShouldSucceed() throws Exception {
        PasswordDTO passwordDTO = new PasswordDTO(OLD_PASSWORD, NEW_PASSWORD);
        when(authenticationService.changePassword(ArgumentMatchers.any(),any())).thenReturn(
                new ResponseEntity<>(
                        createAuthResponseDTO(PasswordService.PASSWORD_CHANGED_SUCESSFULLY,null), HttpStatus.OK));

        ResultActions response = mockMvc.perform(put("/auth/password/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(passwordDTO)));

        byte[] contentAsByteArray = response.andReturn().getResponse().getContentAsByteArray();
        AuthResponseDTO authResponseDTO = objectMapper.readValue(contentAsByteArray, AuthResponseDTO.class);

        response.andExpect(MockMvcResultMatchers.status().isOk());
        assertEquals(PasswordService.PASSWORD_CHANGED_SUCESSFULLY,authResponseDTO.getMessage());
    }

    @Test
    public void passwordChangeShouldFailOldPassword() throws Exception {
        PasswordDTO passwordDTO = new PasswordDTO(OLD_PASSWORD, NEW_PASSWORD);
        when(authenticationService.changePassword(ArgumentMatchers.any(),any())).thenReturn(
                new ResponseEntity<>(
                        createAuthResponseDTO(PasswordService.OLD_PASSWORD_IS_WRONG,null), HttpStatus.CONFLICT));

        ResultActions response = mockMvc.perform(put("/auth/password/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(passwordDTO)));

        byte[] contentAsByteArray = response.andReturn().getResponse().getContentAsByteArray();
        AuthResponseDTO authResponseDTO = objectMapper.readValue(contentAsByteArray, AuthResponseDTO.class);

        response.andExpect(MockMvcResultMatchers.status().isConflict());
        assertEquals(PasswordService.OLD_PASSWORD_IS_WRONG,authResponseDTO.getMessage());
    }

    @Test
    public void passwordChangeShouldFailInvalidNewPassword() throws Exception {
        PasswordDTO passwordDTO = new PasswordDTO(OLD_PASSWORD, NEW_PASSWORD);
        when(authenticationService.changePassword(ArgumentMatchers.any(),any())).thenReturn(
                new ResponseEntity<>(
                        createAuthResponseDTO(PasswordService.NEW_PASSWORD_IS_NOT_VALID,null), HttpStatus.BAD_REQUEST));

        ResultActions response = mockMvc.perform(put("/auth/password/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(passwordDTO)));

        byte[] contentAsByteArray = response.andReturn().getResponse().getContentAsByteArray();
        AuthResponseDTO authResponseDTO = objectMapper.readValue(contentAsByteArray, AuthResponseDTO.class);

        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
        assertEquals(PasswordService.NEW_PASSWORD_IS_NOT_VALID,authResponseDTO.getMessage());
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

    private AuthResponseDTO createAuthResponseDTO(String message,String token) {
        AuthResponseDTO authResponseDTO = new AuthResponseDTO(message);
        authResponseDTO.setToken(token);
        return authResponseDTO;
    }
}