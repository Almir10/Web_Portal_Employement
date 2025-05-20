package web_portal_zaposljenje.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**", "/styles.css").permitAll()



                        .requestMatchers( "/auth/*", "/auth/login", "/job-posting/*","/job-posting", "job-posting").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/user/**").hasRole("USER")
                        .requestMatchers("/developerDashboard").hasRole("DEVELOPER")
                        .requestMatchers("/poslodavacDashboard").hasRole("POSLODAVAC")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login") // Custom login page at /login
                        .loginProcessingUrl("/auth/login") // URL to submit the login form
                        .defaultSuccessUrl("/", true) // Redirect to home on successful login
                        .failureUrl("/login?error=true") // Redirect to login page on failure
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/auth/logout")       // Specify the logout URL
                        .logoutSuccessUrl("/")           // Redirect to homepage after logout
                        .invalidateHttpSession(true)     // Invalidate the session
                        .clearAuthentication(true)       // Clear the SecurityContext
                        .deleteCookies("JSESSIONID")     // Delete the session cookie
                        .permitAll()                     // Allow all users to access the logout endpoint
                ).sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // Enable session-based authentication
        );

        return http.build();
    }

}
