package web_portal_zaposljenje.service;

import web_portal_zaposljenje.model.Prijava;

import java.util.List;

public interface IPrijavaService {

    Prijava savePrijava(Long developerId, Long oglasId);
    List<Prijava> findByDeveloperId(Long developerId);
    List<Prijava> findByOglasId(Long oglasId);
    Prijava updatePrijavaStatus(Long prijavaId, String newStatus);
    void deleteById(Long prijavaId);

}
