package com.example.Kanban.Authentication.register;

import com.example.Kanban.Authentication.AuthenticationRepository;
import com.example.Kanban.Authentication.AuthenticationUserDetails;
import com.example.Kanban.Authentication.dto.AuthResponseDTO;
import com.example.Kanban.Authentication.register.dto.RegisterDTO;
import com.example.Kanban.UserProfile.UserProfileController;
import com.example.Kanban.UserProfile.dto.UserProfileDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.example.Kanban.Authentication.register.validators.UserDetailsValidationErrors.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegisterServiceTest {
    private static final String TEST_USERNAME = "Teste123";
    private static final String TEST_EMAIL = "Teste@gmail.com";
    private static final String TEST_PASSWORD = "Password123";
    @Mock
    AuthenticationRepository authenticationRepository;
    @Mock
    UserProfileController userProfileController;
    @Mock
    PasswordEncoder passwordEncoder;
    @InjectMocks
    RegisterService registerService;

    @Test
    public void registerWithSuccess() {
        RegisterDTO registerDTO = createRegisterDTO(TEST_USERNAME, TEST_EMAIL, TEST_PASSWORD);
        UserProfileDTO userProfileDTO = new UserProfileDTO();
        userProfileDTO.setId("1");
        userProfileDTO.setUsername(TEST_USERNAME);

        AuthenticationUserDetails mockUser = new AuthenticationUserDetails(TEST_EMAIL, TEST_PASSWORD);
        mockUser.setId("1");

        when(authenticationRepository.insert(any(AuthenticationUserDetails.class))).thenReturn(mockUser);
        when(userProfileController.createUserProfile(userProfileDTO)).thenReturn(new ResponseEntity<>("",HttpStatus.OK));
        ResponseEntity<AuthResponseDTO> registerResponse = registerService.register(registerDTO);

        assertNotNull(registerResponse.getBody());
        assertEquals(RegisterService.REGISTER_SUCCESS_MESSAGE,registerResponse.getBody().getMessage());
        assertEquals("Need to login.",registerResponse.getBody().getToken());
        assertEquals(HttpStatus.OK,registerResponse.getStatusCode());
    }

    @Test
    public void registerWithoutSuccessFailedToCreateProfile() {
        RegisterDTO registerDTO = createRegisterDTO(TEST_USERNAME, TEST_EMAIL, TEST_PASSWORD);
        UserProfileDTO userProfileDTO = new UserProfileDTO();
        userProfileDTO.setId("1");
        userProfileDTO.setUsername(TEST_USERNAME);

        AuthenticationUserDetails mockUser = new AuthenticationUserDetails(TEST_EMAIL, TEST_PASSWORD);
        mockUser.setId("1");

        when(authenticationRepository.insert(any(AuthenticationUserDetails.class))).thenReturn(mockUser);
        when(userProfileController.createUserProfile(userProfileDTO)).thenReturn(new ResponseEntity<>("",HttpStatus.CONFLICT));
        ResponseEntity<AuthResponseDTO> registerResponse = registerService.register(registerDTO);

        assertNotNull(registerResponse.getBody());
        assertEquals("Could not create user profile!",registerResponse.getBody().getMessage());
        assertNull(registerResponse.getBody().getToken());
        assertEquals(HttpStatus.CONFLICT,registerResponse.getStatusCode());
    }

    @Test
    public void registerWithoutSuccessInvalidUsername() {
        RegisterDTO registerDTO = createRegisterDTO("Test", TEST_EMAIL, TEST_PASSWORD);
        ResponseEntity<AuthResponseDTO> registerResponse = registerService.register(registerDTO);

        assertNotNull(registerResponse.getBody());
        assertEquals(INVALID_USERNAME.toString(),registerResponse.getBody().getMessage());
        assertNull(registerResponse.getBody().getToken());
        assertEquals(HttpStatus.CONFLICT,registerResponse.getStatusCode());
    }

    @Test
    public void registerWithoutSuccessInvalidEmail() {
        RegisterDTO registerDTO = createRegisterDTO(TEST_USERNAME, "Teste.gmail.com", TEST_PASSWORD);
        ResponseEntity<AuthResponseDTO> registerResponse = registerService.register(registerDTO);

        assertNotNull(registerResponse.getBody());
        assertEquals(INVALID_EMAIL.toString(),registerResponse.getBody().getMessage());
        assertNull(registerResponse.getBody().getToken());
        assertEquals(HttpStatus.CONFLICT,registerResponse.getStatusCode());
    }

    @Test
    public void registerWithoutSuccessInvalidPassword() {
        RegisterDTO registerDTO = createRegisterDTO(TEST_USERNAME, TEST_EMAIL, "password123");
        ResponseEntity<AuthResponseDTO> registerResponse = registerService.register(registerDTO);

        assertNotNull(registerResponse.getBody());
        assertEquals(INVALID_PASSWORD.toString(),registerResponse.getBody().getMessage());
        assertNull(registerResponse.getBody().getToken());
        assertEquals(HttpStatus.CONFLICT,registerResponse.getStatusCode());
    }

    @Test
    public void registerWithoutSuccessEmailAlreadyTaken() {
        when(authenticationRepository.existsByEmail(TEST_EMAIL)).thenReturn(true);
        RegisterDTO registerDTO = createRegisterDTO(TEST_USERNAME, TEST_EMAIL, TEST_PASSWORD);
        ResponseEntity<AuthResponseDTO> registerResponse = registerService.register(registerDTO);

        assertNotNull(registerResponse.getBody());
        assertEquals(RegisterService.EMAIL_ALREADY_TAKEN,registerResponse.getBody().getMessage());
        assertNull(registerResponse.getBody().getToken());
        assertEquals(HttpStatus.CONFLICT,registerResponse.getStatusCode());
    }



    private RegisterDTO createRegisterDTO(String username, String email, String password) {
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setUsername(username);
        registerDTO.setEmail(email);
        registerDTO.setPassword(password);
        return registerDTO;
    }
}