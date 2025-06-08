package web_portal_zaposljenje.service;

import web_portal_zaposljenje.model.Prijava;

import java.util.List;
import java.util.Optional;

public interface IPrijavaService {

    Prijava savePrijava(Long developerId, Long oglasId);
    List<Prijava> findByDeveloperId(Long developerId);

    Optional<Prijava> findById(Long prijavaId);

    List<Prijava> findByOglasId(Long oglasId);
    Prijava updatePrijavaStatus(Long prijavaId, String newStatus);
    void deleteById(Long prijavaId);


    Optional<Prijava> findByOglasIdAndDeveloperEmail(Long oglasId, String email);

    List<Prijava> findAll();
}
