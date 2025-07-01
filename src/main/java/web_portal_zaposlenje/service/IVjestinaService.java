package web_portal_zaposlenje.service;

import web_portal_zaposlenje.model.Vjestina;

import java.util.List;
import java.util.Optional;

public interface IVjestinaService {

    Optional<Vjestina> findByNazivContaining(String naziv);

    List<Vjestina> findAll();

    void deleteById(Long id);

    void saveVjestina(Vjestina vjestina);
}
