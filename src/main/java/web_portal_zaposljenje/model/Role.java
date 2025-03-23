package web_portal_zaposljenje.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="Role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="role_name", unique = true, nullable = false, length=100)
    private String roleName;

    @ManyToMany(mappedBy = "roles")  // 'roles' refers to the field in the User class
    private Set<User> users = new HashSet<>();


}
