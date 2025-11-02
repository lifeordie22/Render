package juegosUnpaApiRest.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "torneos")
public class Torneo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "disciplina_id", nullable = false)
    private Disciplina disciplina;

    @Column(name = "formato_torneo")
    @Enumerated(EnumType.STRING)
    private FormatoTorneo formatoTorneo;

    private int rondas;

    @Column(name = "ida_vuelta")
    private boolean idaVuelta;

    @Column(name = "puntos_victoria")
    private int puntosVictoria = 3;

    @Column(name = "puntos_empate")
    private int puntosEmpate = 1;

    @Column(name = "puntos_derrota")
    private int puntosDerrota = 0;

    @Enumerated(EnumType.STRING)
    private EstadoTorneo estado = EstadoTorneo.PENDIENTE;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    @Column(name = "creado_en")
    private LocalDateTime creadoEn;

    @ManyToMany
    @JoinTable(
            name = "torneo_equipos",
            joinColumns = @JoinColumn(name = "torneo_id"),
            inverseJoinColumns = @JoinColumn(name = "equipo_id")
    )
    private Set<Equipo> equipos;

    public enum FormatoTorneo {
        LIGA, ELIMINACION_DIRECTA
    }

    public enum EstadoTorneo {
        PENDIENTE, EN_CURSO, FINALIZADO
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public FormatoTorneo getFormatoTorneo() {
        return formatoTorneo;
    }

    public int getRondas() {
        return rondas;
    }

    public boolean isIdaVuelta() {
        return idaVuelta;
    }

    public int getPuntosVictoria() {
        return puntosVictoria;
    }

    public int getPuntosEmpate() {
        return puntosEmpate;
    }

    public int getPuntosDerrota() {
        return puntosDerrota;
    }

    public EstadoTorneo getEstado() {
        return estado;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public LocalDateTime getCreadoEn() {
        return creadoEn;
    }

    public Set<Equipo> getEquipos() {
        return equipos;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }

    public void setFormatoTorneo(FormatoTorneo formatoTorneo) {
        this.formatoTorneo = formatoTorneo;
    }

    public void setRondas(int rondas) {
        this.rondas = rondas;
    }

    public void setIdaVuelta(boolean idaVuelta) {
        this.idaVuelta = idaVuelta;
    }

    public void setPuntosVictoria(int puntosVictoria) {
        this.puntosVictoria = puntosVictoria;
    }

    public void setPuntosEmpate(int puntosEmpate) {
        this.puntosEmpate = puntosEmpate;
    }

    public void setPuntosDerrota(int puntosDerrota) {
        this.puntosDerrota = puntosDerrota;
    }

    public void setEstado(EstadoTorneo estado) {
        this.estado = estado;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public void setCreadoEn(LocalDateTime creadoEn) {
        this.creadoEn = creadoEn;
    }

    public void setEquipos(Set<Equipo> equipos) {
        this.equipos = equipos;
    }

    // Constructor vacío requerido por JPA
    public Torneo() {}

    // Constructor con parámetros básicos
    public Torneo(String nombre, Disciplina disciplina, FormatoTorneo formatoTorneo,
                  LocalDate fechaInicio, LocalDate fechaFin) {
        this.nombre = nombre;
        this.disciplina = disciplina;
        this.formatoTorneo = formatoTorneo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = EstadoTorneo.PENDIENTE;
        this.creadoEn = LocalDateTime.now();
    }
}