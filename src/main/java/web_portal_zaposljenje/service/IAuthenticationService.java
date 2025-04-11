package web_portal_zaposljenje.service;

import web_portal_zaposljenje.dto.LoginRequest;
import web_portal_zaposljenje.dto.RegistrationRequest;

public interface IAuthenticationService {
    void register(RegistrationRequest request);

    void authenticate(LoginRequest request);
}
