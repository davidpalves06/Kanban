package com.example.Kanban.Authentication;

import com.example.Kanban.Authentication.dto.AuthResponseDTO;
import com.example.Kanban.Authentication.dto.LoginDTO;
import com.example.Kanban.Authentication.dto.RegisterDTO;
import com.example.Kanban.Authentication.jwt.JwtGenerator;
import com.example.Kanban.Authentication.validators.UserDetailsValidator;
import com.example.Kanban.Authentication.validators.UserDetailsValidatorImpl;
import com.example.Kanban.UserAccount.UserAccount;
import com.example.Kanban.UserAccount.UserAccountRepository;
import com.example.Kanban.UserAccount.exception.InvalidUserInfoException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationService {
    public static final String REGISTER_SUCCESS_MESSAGE = "Account created successfully!";
    public static final String LOGIN_SUCCESS_MESSAGE = "Login was successful!";
    public static final String EMAIL_ALREADY_TAKEN = "Email already taken";
    private static final UserDetailsValidator validator = new UserDetailsValidatorImpl();

    private final AuthenticationRepository authenticationRepository;
    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private JwtGenerator jwtGenerator;


    public ResponseEntity<AuthResponseDTO> register(RegisterDTO registerDTO) {
        try {
            throwIfUserDetailsAreNotValid(registerDTO);
            throwIfUserDetailsAreTaken(registerDTO);
            saveNewUser(registerDTO);
            return createRegisterResponse();
        } catch (InvalidUserInfoException e) {
            return createUnsuccessfulRegisterResponse(e);
        }

    }

    private void throwIfUserDetailsAreNotValid(RegisterDTO registerDTO) throws InvalidUserInfoException {
        validator.validateUsername(registerDTO.getUsername());
        validator.validateEmail(registerDTO.getEmail());
        validator.validatePassword(registerDTO.getPassword());
    }

    private void throwIfUserDetailsAreTaken(RegisterDTO registerDTO) throws InvalidUserInfoException {
        if (authenticationRepository.existsByEmail(registerDTO.getEmail()))
            throw new InvalidUserInfoException(EMAIL_ALREADY_TAKEN);
    }

    private void saveNewUser(RegisterDTO registerDTO) {
        saveNewUserDetails(registerDTO);
        saveNewUserAccount(registerDTO);
    }

    private void saveNewUserDetails(RegisterDTO registerDTO) {
        AuthenticationUserDetails userAccount = new AuthenticationUserDetails(registerDTO.getEmail(),
                passwordEncoder.encode(registerDTO.getPassword()));
        authenticationRepository.insert(userAccount);
    }

    private void saveNewUserAccount(RegisterDTO registerDTO) {
        UserAccount userAccount = new UserAccount(registerDTO.getUsername());
        userAccountRepository.insert(userAccount);
    }

    private ResponseEntity<AuthResponseDTO> createRegisterResponse() {
        AuthResponseDTO authResponseDTO = createAuthResponseDTO(REGISTER_SUCCESS_MESSAGE,"TODO");
        return new ResponseEntity<>(authResponseDTO, HttpStatus.OK);
    }

    private ResponseEntity<AuthResponseDTO> createUnsuccessfulRegisterResponse(InvalidUserInfoException e) {
        AuthResponseDTO authResponseDTO = createAuthResponseDTO(e.getMessage(),null);
        return new ResponseEntity<>(authResponseDTO, HttpStatus.BAD_REQUEST);
    }

    private AuthResponseDTO createAuthResponseDTO(String message,String token) {
        AuthResponseDTO authResponseDTO = new AuthResponseDTO(message);
        authResponseDTO.setToken(token);
        return authResponseDTO;
    }

    public ResponseEntity<AuthResponseDTO> login(LoginDTO loginDTO) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(),loginDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);
        AuthResponseDTO authResponseDTO = createAuthResponseDTO(LOGIN_SUCCESS_MESSAGE,token);
        return new ResponseEntity<>(authResponseDTO,HttpStatus.OK);
    }


}
