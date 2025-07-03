package web_portal_zaposlenje.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import web_portal_zaposlenje.model.Prijava;
import web_portal_zaposlenje.model.User;
import web_portal_zaposlenje.security.CustomUserDetails;
import web_portal_zaposlenje.service.IPrijavaService;
import web_portal_zaposlenje.service.IUserService;
import web_portal_zaposlenje.service.IVjestinaService;

import java.util.List;

@Controller
@RequestMapping("/developer")
public class DeveloperController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IPrijavaService prijavaService;

    @Autowired
    private IVjestinaService vjestinaService;


    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User developer = userService.findByEmail(userDetails.getUsername()).orElseThrow();

            List<Prijava> prijave = prijavaService.findByDeveloperId(developer.getId());
            model.addAttribute("prijave", prijave);
            model.addAttribute("vjestine", vjestinaService.findAll());
            model.addAttribute("developer", developer);
            model.addAttribute("userName", developer.getFirstName());
            model.addAttribute("lastName", developer.getLastName());
            model.addAttribute("isDeveloper", true);
        } else {
            System.out.println("Nema autentikacije!");
        }
        return "developerDashboard";
    }


    @GetMapping("/edit-profile")
    public String editProfileForm(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User developer = userService.findByEmail(userDetails.getUsername()).orElseThrow();
        model.addAttribute("developer", developer);
        return "editDeveloper";
    }


    @PostMapping("/edit-profile")
    public String updateProfile(@ModelAttribute("developer") User updatedUser,
                                @RequestParam(value = "profilePictureFile", required = false) MultipartFile profilePicture,
                                RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User developer = userService.findByEmail(userDetails.getUsername()).orElseThrow();

        try {
            userService.updateUser(developer.getId(), updatedUser, profilePicture);
            redirectAttributes.addFlashAttribute("successMessage", "Profil uspješno ažuriran!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Greška prilikom snimanja slike: " + e.getMessage());
        }
        return "redirect:/developer/dashboard#profil";
    }

    @PostMapping("/promijeni-sifru")
    public String promijeniSifru(@RequestParam String staraSifra,
                                 @RequestParam String novaSifra,
                                 @RequestParam String potvrdaSifre,
                                 RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User developer = userService.findByEmail(userDetails.getUsername()).orElseThrow();

        if (!novaSifra.equals(potvrdaSifre)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Nove lozinke se ne podudaraju.");
            return "redirect:/developer/dashboard#profil";
        }

        boolean uspjeh = userService.promijeniPasswordValidacija(developer.getId(), staraSifra, novaSifra);

        if (!uspjeh) {
            redirectAttributes.addFlashAttribute("errorMessage", "Stara lozinka nije ispravna.");
            return "redirect:/developer/dashboard#profil";
        }

        redirectAttributes.addFlashAttribute("successMessage", "Lozinka uspješno promijenjena.");
        return "redirect:/developer/dashboard#profil";
    }


    @PostMapping("/remove-prijava/{prijavaId}")
    public String removePrijava(@PathVariable Long prijavaId, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User developer = userService.findByEmail(userDetails.getUsername()).orElseThrow();

        Prijava prijava = prijavaService.findById(prijavaId).orElseThrow();
        if (!prijava.getDeveloper().getId().equals(developer.getId())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Nemate pravo ukloniti ovu prijavu.");
            return "redirect:/developer/dashboard";
        }
        prijavaService.deleteById(prijavaId);
        redirectAttributes.addFlashAttribute("successMessage", "Prijava uspješno uklonjena.");
        return "redirect:/developer/dashboard";
    }



}
