package web_portal_zaposljenje.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web_portal_zaposljenje.model.Oglas;
import web_portal_zaposljenje.model.Vjestina;
import web_portal_zaposljenje.repository.IOglasRepository;
import web_portal_zaposljenje.repository.IVjestinaRepository;

import java.util.HashSet;
import java.util.List;
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

}
