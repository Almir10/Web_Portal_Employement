package web_portal_zaposlenje.service;

import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import web_portal_zaposlenje.dto.RegistrationRequest;
import web_portal_zaposlenje.model.User;

import java.util.List;
import java.util.Optional;


public interface IUserService {

    User register(RegistrationRequest request);

    User saveUser(User user, List<Long> roleIds);

    User updateUserAdmin(Long id, User updatedUser, Long roleId);


    User updateUser(Long id, User updatedUser, MultipartFile profilePicture);

    boolean promijeniPasswordValidacija(Long userId, String oldPassword, String newPassword);


    void removeProfilePicture(Long userId);

    void dodajUserAtributeUModel(Authentication authentication, Model model);

    Optional<User> findById(Long id);
    List<User> findAll();
    boolean existsById(Long id);
    void deleteById(Long id);
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
