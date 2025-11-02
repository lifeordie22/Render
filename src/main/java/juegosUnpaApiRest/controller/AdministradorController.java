package juegosUnpaApiRest.controller;

import juegosUnpaApiRest.model.Administrador;
import juegosUnpaApiRest.repository.AdministradorRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/administradores")
@CrossOrigin(origins = "http://localhost:4200")
public class AdministradorController {
    private final AdministradorRepository administradorRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AdministradorController(AdministradorRepository administradorRepository) {
        this.administradorRepository = administradorRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @GetMapping
    public List<Administrador> getAll() {
        return administradorRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Administrador administrador) {
        try {
            if (administradorRepository.existsByDni(administrador.getDni())) {
                return ResponseEntity.badRequest()
                        .body("Ya existe un administrador con ese DNI");
            }

            // Encrypt password
            administrador.setPassword(passwordEncoder.encode(administrador.getPassword()));
            administrador.setCreadoEn(LocalDateTime.now());

            return ResponseEntity.ok(administradorRepository.save(administrador));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Error al crear administrador: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Administrador administrador) {
        try {
            return administradorRepository.findById(id)
                    .map(existingAdmin -> {
                        // Update basic info
                        existingAdmin.setNombreCompleto(administrador.getNombreCompleto());
                        existingAdmin.setDni(administrador.getDni());

                        // Only update password if provided
                        if (administrador.getPassword() != null && !administrador.getPassword().isEmpty()) {
                            existingAdmin.setPassword(passwordEncoder.encode(administrador.getPassword()));
                        }

                        Administrador updated = administradorRepository.save(existingAdmin);
                        // Clear password before sending response
                        updated.setPassword(null);
                        return ResponseEntity.ok(updated);
                    })
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Error al actualizar administrador: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            if (!administradorRepository.existsById(id)) {
                return ResponseEntity.notFound().build();
            }
            administradorRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Error al eliminar administrador: " + e.getMessage());
        }
    }
}