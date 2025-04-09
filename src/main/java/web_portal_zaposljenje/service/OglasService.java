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

@Service
public class OglasService implements IOglasService{

    @Autowired
    private IOglasRepository oglasRepository;

    @Autowired
    private IVjestinaRepository vjestinaRepository;

    @Override
    public Oglas save(Oglas oglas, Set<Long> vjestinaIds) {
        Set<Vjestina> vjestine = new HashSet<>();

        for (Long id : vjestinaIds) {
            Vjestina vjestina = vjestinaRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Vještina nije pronađena"));
            vjestine.add(vjestina);
        }

        oglas.setVjestine(vjestine);

        return oglasRepository.save(oglas);
    }

    public List<Oglas> advancedSearch(String pozicija, String lokacija, String tip, Double plata, Long vjestinaId) {
        return oglasRepository.advancedSearch(pozicija, lokacija, tip, plata, vjestinaId);
    }

    @Override
    public List<Oglas> findAllOglasi(){
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
    public Oglas updateOglas(Long id, Oglas updatedOglas, Set<Long> vjestinaIds) {
        Oglas existingOglas = oglasRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Oglas sa ID-em " + id + " nije pronađen."));


        existingOglas.setPozicija(updatedOglas.getPozicija());
        existingOglas.setTip(updatedOglas.getTip());
        existingOglas.setLokacija(updatedOglas.getLokacija());
        existingOglas.setOpis(updatedOglas.getOpis());
        existingOglas.setPlata(updatedOglas.getPlata());


        Set<Vjestina> vjestine = new HashSet<>();
        for (Long vjestinaId : vjestinaIds) {
            Vjestina v = vjestinaRepository.findById(vjestinaId)
                    .orElseThrow(() -> new RuntimeException("Vještina sa ID-em " + vjestinaId + " nije pronađena."));
            vjestine.add(v);
        }
        existingOglas.setVjestine(vjestine);

        return oglasRepository.save(existingOglas);
    }


}
