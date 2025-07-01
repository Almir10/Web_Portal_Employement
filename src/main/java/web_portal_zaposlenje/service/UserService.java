package web_portal_zaposlenje.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import web_portal_zaposlenje.dto.RegistrationRequest;
import web_portal_zaposlenje.model.Role;
import web_portal_zaposlenje.model.User;
import web_portal_zaposlenje.model.Vjestina;
import web_portal_zaposlenje.repository.IRoleRepository;
import web_portal_zaposlenje.repository.IUserRepository;
import web_portal_zaposlenje.repository.IVjestinaRepository;
import web_portal_zaposlenje.security.CustomUserDetails;

import java.io.File;
import java.io.IOException;
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

    @Autowired
    private IVjestinaRepository vjestinaRepository;

    @Value("${app.upload.dir}")
    private String uploadDir;


    @Override
    public User register(RegistrationRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        Set<Role> roles = request.getRoleIds().stream()
                .map(id -> roleRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Role with ID " + id + " not found")))
                .collect(Collectors.toSet());

        user.setRoles(roles);

        return userRepository.save(user);
    }

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
    public User updateUser(Long id, User updatedUser, MultipartFile profilePicture) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User with ID " + id + " not found."));


        if (updatedUser.getGithubLink() != null)
            existingUser.setGithubLink(updatedUser.getGithubLink());
        if (updatedUser.getLinkedinLink() != null)
            existingUser.setLinkedinLink(updatedUser.getLinkedinLink());
        if (updatedUser.getSummary() != null)
            existingUser.setSummary(updatedUser.getSummary());


        if (profilePicture != null && !profilePicture.isEmpty()) {
            try {
                String originalFilename = profilePicture.getOriginalFilename();
                String extension = ".jpg";
                if (originalFilename != null && originalFilename.contains(".")) {
                    extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                }
                String fileName = "user_" + existingUser.getId() + "_" + System.currentTimeMillis() + extension;
                File dir = new File(uploadDir);
                if (!dir.exists()) dir.mkdirs();
                File dest = new File(dir, fileName);
                profilePicture.transferTo(dest);
                existingUser.setProfilePicture(fileName);
            } catch (IOException e) {
                throw new RuntimeException("Greška pri uploadu slike: " + e.getMessage());
            }
        }

        Set<Long> vjestinaIds = updatedUser.getVjestine()
                .stream()
                .map(Vjestina::getId)
                .collect(Collectors.toSet());

        Set<Vjestina> vjestine = vjestinaIds.stream()
                .map(vjestinaId -> vjestinaRepository.findById(vjestinaId)
                        .orElseThrow(() -> new RuntimeException("Vještina sa ID-em " + vjestinaId + " nije pronađena.")))
                .collect(Collectors.toSet());

        existingUser.setVjestine(vjestine);

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
    public void removeProfilePicture(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            user.setProfilePicture(null); // ili user.setProfilePicture("avatar.png");
            userRepository.save(user);
        }
    }

    @Override
    public void dodajUserAtributeUModel(Authentication authentication, Model model) {
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User user = findByEmail(userDetails.getUsername()).orElse(null);
            if (user != null) {
                model.addAttribute("userName", user.getFirstName() + (user.getLastName() != null ? " " + user.getLastName() : ""));
                model.addAttribute("profilePicture", user.getProfilePicture());
            } else {
                model.addAttribute("userName", userDetails.getUsername());
                model.addAttribute("profilePicture", null);
            }

            boolean isDeveloper = userDetails.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_DEVELOPER"));
            boolean isPoslodavac = userDetails.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_POSLODAVAC"));
            boolean isAdmin = userDetails.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

            model.addAttribute("isDeveloper", isDeveloper);
            model.addAttribute("isPoslodavac", isPoslodavac);
            model.addAttribute("isAdmin", isAdmin);
        }
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