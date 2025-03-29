package web_portal_zaposljenje.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name="Oglas")
public class Oglas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @ManyToOne
    @JoinColumn(name = "poslodavac_id", nullable = false)
    private User poslodavac;

    @Column(name="pozicija", unique = true, nullable = false, length=100)
    private String pozicija;

    @Column(name="tip", unique = true, nullable = false, length=100)
    private String tip;

    @Column(name="lokacija", unique = true, nullable = false, length=100)
    private String lokacija;

    @Column(name="opis", unique = true, nullable = false, length=1000)
    private String opis;

    @Column(name="plata", unique = true, nullable = false, length=100)
    private double plata;

    @ManyToMany
    @JoinTable(
            name = "oglas_vjestina", // Naziv join tabele
            joinColumns = @JoinColumn(name = "oglas_id"), // FK ka Oglas
            inverseJoinColumns = @JoinColumn(name = "vjestina_id") // FK ka Vjestina
    )
    private Set<Vjestina> vjestine = new HashSet<>();


}
