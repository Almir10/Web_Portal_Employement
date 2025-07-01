package web_portal_zaposlenje.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web_portal_zaposlenje.model.Vjestina;

import java.util.Optional;

public interface IVjestinaRepository extends JpaRepository<Vjestina, Long> {

    Optional<Vjestina> findByNazivContaining(String naziv);

}
