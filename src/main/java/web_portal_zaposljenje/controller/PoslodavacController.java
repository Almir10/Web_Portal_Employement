package web_portal_zaposljenje.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import web_portal_zaposljenje.model.Oglas;
import web_portal_zaposljenje.model.Prijava;
import web_portal_zaposljenje.model.User;
import web_portal_zaposljenje.model.Vjestina;
import web_portal_zaposljenje.repository.IVjestinaRepository;
import web_portal_zaposljenje.security.CustomUserDetails;
import web_portal_zaposljenje.service.IOglasService;
import web_portal_zaposljenje.service.IUserService;
import web_portal_zaposljenje.service.IPrijavaService;
import web_portal_zaposljenje.service.IVjestinaService;

import java.util.List;
import java.util.Optional;


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

    // 1. Prikaz Dashboard-a
    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Ako koristiš CustomUserDetails, koristi:
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User poslodavac = userService.findByEmail(userDetails.getUsername()).orElseThrow();

            List<Oglas> oglasi = oglasService.findByPoslodavacId(poslodavac.getId());
            List<Vjestina> vjestine = vjestinaService.findAll();
            model.addAttribute("oglasi", oglasi);
            model.addAttribute("vjestine", vjestine);
            model.addAttribute("userName", poslodavac.getFirstName());
            model.addAttribute("isPoslodavac", true);
        }
        return "poslodavacDashboard";
    }

    // 2. Dodavanje novog oglasa
    @PostMapping("/oglas")
    public String createOglas(@ModelAttribute Oglas oglas, @RequestParam(value = "vjestine", required = false) List<Long> vjestinaIds) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User poslodavac = userService.findByEmail(authentication.getName()).orElseThrow();
        oglas.setPoslodavac(poslodavac);
        oglasService.save(oglas, vjestinaIds);  // PROSLEDJUJEŠ OBJEKAT + ID-ove
        return "redirect:/poslodavac/dashboard";
    }

    @GetMapping("/oglasi")
    @ResponseBody
    public List<Oglas> getOglasiForPoslodavac() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User poslodavac = userService.findByEmail(authentication.getName()).orElseThrow();
        return oglasService.findByPoslodavacId(poslodavac.getId());
    }

    // 3. Uređivanje oglasa
    @GetMapping("/oglasi/{oglasId}/uredi")
    public String showEditOglasForm(@PathVariable Long oglasId, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User poslodavac = userService.findByEmail(userDetails.getUsername()).orElseThrow();

        // Provjeri da li oglas pripada ovom poslodavcu (security na entitetu)
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

    /*
    // Prijave za oglas
    @GetMapping("/oglasi/{oglasId}/prijave")
    public String listaPrijava(@PathVariable Long oglasId, Model model) {
        List<Prijava> prijave = prijavaService.findByOglasId(oglasId);
        model.addAttribute("prijave", prijave);
        return "prijaveZaOglas";
    }

    // Detalji prijave i developera
    @GetMapping("/prijave/{prijavaId}")
    public String detaljiPrijave(@PathVariable Long prijavaId, Model model) {
        Prijava prijava = prijavaService.findById(prijavaId).orElseThrow();
        User developer = prijava.getDeveloper();
        model.addAttribute("prijava", prijava);
        model.addAttribute("developer", developer);
        return "prijavaDetalji";
    }

    @PostMapping("/prijave/{prijavaId}/status")
    public String promijeniStatus(@PathVariable Long prijavaId, @RequestParam String status) {
        prijavaService.updatePrijavaStatus(prijavaId, status);
        return "redirect:/poslodavac/prijave/" + prijavaId;
    }*/


    @GetMapping("/api/oglasi/{oglasId}/prijave")
    @ResponseBody
    public List<Prijava> getPrijaveForOglas(@PathVariable Long oglasId) {
        return prijavaService.findByOglasId(oglasId);
    }

    // API za detalje developera
    @GetMapping("/api/developer/{developerId}")
    @ResponseBody
    public User getDeveloperDetails(@PathVariable Long developerId) {
        return userService.findById(developerId).orElseThrow(() -> new RuntimeException("Developer nije pronađen."));
    }

    // Promjena statusa (ostaje kao POST)
    @PostMapping("/prijave/{prijavaId}/status")
    @ResponseBody
    public String updatePrijavaStatus(@PathVariable Long prijavaId, @RequestParam String newStatus) {
        prijavaService.updatePrijavaStatus(prijavaId, newStatus);
        return "Status prijave je uspješno ažuriran.";
    }

}
