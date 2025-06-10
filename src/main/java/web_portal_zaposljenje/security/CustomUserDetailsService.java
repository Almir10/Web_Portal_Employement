package web_portal_zaposljenje.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import web_portal_zaposljenje.repository.IUserRepository;
import web_portal_zaposljenje.model.User;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private IUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        Optional<User> userOptional;
        if ("admin".equalsIgnoreCase(usernameOrEmail)) {
            userOptional = userRepository.findByFirstName("admin");
        } else {
            userOptional = userRepository.findByEmail(usernameOrEmail);
        }
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("Korisnik nije pronađen.");
        }

        User user = userOptional.get();
        return new CustomUserDetails(user);
    }

}
