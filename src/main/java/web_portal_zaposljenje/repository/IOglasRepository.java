package web_portal_zaposljenje.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web_portal_zaposljenje.model.Oglas;
import web_portal_zaposljenje.model.User;
import web_portal_zaposljenje.model.Vjestina;

import java.util.List;
import java.util.Set;

public interface IOglasRepository extends JpaRepository<Oglas, Long> {


    List<Oglas> findByPozicijaContaining(String pozicija);
    List<Oglas> findByLokacijaContaining(String lokacija);
    List<Oglas> findByTipContaining(String tip);
    List<Oglas> findByPlataGreaterThan(double plata);
    List<Oglas> findByPozicijaContainingAndLokacijaContaining(String pozicija, String lokacija);
    List<Oglas> findByPozicijaContainingAndTipContaining(String pozicija, String tip);
    List<Oglas> findByLokacijaContainingAndTipContaining(String lokacija, String tip);
    List<Oglas> findByPozicijaContainingAndLokacijaContainingAndTipContaining(String pozicija, String lokacija, String tip);

    List<Oglas> findByPozicijaAndTipAndPlataGreaterThanEqual(String pozicija, String tip, double plata);
}


