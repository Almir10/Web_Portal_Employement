package web_portal_zaposljenje.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;


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


    public void setId(Long id) {
        this.id = id;
    }

    public void setPoslodavac(User poslodavac) {
        this.poslodavac = poslodavac;
    }

    public void setPozicija(String pozicija) {
        this.pozicija = pozicija;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public void setLokacija(String lokacija) {
        this.lokacija = lokacija;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public void setPlata(double plata) {
        this.plata = plata;
    }

    public void setVjestine(Set<Vjestina> vjestine) {
        this.vjestine = vjestine;
    }

    public Long getId() {
        return id;
    }

    public User getPoslodavac() {
        return poslodavac;
    }

    public String getPozicija() {
        return pozicija;
    }

    public String getTip() {
        return tip;
    }

    public String getLokacija() {
        return lokacija;
    }

    public String getOpis() {
        return opis;
    }

    public double getPlata() {
        return plata;
    }

    public Set<Vjestina> getVjestine() {
        return vjestine;
    }
}
