package web_portal_zaposljenje.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web_portal_zaposljenje.model.Oglas;
import web_portal_zaposljenje.model.Prijava;
import web_portal_zaposljenje.model.User;
import web_portal_zaposljenje.repository.IOglasRepository;
import web_portal_zaposljenje.repository.IPrijavaRepository;
import web_portal_zaposljenje.repository.IUserRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class PrijavaService implements IPrijavaService {

    @Autowired
    private IPrijavaRepository prijavaRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IOglasRepository oglasRepository;

    @Override
    public Prijava savePrijava(Long developerId, Long oglasId) {
        User developer = userRepository.findById(developerId)
                .orElseThrow(() -> new RuntimeException("Developer not found"));
        Oglas oglas = oglasRepository.findById(oglasId)
                .orElseThrow(() -> new RuntimeException("Oglas not found"));

        Prijava prijava = new Prijava();
        prijava.setDeveloper(developer);
        prijava.setOglas(oglas);
        prijava.setDatumPrijave(LocalDate.now());
        prijava.setStatus("PENDING");

        return prijavaRepository.save(prijava);
    }

    @Override
    public List<Prijava> findByDeveloperId(Long developerId) {
        return prijavaRepository.findByDeveloperId(developerId);
    }

    @Override
    public List<Prijava> findByOglasId(Long oglasId) {
        return prijavaRepository.findByOglasId(oglasId);
    }

    @Override
    public Prijava updatePrijavaStatus(Long prijavaId, String newStatus) {
        Prijava prijava = prijavaRepository.findById(prijavaId)
                .orElseThrow(() -> new RuntimeException("Prijava not found"));
        prijava.setStatus(newStatus);
        return prijavaRepository.save(prijava);
    }

    @Override
    public void deleteById(Long prijavaId) {
        if (!prijavaRepository.existsById(prijavaId)) {
            throw new RuntimeException("Prijava not found");
        }
        prijavaRepository.deleteById(prijavaId);
    }
}