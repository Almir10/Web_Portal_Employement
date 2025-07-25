package web_portal_zaposlenje.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web_portal_zaposlenje.model.Role;

import java.util.Optional;

public interface IRoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByRoleName(String roleName);

}
