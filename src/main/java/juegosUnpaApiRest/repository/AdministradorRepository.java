package juegosUnpaApiRest.repository;

import juegosUnpaApiRest.model.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdministradorRepository extends JpaRepository<Administrador, Long> {
    boolean existsByDni(String dni);
    Administrador findByDni(String dni);
}