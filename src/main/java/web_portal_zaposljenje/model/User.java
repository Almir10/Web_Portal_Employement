package web_portal_zaposljenje.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;


    @Column(name="email", unique = true, nullable = false, length=100)
    private String email;

    @Column(name="password", nullable = false, length = 100)
    private String password;

    @Column(name="first_name", nullable = false, length=20)
    private String firstName;

    @Column(name="last_name", nullable = false, length = 30)
    private String lastName;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "role_user",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )


    private Set<Role> roles = new HashSet<>();



}
