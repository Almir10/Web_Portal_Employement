package web_portal_zaposljenje.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web_portal_zaposljenje.model.Vjestina;
import web_portal_zaposljenje.repository.IVjestinaRepository;

import java.util.List;
import java.util.Optional;

@Service
public class VjestinaService implements IVjestinaService{

    @Autowired
    private IVjestinaRepository vjestinaRepository;


    @Override
    public Optional<Vjestina> findByNazivContaining(String naziv) {
        return vjestinaRepository.findByNazivContaining(naziv);
    }

    @Override
    public List<Vjestina> findAll(){
        return vjestinaRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        if (!vjestinaRepository.existsById(id)) {
            throw new RuntimeException("Vjestina with id " + id + " does not exist");
        }

        vjestinaRepository.deleteById(id);
    }

    @Override
    public void saveVjestina(Vjestina vjestina) {
        if (vjestina.getNaziv() == null || vjestina.getNaziv().isEmpty()) {
            throw new RuntimeException("Vjestina must have a valid name");
        }
        vjestinaRepository.save(vjestina);
    }
}
