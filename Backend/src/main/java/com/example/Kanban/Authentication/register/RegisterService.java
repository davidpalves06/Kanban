package com.example.Kanban.Authentication.register;

import com.example.Kanban.Authentication.AuthenticationRepository;
import com.example.Kanban.Authentication.AuthenticationUserDetails;
import com.example.Kanban.Authentication.dto.AuthResponseDTO;
import com.example.Kanban.Authentication.register.dto.RegisterDTO;
import com.example.Kanban.Authentication.register.validators.UserDetailsValidator;
import com.example.Kanban.Authentication.register.validators.UserDetailsValidatorImpl;
import com.example.Kanban.UserProfile.UserProfileController;
import com.example.Kanban.UserProfile.dto.UserProfileDTO;
import com.example.Kanban.Authentication.exception.AuthenticationException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RegisterService {
    public static final String REGISTER_SUCCESS_MESSAGE = "Account created successfully!";
    public static final String EMAIL_ALREADY_TAKEN = "Email already taken";
    private static final UserDetailsValidator validator = new UserDetailsValidatorImpl();

    private final AuthenticationRepository authenticationRepository;
    private final UserProfileController userProfileController;
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<AuthResponseDTO> register(RegisterDTO registerDTO) {
        try {
            throwIfUserDetailsAreNotValid(registerDTO);
            throwIfUserDetailsAreTaken(registerDTO);
            saveNewUser(registerDTO);
            return createRegisterResponse();
        } catch (AuthenticationException e) {
            return createUnsuccessfulRegisterResponse(e);
        }

    }

    private void throwIfUserDetailsAreNotValid(RegisterDTO registerDTO) throws AuthenticationException {
        validator.validateUsername(registerDTO.getUsername());
        validator.validateEmail(registerDTO.getEmail());
        validator.validatePassword(registerDTO.getPassword());
    }

    private void throwIfUserDetailsAreTaken(RegisterDTO registerDTO) throws AuthenticationException {
        if (authenticationRepository.existsByEmail(registerDTO.getEmail()))
            throw new AuthenticationException(EMAIL_ALREADY_TAKEN);
    }

    private void saveNewUser(RegisterDTO registerDTO) throws AuthenticationException {
        String userID = saveNewUserDetails(registerDTO);
        saveNewUserAccount(registerDTO,userID);
    }

    private String saveNewUserDetails(RegisterDTO registerDTO) {
        AuthenticationUserDetails userAccount = new AuthenticationUserDetails(registerDTO.getEmail(),
                passwordEncoder.encode(registerDTO.getPassword()));
        AuthenticationUserDetails insertedUserDetails = authenticationRepository.insert(userAccount);
        return insertedUserDetails.getId();
    }

    private void saveNewUserAccount(RegisterDTO registerDTO, String userID) throws AuthenticationException {
        UserProfileDTO userProfileDTO = new UserProfileDTO();
        userProfileDTO.setUsername(registerDTO.getUsername());
        userProfileDTO.setId(userID);

        ResponseEntity<String> createProfileResponse = userProfileController.createUserProfile(userProfileDTO);

        if (createProfileResponse.getStatusCode().is4xxClientError()) {
            throw new AuthenticationException("Could not create user profile!");
        }

    }

    private ResponseEntity<AuthResponseDTO> createRegisterResponse() {
        AuthResponseDTO authResponseDTO = createAuthResponseDTO(REGISTER_SUCCESS_MESSAGE,"Need to login.");
        return new ResponseEntity<>(authResponseDTO, HttpStatus.OK);
    }

    private ResponseEntity<AuthResponseDTO> createUnsuccessfulRegisterResponse(AuthenticationException e) {
        AuthResponseDTO authResponseDTO = createAuthResponseDTO(e.getMessage(),null);
        return new ResponseEntity<>(authResponseDTO, HttpStatus.CONFLICT);
    }

    private AuthResponseDTO createAuthResponseDTO(String message,String token) {
        AuthResponseDTO authResponseDTO = new AuthResponseDTO(message);
        authResponseDTO.setToken(token);
        return authResponseDTO;
    }
}
