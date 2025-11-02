package juegosUnpaApiRest.model;



import jakarta.persistence.*;

@Entity
public class UnidadAcademica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String sede;

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getSede() { return sede; }
    public void setSede(String sede) { this.sede = sede; }
}
