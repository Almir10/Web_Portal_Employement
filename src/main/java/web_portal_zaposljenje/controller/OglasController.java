package web_portal_zaposljenje.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web_portal_zaposljenje.model.Oglas;
import web_portal_zaposljenje.service.IOglasService;

import java.util.List;
import java.util.Optional;
import java.util.Set;
@RestController
@RequestMapping("job-posting")
public class OglasController {

    @Autowired
    private IOglasService oglasService;

    @GetMapping()
    public ResponseEntity<List<Oglas>> getAllOglasi() {
        List<Oglas> oglasi = oglasService.findAll();
        return ResponseEntity.ok(oglasi);
    }

    @PostMapping()
    public ResponseEntity<Oglas> saveOglas(@RequestBody Oglas oglas) {
        Oglas savedOglas = oglasService.save(oglas);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedOglas);
    }

    @GetMapping("{id}")
    public ResponseEntity<Oglas> getOglasById(@PathVariable Long id) {
        Optional<Oglas> oglas = oglasService.findById(id);
        return oglas.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteOglas(@PathVariable Long id) {
        if (oglasService.existsById(id)) {
            oglasService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<Oglas> updateOglas(
            @PathVariable Long id,
            @RequestBody Oglas oglasDetails) {
        try {
            Oglas updated = oglasService.updateOglas(id, oglasDetails);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("search")
    public ResponseEntity<List<Oglas>> searchOglasi(
            @RequestParam(required = false) String pozicija,
            @RequestParam(required = false) String lokacija,
            @RequestParam(required = false) String tip,
            @RequestParam(required = false) Double plata,
            @RequestParam(required = false) Long vjestinaId) {
        List<Oglas> oglasi = oglasService.advancedSearch(pozicija, lokacija, tip, plata, vjestinaId);
        return ResponseEntity.ok(oglasi);
    }
}