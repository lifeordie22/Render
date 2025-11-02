package juegosUnpaApiRest.repository;

import juegosUnpaApiRest.model.Torneo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface TorneoRepository extends JpaRepository<Torneo, Long> {
    List<Torneo> findByDisciplinaId(Long disciplinaId);

    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM Torneo t WHERE t.nombre = :nombre")
    boolean existsByNombre(@Param("nombre") String nombre);
}