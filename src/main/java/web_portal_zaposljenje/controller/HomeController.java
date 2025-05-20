package web_portal_zaposljenje.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import web_portal_zaposljenje.model.Oglas;
import web_portal_zaposljenje.model.User;
import web_portal_zaposljenje.security.CustomUserDetails;
import web_portal_zaposljenje.service.IOglasService;
import web_portal_zaposljenje.service.IUserService;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IOglasService oglasService;

    @GetMapping("/")
    public String showLandingPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


        System.out.println("Authentication: " + authentication);

        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            model.addAttribute("userName", userDetails.getUsername());


            boolean isDeveloper = userDetails.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_DEVELOPER"));
            boolean isPoslodavac = userDetails.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_POSLODAVAC"));

            model.addAttribute("isDeveloper", isDeveloper);
            model.addAttribute("isPoslodavac", isPoslodavac);
        }

        return "landing";
    }
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }


    @GetMapping("/developerDashboard")
    public String showDeveloperDashboard() {
        return "developerDashboard"; // Returns developerDashboard.html
    }

    @GetMapping("/poslodavacDashboard")
    public String showPoslodavacDashboard(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User poslodavac = userService.findByEmail(userDetails.getUsername()).orElseThrow();

            List<Oglas> oglasi = oglasService.findByPoslodavacId(poslodavac.getId());
            model.addAttribute("oglasi", oglasi);

            model.addAttribute("userName", poslodavac.getFirstName());
            model.addAttribute("isPoslodavac", true);
        }
        return "poslodavacDashboard";
    }
}
