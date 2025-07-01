package web_portal_zaposlenje.service;

import web_portal_zaposlenje.model.Role;

import java.util.List;
import java.util.Optional;

public interface IRoleService {

    List<Role> findAllRoles();

    Optional<Role> findByRoleName(String roleName);

}
