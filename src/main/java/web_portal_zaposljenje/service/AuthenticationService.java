package web_portal_zaposljenje.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import web_portal_zaposljenje.dto.LoginRequest;
import web_portal_zaposljenje.dto.RegistrationRequest;
import web_portal_zaposljenje.model.User;

@Service
public class AuthenticationService implements IAuthenticationService {


    @Autowired
    private IUserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public void register(RegistrationRequest request) {
        if (userService.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email već postoji.");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        userService.saveUser(user);
    }

    @Override
    public void authenticate(LoginRequest request) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                request.getEmail(), request.getPassword());
        Authentication authenticated = authenticationManager.authenticate(authentication);

        System.out.println("Authenticated User: " + authenticated.getPrincipal());

        SecurityContextHolder.getContext().setAuthentication(authenticated);

        System.out.println("SecurityContext: " + SecurityContextHolder.getContext().getAuthentication());
    }
}
