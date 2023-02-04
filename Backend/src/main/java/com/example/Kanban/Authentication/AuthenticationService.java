package com.example.Kanban.Authentication;

import com.example.Kanban.Authentication.dto.AuthResponseDTO;
import com.example.Kanban.Authentication.login.dto.LoginDTO;
import com.example.Kanban.Authentication.password.PasswordService;
import com.example.Kanban.Authentication.password.dto.PasswordDTO;
import com.example.Kanban.Authentication.register.dto.RegisterDTO;
import com.example.Kanban.Authentication.login.LoginService;
import com.example.Kanban.Authentication.register.RegisterService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationService {
    private final RegisterService registerService;
    private final LoginService loginService;
    private final PasswordService passwordService;


    public ResponseEntity<AuthResponseDTO> register(RegisterDTO registerDTO) {
        return registerService.register(registerDTO);
    }


    public ResponseEntity<AuthResponseDTO> login(LoginDTO loginDTO) {
        return loginService.login(loginDTO);
    }

    public ResponseEntity<AuthResponseDTO> changePassword(PasswordDTO passwordDTO,String id) {
        return passwordService.changePassword(passwordDTO,id);
    }
}
