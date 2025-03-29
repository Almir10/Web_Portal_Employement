package web_portal_zaposljenje.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web_portal_zaposljenje.model.Vjestina;

import java.util.List;
import java.util.Optional;

public interface IVjestinaRepository extends JpaRepository<Vjestina, Long> {

    Optional<Vjestina> findByNazivContaining(String naziv);

}
