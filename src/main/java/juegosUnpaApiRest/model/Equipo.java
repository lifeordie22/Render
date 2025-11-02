package juegosUnpaApiRest.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "equipos")
public class Equipo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @ManyToOne
    @JoinColumn(name = "unidad_academica_id", nullable = false)
    private UnidadAcademica unidadAcademica;

    @ManyToOne
    @JoinColumn(name = "disciplina_id", nullable = false)
    private Disciplina disciplina;

    @Column(name = "creado_en")
    private LocalDateTime creadoEn;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public UnidadAcademica getUnidadAcademica() { return unidadAcademica; }
    public void setUnidadAcademica(UnidadAcademica unidadAcademica) {
        this.unidadAcademica = unidadAcademica;
    }

    public Disciplina getDisciplina() { return disciplina; }
    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }

    public LocalDateTime getCreadoEn() { return creadoEn; }
    public void setCreadoEn(LocalDateTime creadoEn) { this.creadoEn = creadoEn; }
}