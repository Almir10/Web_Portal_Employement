package web_portal_zaposljenje.controller;


import org.springframework.ui.Model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import web_portal_zaposljenje.model.*;
import web_portal_zaposljenje.security.CustomUserDetails;
import web_portal_zaposljenje.service.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private IUserService userService;
    @Autowired
    private IOglasService oglasService;
    @Autowired
    private IPrijavaService prijavaService;
    @Autowired
    private IVjestinaService vjestinaService;
    @Autowired
    private IRoleService roleService;

    // Prikaz admin dashboarda (sve na jednoj stranici)
    @GetMapping("/dashboard")
    public String showAdminDashboard(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            model.addAttribute("userName", userDetails.getUsername());
            model.addAttribute("isAdmin", true);
        }


        List<User> users = userService.findAll();
        List<Oglas> oglasi = oglasService.findAll();
        List<Prijava> prijave = prijavaService.findAll();
        List<Vjestina> vjestine = vjestinaService.findAll();
        List<Role> roles = roleService.findAllRoles();

        model.addAttribute("brojKorisnika", users.size());
        model.addAttribute("brojOglasa", oglasi.size());
        model.addAttribute("brojPrijava", prijave.size());
        model.addAttribute("brojVjestina", vjestine.size());

        model.addAttribute("users", users);
        model.addAttribute("allRoles", roles);
        model.addAttribute("oglasi", oglasi);
        model.addAttribute("prijave", prijave);
        model.addAttribute("vjestine", vjestine);

        return "adminDashboard";
    }

    // ========================= KORISNICI =========================

    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return "redirect:/admin/dashboard#users";
    }

    @PostMapping("/users/edit/{id}")
    public String editUser(@PathVariable Long id, @ModelAttribute User user, @RequestParam("roleId") Long roleId) {
        userService.updateUserAdmin(id, user, roleId);
        return "redirect:/admin/dashboard#users";
    }

    @PostMapping("/users/remove-picture/{id}")
    public String removeUserPicture(@PathVariable Long id) {
        userService.removeProfilePicture(id); // Dodaj ovu metodu u servis
        return "redirect:/admin/dashboard#users";
    }

    // ========================= OGLASI =========================

    @PostMapping("/oglasi/delete/{id}")
    public String deleteOglas(@PathVariable Long id) {
        oglasService.deleteById(id);
        return "redirect:/admin/dashboard#oglasi";
    }

    @PostMapping("/oglasi/edit/{id}")
    public String editOglas(@PathVariable Long id, @ModelAttribute Oglas oglas) {
        oglasService.updateOglas(id, oglas);
        return "redirect:/admin/dashboard#oglasi";
    }

    // ========================= PRIJAVE =========================

    @PostMapping("/prijave/delete/{id}")
    public String deletePrijava(@PathVariable Long id) {
        prijavaService.deleteById(id);
        return "redirect:/admin/dashboard#prijave";
    }

    @PostMapping("/prijave/edit/{id}")
    public String editPrijava(@PathVariable Long id, @ModelAttribute Prijava prijava) {
        prijavaService.updatePrijavaStatus(id, prijava.getStatus());
        return "redirect:/admin/dashboard#prijave";
    }

    // ========================= VJEŠTINE =========================

    @PostMapping("/vjestine/delete/{id}")
    public String deleteVjestina(@PathVariable Long id) {
        vjestinaService.deleteById(id);
        return "redirect:/admin/dashboard#vjestine";
    }

    // Dodavanje i edit vještine (po potrebi)


    @PostMapping("/vjestine/add")
    public String addVjestina(@ModelAttribute Vjestina vjestina) {
        vjestinaService.saveVjestina(vjestina);
        return "redirect:/admin/dashboard#vjestine";
    }
}