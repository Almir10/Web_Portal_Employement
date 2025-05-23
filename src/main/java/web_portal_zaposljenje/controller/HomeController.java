package web_portal_zaposljenje.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import web_portal_zaposljenje.model.Oglas;
import web_portal_zaposljenje.model.User;
import web_portal_zaposljenje.security.CustomUserDetails;
import web_portal_zaposljenje.service.IOglasService;
import web_portal_zaposljenje.service.IPrijavaService;
import web_portal_zaposljenje.service.IUserService;

import java.util.List;
import java.util.Optional;

@Controller
public class HomeController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IOglasService oglasService;

    @Autowired
    private IPrijavaService prijavaService;

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


        List<Oglas> oglasi = oglasService.findAll();
        model.addAttribute("oglasi", oglasi);

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

    @GetMapping("/oglas/{oglasId}")
    public String prikaziOglasDetalje(@PathVariable Long oglasId, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        boolean isDeveloper = false;
        boolean isPoslodavac = false;
        boolean alreadyApplied = false;

        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            model.addAttribute("userName", userDetails.getUsername());

            isDeveloper = userDetails.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_DEVELOPER"));
            isPoslodavac = userDetails.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_POSLODAVAC"));


            if (isDeveloper) {
                alreadyApplied = prijavaService.findByOglasIdAndDeveloperEmail(oglasId, userDetails.getUsername()).isPresent();
            }
        }

        model.addAttribute("isDeveloper", isDeveloper);
        model.addAttribute("isPoslodavac", isPoslodavac);
        model.addAttribute("alreadyApplied", alreadyApplied);

        Oglas oglas = oglasService.findById(oglasId)
                .orElseThrow(() -> new RuntimeException("Oglas nije pronađen."));
        model.addAttribute("oglas", oglas);

        List<Oglas> slicniOglasi = oglasService.findSlicniOglasi(oglas, 5);
        model.addAttribute("slicniOglasi", slicniOglasi);

        return "oglasDetalji";
    }

    @PostMapping("/prijava/{oglasId}")
    public String prijaviSeNaOglas(@PathVariable Long oglasId, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            return "redirect:/login";
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();


        String email = userDetails.getUsername();
        User developer = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Developer not found"));
        Long developerId = developer.getId();


        try {
            prijavaService.savePrijava(developerId, oglasId);
            redirectAttributes.addFlashAttribute("successMessage", "Uspješno ste se prijavili na oglas!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Greška prilikom prijave: " + e.getMessage());
        }


        return "redirect:/oglas/" + oglasId;
    }


}
