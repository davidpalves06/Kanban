package com.example.Kanban.Authentication;

import com.example.Kanban.Authentication.dto.AuthResponseDTO;
import com.example.Kanban.Authentication.login.dto.LoginDTO;
import com.example.Kanban.Authentication.password.dto.PasswordDTO;
import com.example.Kanban.Authentication.register.dto.RegisterDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthenticationController {

    private AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@RequestBody RegisterDTO registerDTO) {
        return authenticationService.register(registerDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginDTO loginDTO) {
        return authenticationService.login(loginDTO);
    }

    @PostMapping("/password/{id}")
    public ResponseEntity<AuthResponseDTO> changePassword(@RequestBody PasswordDTO passwordDTO,@PathVariable String id) {
        return authenticationService.changePassword(passwordDTO,id);
    }
}
