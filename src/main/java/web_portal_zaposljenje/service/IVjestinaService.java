package web_portal_zaposljenje.service;

import web_portal_zaposljenje.model.Vjestina;

import java.util.Optional;

public interface IVjestinaService {

    Optional<Vjestina> findByNazivContaining(String naziv);
}
