package web_portal_zaposlenje.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import web_portal_zaposlenje.model.Oglas;
import web_portal_zaposlenje.model.User;
import web_portal_zaposlenje.security.CustomUserDetails;
import web_portal_zaposlenje.service.IOglasService;
import web_portal_zaposlenje.service.IPrijavaService;
import web_portal_zaposlenje.service.IUserService;
import web_portal_zaposlenje.service.IVjestinaService;

import java.util.Collections;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IOglasService oglasService;

    @Autowired
    private IPrijavaService prijavaService;


    @Autowired
    private IVjestinaService vjestinaService;
    @GetMapping("/")
    public String showLandingPage(
            @RequestParam(required = false) String pozicija,
            @RequestParam(required = false) String lokacija,
            @RequestParam(required = false) String tip,
            @RequestParam(required = false) Double plata,
            @RequestParam(required = false) Long vjestinaId,
            Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        userService.dodajUserAtributeUModel(authentication, model);

        List<Oglas> oglasi;
        try {
            oglasi = oglasService.advancedSearch(pozicija, lokacija, tip, plata, vjestinaId);
        } catch (Exception e) {
            oglasi = List.of();
            model.addAttribute("error", "Greška pri pretrazi: " + e.getMessage());
            e.printStackTrace();
        }
        model.addAttribute("oglasi", oglasi);

        model.addAttribute("vjestine", vjestinaService.findAll());
        model.addAttribute("pozicija", pozicija == null ? "" : pozicija);
        model.addAttribute("lokacija", lokacija == null ? "" : lokacija);
        model.addAttribute("tip", tip == null ? "" : tip);
        model.addAttribute("plata", plata == null ? "" : plata);
        model.addAttribute("vjestinaId", vjestinaId);

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

    @GetMapping("/korisnik-detalji/{id}")
    public String prikaziKorisnikDetalje(@PathVariable Long id, Model model) {
        User korisnik = userService.findById(id).orElseThrow();
        model.addAttribute("korisnik", korisnik);
        model.addAttribute("profilePicture", korisnik.getProfilePicture());


        boolean isPoslodavac = korisnik.getRoles().stream()
                .anyMatch(role -> role.getRoleName().equalsIgnoreCase("POSLODAVAC"));
        model.addAttribute("isPoslodavac", isPoslodavac);

        if (isPoslodavac) {
            List<Oglas> oglasi = oglasService.findByPoslodavacId(korisnik.getId());
            model.addAttribute("oglasi", oglasi);
        } else {
            model.addAttribute("oglasi", Collections.emptyList());
        }


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            model.addAttribute("userName", userDetails.getUsername());

            boolean isDeveloper = userDetails.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_DEVELOPER"));
            boolean isPoslodavacNavbar = userDetails.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_POSLODAVAC"));

            model.addAttribute("isDeveloper", isDeveloper);
            model.addAttribute("isPoslodavacNavbar", isPoslodavacNavbar);
        }

        return "korisnikDetalji";
    }
    @GetMapping("/oglas/{oglasId}")
    public String prikaziOglasDetalje(@PathVariable Long oglasId, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Default vrijednosti za gosta
        String userName = null;
        boolean isDeveloper = false;
        boolean isPoslodavac = false;
        boolean isAdmin = false;
        String profilePicture = null;
        boolean alreadyApplied = false;

        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            userName = userDetails.getUsername();

            isDeveloper = userDetails.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_DEVELOPER"));
            isPoslodavac = userDetails.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_POSLODAVAC"));
            isAdmin = userDetails.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
            if (isDeveloper) {
                alreadyApplied = prijavaService.findByOglasIdAndDeveloperEmail(oglasId, userName).isPresent();
            }
        }
        model.addAttribute("userName", userName);
        model.addAttribute("isDeveloper", isDeveloper);
        model.addAttribute("isPoslodavac", isPoslodavac);
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("profilePicture", profilePicture);
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
