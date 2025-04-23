package web_portal_zaposljenje.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web_portal_zaposljenje.model.Oglas;
import web_portal_zaposljenje.model.Vjestina;
import web_portal_zaposljenje.repository.IOglasRepository;
import web_portal_zaposljenje.repository.IVjestinaRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OglasService implements IOglasService{

    @Autowired
    private IOglasRepository oglasRepository;

    @Autowired
    private IVjestinaRepository vjestinaRepository;

    @Override
    public Oglas save(Oglas oglas) {
        // Use the vjestinaIds from the Oglas object itself
        Set<Long> vjestinaIds = oglas.getVjestine()
                .stream()
                .map(Vjestina::getId)
                .collect(Collectors.toSet());

        // Fetch the Vjestina objects from the database
        Set<Vjestina> vjestine = vjestinaIds.stream()
                .map(id -> vjestinaRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Vještina sa ID-em " + id + " nije pronađena.")))
                .collect(Collectors.toSet());

        // Set the Vjestine back to the Oglas object
        oglas.setVjestine(vjestine);

        // Save the Oglas object
        return oglasRepository.save(oglas);
    }

    public List<Oglas> advancedSearch(String pozicija, String lokacija, String tip, Double plata, Long vjestinaId) {
        return oglasRepository.advancedSearch(pozicija, lokacija, tip, plata, vjestinaId);
    }

    @Override
    public List<Oglas> findAll(){
        return oglasRepository.findAll();
    }

    @Override
    public void deleteById(Long id){
        oglasRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id){
        return oglasRepository.existsById(id);
    }

    @Override
    public Optional<Oglas> findById(Long id){
        return oglasRepository.findById(id);
    }


    @Override
    public Oglas updateOglas(Long id, Oglas updatedOglas) {
        Oglas existingOglas = oglasRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Oglas sa ID-em " + id + " nije pronađen."));


        existingOglas.setPozicija(updatedOglas.getPozicija());
        existingOglas.setTip(updatedOglas.getTip());
        existingOglas.setLokacija(updatedOglas.getLokacija());
        existingOglas.setOpis(updatedOglas.getOpis());
        existingOglas.setPlata(updatedOglas.getPlata());


        Set<Long> vjestinaIds = updatedOglas.getVjestine()
                .stream()
                .map(Vjestina::getId)
                .collect(Collectors.toSet());

        Set<Vjestina> vjestine = vjestinaIds.stream()
                .map(vjestinaId -> vjestinaRepository.findById(vjestinaId)
                        .orElseThrow(() -> new RuntimeException("Vještina sa ID-em " + vjestinaId + " nije pronađena.")))
                .collect(Collectors.toSet());

        existingOglas.setVjestine(vjestine);

        return oglasRepository.save(existingOglas);
    }


}
