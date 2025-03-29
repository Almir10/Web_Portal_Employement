package web_portal_zaposljenje.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import web_portal_zaposljenje.model.Prijava;

import java.util.List;

public interface IPrijavaRepository extends JpaRepository<Prijava, Long> {


    List<Prijava> findByOglasId(Long oglasId);

    List<Prijava> findByDeveloperId(Long developerId);

    List<Prijava> findByOglasIdAndDeveloperId(Long oglasId, Long developerId);

}
