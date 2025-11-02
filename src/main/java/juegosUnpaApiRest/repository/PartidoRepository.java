package juegosUnpaApiRest.repository;

import juegosUnpaApiRest.model.Partido;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PartidoRepository extends JpaRepository<Partido, Long> {
    List<Partido> findByTorneoIdOrderByFechaAsc(Long torneoId);
}