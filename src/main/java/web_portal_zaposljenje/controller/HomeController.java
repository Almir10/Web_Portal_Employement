package web_portal_zaposljenje.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import web_portal_zaposljenje.security.CustomUserDetails;

@Controller
public class HomeController {

    @GetMapping("/")
    public String showLandingPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


        System.out.println("Authentication: " + authentication);

        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            model.addAttribute("userName", userDetails.getUsername());
        }

        return "landing";
    }
    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; // Returns login.html
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return "register"; // Returns register.html
    }
}
