package juegosUnpaApiRest.controller;



import juegosUnpaApiRest.model.UnidadAcademica;
import juegosUnpaApiRest.repository.UnidadAcademicaRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/unidades-academicas")
@CrossOrigin(origins = "*")
public class UnidadAcademicaController {

    private final UnidadAcademicaRepository repository;

    public UnidadAcademicaController(UnidadAcademicaRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<UnidadAcademica> getAll() {
        return repository.findAll();
    }

    @PostMapping
    public UnidadAcademica create(@RequestBody UnidadAcademica unidad) {
        return repository.save(unidad);
    }

    @PutMapping("/{id}")
    public UnidadAcademica update(@PathVariable Long id, @RequestBody UnidadAcademica unidad) {
        unidad.setId(id);
        return repository.save(unidad);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
