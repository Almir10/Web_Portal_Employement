package web_portal_zaposljenje.service;

import web_portal_zaposljenje.model.Oglas;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface IOglasService {

    Oglas save(Oglas oglas, Set<Long> vjestinaIds);

    List<Oglas> advancedSearch(String pozicija, String lokacija, String tip, Double plata, Long vjestinaId);

    List<Oglas> findAllOglasi();

    void deleteById(Long id);

    boolean existsById(Long id);

    Optional<Oglas> findById(Long id);

    Oglas updateOglas(Long id, Oglas updatedOglas, Set<Long> vjestinaIds);


}
