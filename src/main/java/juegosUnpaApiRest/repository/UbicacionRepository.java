package juegosUnpaApiRest.repository;

import juegosUnpaApiRest.model.Ubicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UbicacionRepository extends JpaRepository<Ubicacion, Long> {
    boolean existsByNombre(String nombre);
}