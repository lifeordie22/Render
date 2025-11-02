package juegosUnpaApiRest.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "partidos")
public class Partido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "torneo_id")
    private Torneo torneo;

    @ManyToOne
    @JoinColumn(name = "equipo_local_id")
    private Equipo equipoLocal;

    @ManyToOne
    @JoinColumn(name = "equipo_visitante_id")
    private Equipo equipoVisitante;

    @Column(name = "goles_local")
    private Integer golesLocal;

    @Column(name = "goles_visitante")
    private Integer golesVisitante;

    private Integer ronda;

    private LocalDate fecha;

    @ManyToOne
    @JoinColumn(name = "ubicacion_id")
    private Ubicacion ubicacion;

    @Enumerated(EnumType.STRING)
    private EstadoPartido estado = EstadoPartido.PENDIENTE;

    public enum EstadoPartido {
        PENDIENTE, JUGADO, SUSPENDIDO
    }

    public Long getId() {
        return id;
    }

    public Torneo getTorneo() {
        return torneo;
    }

    public Equipo getEquipoLocal() {
        return equipoLocal;
    }

    public Equipo getEquipoVisitante() {
        return equipoVisitante;
    }

    public Integer getGolesLocal() {
        return golesLocal;
    }

    public Integer getGolesVisitante() {
        return golesVisitante;
    }

    public Integer getRonda() {
        return ronda;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public EstadoPartido getEstado() {
        return estado;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setTorneo(Torneo torneo) {
        this.torneo = torneo;
    }

    public void setEquipoLocal(Equipo equipoLocal) {
        this.equipoLocal = equipoLocal;
    }

    public void setEquipoVisitante(Equipo equipoVisitante) {
        this.equipoVisitante = equipoVisitante;
    }

    public void setGolesLocal(Integer golesLocal) {
        this.golesLocal = golesLocal;
    }

    public void setGolesVisitante(Integer golesVisitante) {
        this.golesVisitante = golesVisitante;
    }

    public void setRonda(Integer ronda) {
        this.ronda = ronda;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    public void setEstado(EstadoPartido estado) {
        this.estado = estado;
    }

    // Constructors
    public Partido() {}

    public Partido(Torneo torneo, Equipo equipoLocal, Equipo equipoVisitante,
                   Integer ronda, LocalDate fecha, Ubicacion ubicacion) {
        this.torneo = torneo;
        this.equipoLocal = equipoLocal;
        this.equipoVisitante = equipoVisitante;
        this.ronda = ronda;
        this.fecha = fecha;
        this.ubicacion = ubicacion;
        this.estado = EstadoPartido.PENDIENTE;
    }
}