package juegosUnpaApiRest.controller;

import juegosUnpaApiRest.model.Disciplina;
import juegosUnpaApiRest.repository.DisciplinaRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/disciplinas")
@CrossOrigin(origins = "*")
public class DisciplinaController {
    private final DisciplinaRepository repository;

    public DisciplinaController(DisciplinaRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Disciplina> getAll() {
        // Agrega un log para debug
        return repository.findAll();

    }


    @PostMapping
    public Disciplina create(@RequestBody Disciplina disciplina) {
        return repository.save(disciplina);
    }

    @PutMapping("/{id}")
    public Disciplina update(@PathVariable Long id, @RequestBody Disciplina disciplina) {
        disciplina.setId(id);
        return repository.save(disciplina);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }
}