package be.ddd.api.login.controller;

import be.ddd.api.dto.res.LoginDto;
import be.ddd.api.login.service.Auth0Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class LoginController {

    private final Auth0Service auth0Service;

    @GetMapping("/login")
    public ResponseEntity<LoginDto> login(@RequestParam String code) {
        LoginDto loginDto = auth0Service.loginWithCode(code);
        return ResponseEntity.ok(loginDto);
    }

}
