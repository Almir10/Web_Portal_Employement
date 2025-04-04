package web_portal_zaposljenje.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import web_portal_zaposljenje.model.Oglas;
import web_portal_zaposljenje.model.User;
import web_portal_zaposljenje.model.Vjestina;

import java.util.List;
import java.util.Set;

public interface IOglasRepository extends JpaRepository<Oglas, Long> {


    @Query("SELECT o FROM Oglas o " +
            "LEFT JOIN o.vjestine v " +
            "WHERE (:pozicija IS NULL OR LOWER(o.pozicija) LIKE LOWER(CONCAT('%', :pozicija, '%'))) AND " +
            "(:lokacija IS NULL OR LOWER(o.lokacija) LIKE LOWER(CONCAT('%', :lokacija, '%'))) AND " +
            "(:tip IS NULL OR LOWER(o.tip) LIKE LOWER(CONCAT('%', :tip, '%'))) AND " +
            "(:plata IS NULL OR o.plata >= :plata) AND " +
            "(:vjestinaId IS NULL OR v.Id = :vjestinaId)")
    List<Oglas> advancedSearch(
            @Param("pozicija") String pozicija,
            @Param("lokacija") String lokacija,
            @Param("tip") String tip,
            @Param("plata") Double plata,
            @Param("vjestinaId") Long vjestinaId // id vještine, može biti null ako nije odabrana
    );

}


