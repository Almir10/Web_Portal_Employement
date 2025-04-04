package web_portal_zaposljenje.service;

import web_portal_zaposljenje.model.Role;

import java.util.Optional;

public interface IRoleService {

    Optional<Role> findByRoleName(String roleName);

}
