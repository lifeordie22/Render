package juegosUnpaApiRest.repository;

import juegosUnpaApiRest.model.Disciplina;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DisciplinaRepository extends JpaRepository<Disciplina, Long> {
}