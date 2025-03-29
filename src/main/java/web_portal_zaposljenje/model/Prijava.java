package web_portal_zaposljenje.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name="Prijava")
public class Prijava {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "developer_id", nullable = false)
    private User developer;

    @ManyToOne
    @JoinColumn(name = "oglas_id", nullable = false)
    private Oglas oglas;

    @Column(name = "datum_prijave", nullable = false)
    private LocalDate datumPrijave;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

}
