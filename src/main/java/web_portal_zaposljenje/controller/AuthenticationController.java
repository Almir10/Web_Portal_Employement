package web_portal_zaposljenje.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import web_portal_zaposljenje.dto.RegistrationRequest;
import web_portal_zaposljenje.dto.LoginRequest;

import web_portal_zaposljenje.service.AuthenticationService;
import web_portal_zaposljenje.service.IAuthenticationService;

import javax.naming.AuthenticationException;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

    @Autowired
    private IAuthenticationService authenticationService;

    @PostMapping("register")
    public ResponseEntity<String> register(@RequestBody RegistrationRequest request) {
        try {
            authenticationService.register(request);
            return ResponseEntity.ok("Registracija uspješna.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) throws AuthenticationException {
        authenticationService.authenticate(request);
        return ResponseEntity.ok("Prijava uspješna.");
    }

}
