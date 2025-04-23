package web_portal_zaposljenje.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Data
@Entity
@Table(name="Oglas")
public class Oglas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "poslodavac_id", nullable = false)
    private User poslodavac;

    @Column(name="pozicija", nullable = false, length=100)
    private String pozicija;

    @Column(name="tip", nullable = false, length=100)
    private String tip;

    @Column(name="lokacija", nullable = false, length=100)
    private String lokacija;

    @Column(name="opis", nullable = false, length=1000)
    private String opis;

    @Column(name="plata", nullable = false, length=100)
    private double plata;

    @ManyToMany
    @JoinTable(
            name = "oglas_vjestina", // Naziv join tabele
            joinColumns = @JoinColumn(name = "oglas_id"), // FK ka Oglas
            inverseJoinColumns = @JoinColumn(name = "vjestina_id") // FK ka Vjestina
    )
    private Set<Vjestina> vjestine = new HashSet<>();


}
