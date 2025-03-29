package web_portal_zaposljenje.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name="Vjestina")
public class Vjestina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(name="naziv", unique = true, length = 20)
    private String naziv;

    @ManyToMany(mappedBy = "vjestine")
    private Set<Oglas> oglasi = new HashSet<>();

}
