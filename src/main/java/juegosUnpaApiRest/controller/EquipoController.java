package juegosUnpaApiRest.controller;

import juegosUnpaApiRest.model.Equipo;
import juegosUnpaApiRest.repository.EquipoRepository;
import juegosUnpaApiRest.repository.UnidadAcademicaRepository;
import juegosUnpaApiRest.repository.DisciplinaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/equipos")
@CrossOrigin(origins = "http://localhost:4200")
public class EquipoController {
    private final EquipoRepository equipoRepository;
    private final UnidadAcademicaRepository unidadAcademicaRepository;
    private final DisciplinaRepository disciplinaRepository;

    public EquipoController(
            EquipoRepository equipoRepository,
            UnidadAcademicaRepository unidadAcademicaRepository,
            DisciplinaRepository disciplinaRepository
    ) {
        this.equipoRepository = equipoRepository;
        this.unidadAcademicaRepository = unidadAcademicaRepository;
        this.disciplinaRepository = disciplinaRepository;
    }

    @GetMapping
    public List<Equipo> getAll() {
        return equipoRepository.findAll();
    }


    @PostMapping
    public ResponseEntity<?> create(@RequestBody Equipo equipo) {
        try {
            if (equipoRepository.existsByNombreAndUnidadAcademicaAndDisciplina(
                    equipo.getNombre(),
                    equipo.getUnidadAcademica().getId(),
                    equipo.getDisciplina().getId())) {
                return ResponseEntity.badRequest()
                        .body("Ya existe un equipo con el mismo nombre para esta unidad académica y disciplina");
            }
            // Validate unidad academica exists
            if (!unidadAcademicaRepository.existsById(equipo.getUnidadAcademica().getId())) {
                return ResponseEntity.badRequest().body("Unidad Académica no existe");
            }

            // Validate disciplina exists
            if (!disciplinaRepository.existsById(equipo.getDisciplina().getId())) {
                return ResponseEntity.badRequest().body("Disciplina no existe");
            }

            equipo.setCreadoEn(LocalDateTime.now());
            Equipo savedEquipo = equipoRepository.save(equipo);
            return ResponseEntity.ok(savedEquipo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear el equipo: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Equipo equipo) {
        try {
            return equipoRepository.findById(id)
                    .map(existingEquipo -> {
                        // Validate unidad academica exists
                        if (!unidadAcademicaRepository.existsById(equipo.getUnidadAcademica().getId())) {
                            return ResponseEntity.badRequest().body("Unidad Académica no existe");
                        }

                        // Validate disciplina exists
                        if (!disciplinaRepository.existsById(equipo.getDisciplina().getId())) {
                            return ResponseEntity.badRequest().body("Disciplina no existe");
                        }

                        existingEquipo.setNombre(equipo.getNombre());
                        existingEquipo.setUnidadAcademica(equipo.getUnidadAcademica());
                        existingEquipo.setDisciplina(equipo.getDisciplina());

                        return ResponseEntity.ok(equipoRepository.save(existingEquipo));
                    })
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar el equipo: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            if (!equipoRepository.existsById(id)) {
                return ResponseEntity.notFound().build();
            }
            equipoRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar el equipo: " + e.getMessage());
        }
    }
}