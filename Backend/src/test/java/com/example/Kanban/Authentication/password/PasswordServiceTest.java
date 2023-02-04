package com.example.Kanban.Authentication.password;

import com.example.Kanban.Authentication.AuthenticationRepository;
import com.example.Kanban.Authentication.AuthenticationUserDetails;
import com.example.Kanban.Authentication.dto.AuthResponseDTO;
import com.example.Kanban.Authentication.password.dto.PasswordDTO;
import com.example.Kanban.Authentication.register.RegisterService;
import com.example.Kanban.Authentication.register.dto.RegisterDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PasswordServiceTest {
    private static final String TEST_EMAIL = "Teste@gmail.com";
    private static final String TEST_PASSWORD = "Password123";
    public static final String TEST_USER_ID = "1";
    public static final String OLD_PASSWORD = "OLD_PASSWORD";
    public static final String NEW_PASSWORD = "New_PASSWORD123";
    @Mock
    AuthenticationRepository authenticationRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @InjectMocks
    PasswordService passwordService;

    @Test
    public void changePasswordWithSuccess() {
        PasswordDTO passwordDTO = createPasswordDTO(OLD_PASSWORD, NEW_PASSWORD);
        AuthenticationUserDetails mockUser = new AuthenticationUserDetails(TEST_EMAIL, TEST_PASSWORD);
        when(authenticationRepository.existsById(TEST_USER_ID)).thenReturn(true);
        when(authenticationRepository.findById(TEST_USER_ID)).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(OLD_PASSWORD,TEST_PASSWORD)).thenReturn(true);
        ResponseEntity<AuthResponseDTO> passwordResponse = passwordService.changePassword(passwordDTO, TEST_USER_ID);

        assertNotNull(passwordResponse.getBody());
        assertEquals(PasswordService.PASSWORD_CHANGED_SUCESSFULLY,passwordResponse.getBody().getMessage());
        assertNull(passwordResponse.getBody().getToken());
        assertEquals(HttpStatus.OK,passwordResponse.getStatusCode());
    }

    @Test
    public void changePasswordShouldFailUserNotFound() {
        PasswordDTO passwordDTO = createPasswordDTO(OLD_PASSWORD, NEW_PASSWORD);
        when(authenticationRepository.existsById(TEST_USER_ID)).thenReturn(false);
        ResponseEntity<AuthResponseDTO> passwordResponse = passwordService.changePassword(passwordDTO, TEST_USER_ID);

        assertNotNull(passwordResponse.getBody());
        assertEquals(PasswordService.USER_NOT_FOUND,passwordResponse.getBody().getMessage());
        assertNull(passwordResponse.getBody().getToken());
        assertEquals(HttpStatus.NOT_FOUND,passwordResponse.getStatusCode());
    }

    @Test
    public void changePasswordShouldFailOldPasswordNotEqual() {
        PasswordDTO passwordDTO = createPasswordDTO(OLD_PASSWORD, NEW_PASSWORD);
        AuthenticationUserDetails mockUser = new AuthenticationUserDetails(TEST_EMAIL, TEST_PASSWORD);
        when(authenticationRepository.existsById(TEST_USER_ID)).thenReturn(true);
        when(authenticationRepository.findById(TEST_USER_ID)).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(OLD_PASSWORD,TEST_PASSWORD)).thenReturn(false);
        ResponseEntity<AuthResponseDTO> passwordResponse = passwordService.changePassword(passwordDTO, TEST_USER_ID);

        assertNotNull(passwordResponse.getBody());
        assertEquals(PasswordService.OLD_PASSWORD_IS_WRONG,passwordResponse.getBody().getMessage());
        assertNull(passwordResponse.getBody().getToken());
        assertEquals(HttpStatus.CONFLICT,passwordResponse.getStatusCode());
    }

    @Test
    public void changePasswordShouldFailNewPasswordNotValid() {
        PasswordDTO passwordDTO = createPasswordDTO(OLD_PASSWORD, "NOTVALID");
        AuthenticationUserDetails mockUser = new AuthenticationUserDetails(TEST_EMAIL, TEST_PASSWORD);
        when(authenticationRepository.existsById(TEST_USER_ID)).thenReturn(true);
        when(authenticationRepository.findById(TEST_USER_ID)).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(OLD_PASSWORD,TEST_PASSWORD)).thenReturn(true);
        ResponseEntity<AuthResponseDTO> passwordResponse = passwordService.changePassword(passwordDTO, TEST_USER_ID);

        assertNotNull(passwordResponse.getBody());
        assertEquals(PasswordService.NEW_PASSWORD_IS_NOT_VALID,passwordResponse.getBody().getMessage());
        assertNull(passwordResponse.getBody().getToken());
        assertEquals(HttpStatus.BAD_REQUEST,passwordResponse.getStatusCode());
    }

    private PasswordDTO createPasswordDTO(String oldPassword, String newPassword) {
        PasswordDTO passwordDTO = new PasswordDTO();
        passwordDTO.setOldPassword(oldPassword);
        passwordDTO.setNewPassword(newPassword);
        return passwordDTO;
    }


}