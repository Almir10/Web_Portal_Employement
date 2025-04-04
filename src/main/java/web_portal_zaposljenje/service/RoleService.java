package web_portal_zaposljenje.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web_portal_zaposljenje.model.Role;
import web_portal_zaposljenje.repository.IRoleRepository;

import java.util.Optional;

@Service
public class RoleService implements IRoleService {

    @Autowired
    private IRoleRepository roleRepository;

    @Override
    public Optional<Role> findByRoleName(String roleName) {
        return roleRepository.findByRoleName(roleName);
    }
}
