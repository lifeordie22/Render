package juegosUnpaApiRest.repository;

import juegosUnpaApiRest.model.Equipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface EquipoRepository extends JpaRepository<Equipo, Long> {

    @Query("SELECT e FROM Equipo e WHERE e.disciplina.id = :disciplinaId")
    List<Equipo> findByDisciplinaId(@Param("disciplinaId") Long disciplinaId);


    @Query("SELECT COUNT(e) > 0 FROM Equipo e WHERE e.nombre = :nombre " +
            "AND e.unidadAcademica.id = :unidadAcademicaId " +
            "AND e.disciplina.id = :disciplinaId")
    boolean existsByNombreAndUnidadAcademicaAndDisciplina(
            @Param("nombre") String nombre,
            @Param("unidadAcademicaId") Long unidadAcademicaId,
            @Param("disciplinaId") Long disciplinaId
    );
}