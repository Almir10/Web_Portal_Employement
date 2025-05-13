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
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("Korisnik sa emailom: " + email + " nije pronađen.");
        }

        User user = userOptional.get();

        System.out.println("Loaded password from DB: " + user.getPassword());
        return new CustomUserDetails(user);
    }

}
