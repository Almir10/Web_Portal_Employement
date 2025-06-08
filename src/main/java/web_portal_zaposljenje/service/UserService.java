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
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class UserService implements IUserService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IRoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User saveUser(User user, List<Long> roleIds) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));



        Set<Role> roles = roleIds.stream()
                .map(id -> roleRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Role with ID " + id + " not found")))
                .collect(Collectors.toSet());


        user.setRoles(roles);


        return userRepository.save(user);
    }

    @Override
    public User updateUserAdmin(Long id, User updatedUser, Long roleId) {

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User with ID " + id + " not found."));


        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setLastName(updatedUser.getLastName());


        if (updatedUser.getGithubLink() != null)
            existingUser.setGithubLink(updatedUser.getGithubLink());
        if (updatedUser.getLinkedinLink() != null)
            existingUser.setLinkedinLink(updatedUser.getLinkedinLink());
        if (updatedUser.getSummary() != null)
            existingUser.setSummary(updatedUser.getSummary());


        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }


        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role with ID " + roleId + " not found."));
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        existingUser.setRoles(roles);
        return userRepository.save(existingUser);
    }


    @Override
    public User updateUser(Long id, User updatedUser) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User with ID " + id + " not found."));


        if (updatedUser.getGithubLink() != null)
            existingUser.setGithubLink(updatedUser.getGithubLink());
        if (updatedUser.getLinkedinLink() != null)
            existingUser.setLinkedinLink(updatedUser.getLinkedinLink());
        if (updatedUser.getSummary() != null)
            existingUser.setSummary(updatedUser.getSummary());


        return userRepository.save(existingUser);
    }

    @Override
    public boolean promijeniPasswordValidacija(Long userId, String oldPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return false;
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return true;
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}