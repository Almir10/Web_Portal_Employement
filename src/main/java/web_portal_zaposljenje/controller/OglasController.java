package web_portal_zaposljenje.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web_portal_zaposljenje.model.Oglas;
import web_portal_zaposljenje.service.IOglasService;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/job-posting")
public class OglasController  {

    @Autowired
    private IOglasService oglasService;

    @GetMapping("/")
    public ResponseEntity<List<Oglas>> getAllOglasi(){
        List<Oglas> oglasi = oglasService.findAllOglasi();
        return ResponseEntity.ok(oglasi);
    }


    @PostMapping("/")
    public ResponseEntity<Oglas> saveOglas(@RequestBody Oglas oglas, @RequestParam Set<Long> vjestinaIds){
        Oglas savedOglas = oglasService.save(oglas, vjestinaIds);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedOglas);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOglas(@PathVariable Long id){
        if (oglasService.existsById(id)){
            oglasService.deleteById(id);
            return ResponseEntity.noContent().build();
        }else {
            return ResponseEntity.notFound().build();
        }
    }
}
