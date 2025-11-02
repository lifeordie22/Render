package juegosUnpaApiRest.controller;

import juegosUnpaApiRest.model.Equipo;
import juegosUnpaApiRest.model.Partido;
import juegosUnpaApiRest.model.Torneo;
import juegosUnpaApiRest.repository.TorneoRepository;
import juegosUnpaApiRest.repository.EquipoRepository;
import juegosUnpaApiRest.repository.PartidoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/torneos")
@CrossOrigin(origins = "http://localhost:4200")
public class TorneoController {
    private final TorneoRepository torneoRepository;
    private final EquipoRepository equipoRepository;
    private final PartidoRepository partidoRepository;

    public TorneoController(TorneoRepository torneoRepository, EquipoRepository equipoRepository, PartidoRepository partidoRepository) {
        this.torneoRepository = torneoRepository;
        this.equipoRepository = equipoRepository;
        this.partidoRepository = partidoRepository;
    }

    @GetMapping
    public List<Torneo> getAll() {
        return torneoRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return torneoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/disciplinas/{disciplinaId}/equipos")
    public ResponseEntity<?> getEquiposByDisciplina(@PathVariable Long disciplinaId) {
        try {
            List<Equipo> equipos = equipoRepository.findByDisciplinaId(disciplinaId);
            if (equipos.isEmpty()) {
                return ResponseEntity.ok()
                        .body("No hay equipos disponibles para esta disciplina");
            }
            return ResponseEntity.ok(equipos);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Error al obtener equipos: " + e.getMessage());
        }
    }
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Torneo torneo) {
        try {
            // Validar que haya equipos
            if (torneo.getEquipos() == null || torneo.getEquipos().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body("Debe seleccionar equipos para el torneo");
            }

            // Validar mínimo de equipos
            if (torneo.getEquipos().size() < 2) {
                return ResponseEntity.badRequest()
                        .body("Se necesitan al menos 2 equipos para crear un torneo");
            }

            // Validar que el nombre no esté duplicado
            if (torneoRepository.existsByNombre(torneo.getNombre())) {
                return ResponseEntity.badRequest()
                        .body("Ya existe un torneo con ese nombre");
            }

            // Calculate rounds and save tournament
            if (torneo.getFormatoTorneo() == Torneo.FormatoTorneo.LIGA) {
                torneo.setRondas(torneo.getEquipos().size() - 1);
            } else {
                torneo.setRondas((int) Math.ceil(Math.log(torneo.getEquipos().size()) / Math.log(2)));
            }

            torneo.setCreadoEn(LocalDateTime.now());
            torneo.setEstado(Torneo.EstadoTorneo.PENDIENTE);

            Torneo nuevoTorneo = torneoRepository.save(torneo);

            // Generate fixture
            List<Partido> partidos = generarFixture(nuevoTorneo);
            partidoRepository.saveAll(partidos);

            return ResponseEntity.ok(nuevoTorneo);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Error al crear el torneo: " + e.getMessage());
        }

    }
        private List<Partido> generarFixture(Torneo torneo) {
            List<Partido> partidos = new ArrayList<>();
            List<Equipo> equipos = new ArrayList<>(torneo.getEquipos());
            LocalDate fechaActual = torneo.getFechaInicio();

            if (torneo.getFormatoTorneo() == Torneo.FormatoTorneo.LIGA) {
                // Round-robin algorithm
                int n = equipos.size();
                int rondas = torneo.getRondas();

                for (int ronda = 1; ronda <= rondas; ronda++) {
                    for (int i = 0; i < n - 1; i += 2) {
                        Partido partido = new Partido();
                        partido.setTorneo(torneo);
                        partido.setEquipoLocal(equipos.get(i));
                        partido.setEquipoVisitante(equipos.get(i + 1));
                        partido.setFecha(fechaActual);
                        partido.setRonda(ronda);
                        partido.setEstado(Partido.EstadoPartido.PENDIENTE);
                        partidos.add(partido);
                    }

                    // Rotate teams for next round
                    Equipo lastTeam = equipos.remove(equipos.size() - 1);
                    equipos.add(1, lastTeam);
                    fechaActual = fechaActual.plusDays(7); // One week between rounds
                }

                // Generate return matches if ida_vuelta is true
                if (torneo.isIdaVuelta()) {
                    int partidosIda = partidos.size();
                    for (int i = 0; i < partidosIda; i++) {
                        Partido partidaIda = partidos.get(i);
                        Partido partidaVuelta = new Partido();

                        partidaVuelta.setTorneo(torneo);
                        partidaVuelta.setEquipoLocal(partidaIda.getEquipoVisitante());
                        partidaVuelta.setEquipoVisitante(partidaIda.getEquipoLocal());
                        partidaVuelta.setFecha(fechaActual);
                        partidaVuelta.setRonda(partidaIda.getRonda() + rondas);
                        partidaVuelta.setEstado(Partido.EstadoPartido.PENDIENTE);

                        partidos.add(partidaVuelta);

                        if ((i + 1) % (n / 2) == 0) {
                            fechaActual = fechaActual.plusDays(7);
                        }
                    }
                }
            } else {
                // Knockout tournament
                int ronda = 1;
                List<Equipo> equiposRonda = new ArrayList<>(equipos);

                while (equiposRonda.size() > 1) {
                    List<Equipo> equiposSiguienteRonda = new ArrayList<>();

                    for (int i = 0; i < equiposRonda.size(); i += 2) {
                        if (i + 1 < equiposRonda.size()) {
                            Partido partido = new Partido();
                            partido.setTorneo(torneo);
                            partido.setEquipoLocal(equiposRonda.get(i));
                            partido.setEquipoVisitante(equiposRonda.get(i + 1));
                            partido.setFecha(fechaActual);
                            partido.setRonda(ronda);
                            partido.setEstado(Partido.EstadoPartido.PENDIENTE);
                            partidos.add(partido);
                        } else {
                            equiposSiguienteRonda.add(equiposRonda.get(i));
                        }
                    }

                    equiposRonda = equiposSiguienteRonda;
                    ronda++;
                    fechaActual = fechaActual.plusDays(7);
                }
            }

            return partidos;
        }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Torneo torneo) {
        try {
            return torneoRepository.findById(id)
                    .map(existingTorneo -> {
                        existingTorneo.setNombre(torneo.getNombre());
                        existingTorneo.setPuntosVictoria(torneo.getPuntosVictoria());
                        existingTorneo.setPuntosEmpate(torneo.getPuntosEmpate());
                        existingTorneo.setPuntosDerrota(torneo.getPuntosDerrota());
                        existingTorneo.setFechaInicio(torneo.getFechaInicio());
                        existingTorneo.setFechaFin(torneo.getFechaFin());
                        return ResponseEntity.ok(torneoRepository.save(existingTorneo));
                    })
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Error al actualizar el torneo: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            if (!torneoRepository.existsById(id)) {
                return ResponseEntity.notFound().build();
            }
            torneoRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Error al eliminar el torneo: " + e.getMessage());
        }
    }

    @GetMapping("/{torneoId}/fixture")
    public ResponseEntity<List<Partido>> getFixture(@PathVariable Long torneoId) {
        try {
            List<Partido> partidos = partidoRepository.findByTorneoIdOrderByFechaAsc(torneoId);
            return ResponseEntity.ok(partidos); // Always return an array, even if empty
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}