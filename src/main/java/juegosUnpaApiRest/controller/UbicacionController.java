package juegosUnpaApiRest.controller;

import juegosUnpaApiRest.model.Ubicacion;
import juegosUnpaApiRest.repository.UbicacionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/ubicaciones")
@CrossOrigin(origins = "http://localhost:4200")
public class UbicacionController {
    private final UbicacionRepository ubicacionRepository;

    public UbicacionController(UbicacionRepository ubicacionRepository) {
        this.ubicacionRepository = ubicacionRepository;
    }

    @GetMapping
    public List<Ubicacion> getAll() {
        return ubicacionRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ubicacionRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Ubicacion ubicacion) {
        try {
            // Validar datos requeridos
            if (ubicacion.getNombre() == null || ubicacion.getNombre().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body("El nombre es requerido");
            }

            if (ubicacion.getLatitud() == null || ubicacion.getLongitud() == null) {
                return ResponseEntity.badRequest()
                        .body("Las coordenadas son requeridas");
            }

            // Validar ubicación duplicada
            if (ubicacionRepository.existsByNombre(ubicacion.getNombre())) {
                return ResponseEntity.badRequest()
                        .body("Ya existe una ubicación con ese nombre");
            }

            ubicacion.setCreadoEn(LocalDateTime.now());
            return ResponseEntity.ok(ubicacionRepository.save(ubicacion));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Error al crear la ubicación: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Ubicacion ubicacion) {
        try {
            return ubicacionRepository.findById(id)
                    .map(existingUbicacion -> {
                        existingUbicacion.setNombre(ubicacion.getNombre());
                        existingUbicacion.setDescripcion(ubicacion.getDescripcion());
                        existingUbicacion.setLatitud(ubicacion.getLatitud());
                        existingUbicacion.setLongitud(ubicacion.getLongitud());
                        return ResponseEntity.ok(ubicacionRepository.save(existingUbicacion));
                    })
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Error al actualizar la ubicación: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            if (!ubicacionRepository.existsById(id)) {
                return ResponseEntity.notFound().build();
            }
            ubicacionRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Error al eliminar la ubicación: " + e.getMessage());
        }
    }
}