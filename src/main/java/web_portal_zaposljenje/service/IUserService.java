package web_portal_zaposljenje.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import web_portal_zaposljenje.model.User;
import web_portal_zaposljenje.repository.IUserRepository;

import java.util.List;
import java.util.Optional;


public interface IUserService {

    User saveUser(User user, List<Long> roleIds);

    User updateUserAdmin(Long id, User updatedUser, Long roleId);


    User updateUser(Long id, User updatedUser, MultipartFile profilePicture);

    boolean promijeniPasswordValidacija(Long userId, String oldPassword, String newPassword);


    void removeProfilePicture(Long userId);

    Optional<User> findById(Long id);
    List<User> findAll();
    boolean existsById(Long id);
    void deleteById(Long id);
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
