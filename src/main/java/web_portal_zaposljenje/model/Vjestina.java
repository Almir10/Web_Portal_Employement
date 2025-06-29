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

    @ManyToMany(mappedBy = "vjestine")
    private Set<User> users = new HashSet<>();

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public Set<Oglas> getOglasi() {
        return oglasi;
    }

    public void setOglasi(Set<Oglas> oglasi) {
        this.oglasi = oglasi;
    }
}
