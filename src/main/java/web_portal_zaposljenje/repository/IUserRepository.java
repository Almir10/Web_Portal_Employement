package web_portal_zaposljenje.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web_portal_zaposljenje.model.User;

import java.util.Optional;

public interface IUserRepository extends JpaRepository<User, Long> {

    // Vec postojece metode Spring Hibernate:

    // - findAll() - dobija sve korisnike
    // - findById(id) - dobija korisnika po ID-u
    // - save(user) - čuva korisnika (ili pravi novog, ili ažurira postojeći)
    // - deleteById(id) - briše korisnika po ID-u

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);


}
