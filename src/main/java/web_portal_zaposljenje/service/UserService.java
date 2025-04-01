package web_portal_zaposljenje.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import web_portal_zaposljenje.model.Role;
import web_portal_zaposljenje.model.User;
import web_portal_zaposljenje.repository.IRoleRepository;
import web_portal_zaposljenje.repository.IUserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
public class UserService implements IUserService{

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IRoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public User saveUser(User user, List<Long> roleIds){

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Set<Role> roles = new HashSet<>();


        for (Long roleId : roleIds) {
            Role role = roleRepository.findById(roleId)
                    .orElseThrow(() -> new RuntimeException("Role not found"));
            roles.add(role);
        }

        user.setRoles(roles);

        return userRepository.save(user);
    }


}
