package web_portal_zaposljenje.service;

import web_portal_zaposljenje.model.Role;

import java.util.List;
import java.util.Optional;

public interface IRoleService {

    List<Role> findAllRoles();

    Optional<Role> findByRoleName(String roleName);

}
