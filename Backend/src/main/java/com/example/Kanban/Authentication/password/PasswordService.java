package com.example.Kanban.Authentication.password;

import com.example.Kanban.Authentication.AuthenticationRepository;
import com.example.Kanban.Authentication.AuthenticationUserDetails;
import com.example.Kanban.Authentication.dto.AuthResponseDTO;
import com.example.Kanban.Authentication.password.dto.PasswordDTO;
import com.example.Kanban.Authentication.register.validators.UserDetailsValidator;
import com.example.Kanban.Authentication.register.validators.UserDetailsValidatorImpl;
import com.example.Kanban.Authentication.exception.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class PasswordService {

    private static final UserDetailsValidator validator = new UserDetailsValidatorImpl();
    public static final String USER_NOT_FOUND = "User not found!";
    public static final String OLD_PASSWORD_IS_WRONG = "Old Password is wrong!";
    public static final String NEW_PASSWORD_IS_NOT_VALID = "New password isn't valid!";
    public static final String PASSWORD_CHANGED_SUCESSFULLY = "Password changed sucessfully!";

    private final AuthenticationRepository authenticationRepository;
    private final PasswordEncoder passwordEncoder;

    public PasswordService(AuthenticationRepository authenticationRepository, PasswordEncoder passwordEncoder) {
        this.authenticationRepository = authenticationRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public ResponseEntity<AuthResponseDTO> changePassword(PasswordDTO passwordDTO, String userID) {
        if (!checkIfUserExists(userID))
            return userNotFoundResponse();

        AuthenticationUserDetails userDetails = getAuthenticationUserDetails(userID);

        if (!checkIfOldPasswordMatches(passwordDTO, userDetails)) {
            return passwordNotCompatibleResponse();
        }

        if (!checkIfNewPasswordIsValid(passwordDTO.getNewPassword())) {
            return newPasswordNotValidResponse();
        }

        String encodedNewPassword = passwordEncoder.encode(passwordDTO.getNewPassword());
        userDetails.setPassword(encodedNewPassword);

        authenticationRepository.save(userDetails);
        return passwordChangedResponse();

    }

    private boolean checkIfUserExists(String userID) {
        return authenticationRepository.existsById(userID);
    }

    private ResponseEntity<AuthResponseDTO> userNotFoundResponse() {
        return new ResponseEntity<>(createAuthResponseDTO(USER_NOT_FOUND), HttpStatus.NOT_FOUND);
    }

    private AuthenticationUserDetails getAuthenticationUserDetails(String userID) {
        AuthenticationUserDetails userDetails = authenticationRepository.findById(userID).get();
        return userDetails;
    }

    private boolean checkIfOldPasswordMatches(PasswordDTO passwordDTO, AuthenticationUserDetails userDetails) {
        boolean matches = passwordEncoder.matches(passwordDTO.getOldPassword(), userDetails.getPassword());
        return matches;
    }

    private ResponseEntity<AuthResponseDTO> passwordNotCompatibleResponse() {
        return new ResponseEntity<>(createAuthResponseDTO(OLD_PASSWORD_IS_WRONG), HttpStatus.CONFLICT);
    }

    private boolean checkIfNewPasswordIsValid(String newPassword) {
        try {
            validator.validatePassword(newPassword);
            return true;
        } catch (AuthenticationException e) {
            return false;
        }
    }

    private ResponseEntity<AuthResponseDTO> newPasswordNotValidResponse() {
        return new ResponseEntity<>(createAuthResponseDTO(NEW_PASSWORD_IS_NOT_VALID), HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<AuthResponseDTO> passwordChangedResponse() {
        return new ResponseEntity<>(createAuthResponseDTO(PASSWORD_CHANGED_SUCESSFULLY), HttpStatus.OK);
    }

    private AuthResponseDTO createAuthResponseDTO(String message) {
        AuthResponseDTO authResponseDTO = new AuthResponseDTO(message);
        return authResponseDTO;
    }

}
