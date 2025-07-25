package web_portal_zaposlenje.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import web_portal_zaposlenje.model.Oglas;
import web_portal_zaposlenje.model.User;
import web_portal_zaposlenje.model.Vjestina;
import web_portal_zaposlenje.security.CustomUserDetails;
import web_portal_zaposlenje.service.IOglasService;
import web_portal_zaposlenje.service.IUserService;
import web_portal_zaposlenje.service.IPrijavaService;
import web_portal_zaposlenje.service.IVjestinaService;
import  web_portal_zaposlenje.dto.PrijavaDTO;

import java.util.List;


@Controller
@RequestMapping("poslodavac")
public class PoslodavacController {


    @Autowired
    private IUserService userService;

    @Autowired
    private IOglasService oglasService;

    @Autowired
    private IPrijavaService prijavaService;

    @Autowired
    private IVjestinaService vjestinaService;


    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User poslodavac = userService.findByEmail(userDetails.getUsername()).orElseThrow();

            List<Oglas> oglasi = oglasService.findByPoslodavacId(poslodavac.getId());
            List<Vjestina> vjestine = vjestinaService.findAll();
            model.addAttribute("oglasi", oglasi);
            model.addAttribute("vjestine", vjestine);
            model.addAttribute("userName", poslodavac.getFirstName());
            model.addAttribute("isPoslodavac", true);
            model.addAttribute("poslodavac", poslodavac);
        }
        return "poslodavacDashboard";
    }


    @PostMapping("/oglas")
    public String createOglas(@ModelAttribute Oglas oglas, @RequestParam(value = "vjestine", required = false) List<Long> vjestinaIds) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User poslodavac = userService.findByEmail(authentication.getName()).orElseThrow();
        oglas.setPoslodavac(poslodavac);
        oglasService.save(oglas, vjestinaIds);
        return "redirect:/poslodavac/dashboard";
    }

    @GetMapping("/oglasi")
    @ResponseBody
    public List<Oglas> getOglasiForPoslodavac() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User poslodavac = userService.findByEmail(authentication.getName()).orElseThrow();
        return oglasService.findByPoslodavacId(poslodavac.getId());
    }


    @GetMapping("/oglasi/{oglasId}/uredi")
    public String showEditOglasForm(@PathVariable Long oglasId, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User poslodavac = userService.findByEmail(userDetails.getUsername()).orElseThrow();


        Oglas oglas = oglasService.findById(oglasId)
                .filter(o -> o.getPoslodavac().getId().equals(poslodavac.getId()))
                .orElseThrow(() -> new RuntimeException("Nemate pristup ovom oglasu!"));

        List<Vjestina> vjestine = vjestinaService.findAll();
        model.addAttribute("oglas", oglas);
        model.addAttribute("vjestine", vjestine);
        return "editOglas";
    }

    @PostMapping("/oglasi/{oglasId}")
    public String updateOglas(@PathVariable Long oglasId, @ModelAttribute Oglas updatedOglas) {
        oglasService.updateOglas(oglasId, updatedOglas);
        return "redirect:/poslodavac/dashboard";
    }


    @PostMapping("/oglasi/{oglasId}/obrisi")
    public String deleteOglas(@PathVariable Long oglasId) {
        oglasService.deleteById(oglasId);
        return "redirect:/poslodavac/dashboard";
    }



   @PostMapping("/edit-profile")
    public String updateProfile(
            @ModelAttribute("poslodavac") User updatedUser,
            @RequestParam(value = "profilePictureFile", required = false) MultipartFile profilePicture,
            RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User poslodavac = userService.findByEmail(userDetails.getUsername()).orElseThrow();


       try {
           userService.updateUser(poslodavac.getId(), updatedUser, profilePicture);
           redirectAttributes.addFlashAttribute("successMessage", "Profil uspješno ažuriran!");
       } catch (Exception e) {
           redirectAttributes.addFlashAttribute("errorMessage", "Greška prilikom snimanja slike: " + e.getMessage());
       }
       return "redirect:/poslodavac/dashboard#profil";
    }

    @PostMapping("/promijeni-sifru")
    public String promijeniSifru(@RequestParam String staraSifra,
                                 @RequestParam String novaSifra,
                                 @RequestParam String potvrdaSifre,
                                 RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User poslodavac = userService.findByEmail(userDetails.getUsername()).orElseThrow();


        if (!novaSifra.equals(potvrdaSifre)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Nove lozinke se ne podudaraju.");
            return "redirect:/poslodavac/dashboard#profil";
        }

        boolean uspjeh = userService.promijeniPasswordValidacija(poslodavac.getId(), staraSifra, novaSifra);

        if (!uspjeh) {
            redirectAttributes.addFlashAttribute("errorMessage", "Stara lozinka nije ispravna.");
            return "redirect:/poslodavac/dashboard#profil";
        }

        redirectAttributes.addFlashAttribute("successMessage", "Lozinka uspješno promijenjena.");
        return "redirect:/poslodavac/dashboard#profil";
    }


    @GetMapping("/api/oglasi/{oglasId}/prijave")
    @ResponseBody
    public List<PrijavaDTO> getPrijaveForOglas(@PathVariable Long oglasId) {
        return prijavaService.findByOglasId(oglasId)
                .stream()
                .map(PrijavaDTO::new)
                .toList();
    }


    @GetMapping("/api/developer/{developerId}")
    @ResponseBody
    public User getDeveloperDetails(@PathVariable Long developerId) {
        return userService.findById(developerId).orElseThrow(() -> new RuntimeException("Developer nije pronađen."));
    }


    @PostMapping("/prijave/{prijavaId}/status")
    @ResponseBody
    public String updatePrijavaStatus(@PathVariable Long prijavaId, @RequestParam String newStatus) {
        prijavaService.updatePrijavaStatus(prijavaId, newStatus);
        return "Status prijave je uspješno ažuriran.";
    }

}
