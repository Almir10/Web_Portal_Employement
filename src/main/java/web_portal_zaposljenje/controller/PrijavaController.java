package web_portal_zaposljenje.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web_portal_zaposljenje.model.Prijava;
import web_portal_zaposljenje.service.IPrijavaService;

import java.util.List;

@RestController
@RequestMapping("application")
public class PrijavaController {

    @Autowired
    private IPrijavaService prijavaService;


    @PostMapping("/")
    public ResponseEntity<Prijava> createPrijava(@RequestParam Long developerId, @RequestParam Long oglasId) {
        Prijava novaPrijava = prijavaService.savePrijava(developerId, oglasId);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaPrijava);
    }


    @GetMapping("/developer/{developerId}")
    public ResponseEntity<List<Prijava>> getPrijavaZaDeveloper(@PathVariable Long developerId) {
        List<Prijava> prijave = prijavaService.findByDeveloperId(developerId);
        if (prijave.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(prijave);
    }


    @GetMapping("/job-posting/{oglasId}")
    public ResponseEntity<List<Prijava>> getPrijaveZaOglas(@PathVariable Long oglasId) {
        List<Prijava> prijave = prijavaService.findByOglasId(oglasId);
        if (prijave.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(prijave);
    }


    @PutMapping("/{prijavaId}")
    public ResponseEntity<Prijava> updateStatusPrijave(@PathVariable Long prijavaId, @RequestParam String noviStatus) {
        Prijava azuriranaPrijava = prijavaService.updatePrijavaStatus(prijavaId, noviStatus);
        return ResponseEntity.ok(azuriranaPrijava);
    }

    @DeleteMapping("/{prijavaId}")
    public ResponseEntity<Void> deletePrijava(@PathVariable Long prijavaId) {
        prijavaService.deleteById(prijavaId);
        return ResponseEntity.noContent().build();
    }

}
