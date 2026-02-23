package com.keepcar.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * KEEPCAR - Entidad Registro de Mantenimiento
 * Historial de intervenciones realizadas en cada vehículo.
 *
 * FIX: vehiculo debe tener @JsonIgnore porque es FetchType.LAZY.
 * Sin él, Jackson lanza LazyInitializationException al serializar
 * el historial fuera de la sesión JPA.
 */
@Entity
@Table(name = "registro_mantenimiento")
public class RegistroMantenimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // FIX BUG #1: @JsonIgnore obligatorio en relaciones LAZY
    // Jackson intentaba inicializar el proxy Hibernate fuera de la transacción → 500 error
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehiculo_id", nullable = false)
    private Vehiculo vehiculo;

    // Campo extra (no persistido) para incluir info básica del vehículo en el JSON
    @Transient
    private Long vehiculoId;
    @Transient
    private String vehiculoNombre;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_mantenimiento", nullable = false)
    private TipoMantenimiento tipoMantenimiento;

    @Column(length = 500)
    private String descripcion;

    @Column(name = "kilometraje_realizado")
    private Double kilometrajeRealizado;

    @Column(name = "fecha_mantenimiento")
    private LocalDateTime fechaMantenimiento;

    @Column
    private Double coste;

    @Column(name = "realizado_por")
    private String realizadoPor;

    //  ENUM - Tipo de Mantenimiento

    public enum TipoMantenimiento {
        CAMBIO_ACEITE("Cambio de Aceite"),
        CAMBIO_FRENOS("Cambio de Frenos"),
        REVISION_GENERAL("Revisión General"),
        CAMBIO_NEUMATICOS("Cambio de Neumáticos"),
        CAMBIO_FILTROS("Cambio de Filtros"),
        OTRO("Otro");

        private final String descripcion;

        TipoMantenimiento(String descripcion) {
            this.descripcion = descripcion;
        }

        public String getDescripcion() { return descripcion; }
    }

    @PostLoad
    @PostPersist
    protected void calcularTransient() {
        if (vehiculo != null) {
            this.vehiculoId     = vehiculo.getId();
            this.vehiculoNombre = vehiculo.getMarca() + " " + vehiculo.getModelo();
        }
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Vehiculo getVehiculo() { return vehiculo; }
    public void setVehiculo(Vehiculo vehiculo) { this.vehiculo = vehiculo; }

    public Long getVehiculoId() { return vehiculoId; }
    public String getVehiculoNombre() { return vehiculoNombre; }

    public TipoMantenimiento getTipoMantenimiento() { return tipoMantenimiento; }
    public void setTipoMantenimiento(TipoMantenimiento t) { this.tipoMantenimiento = t; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Double getKilometrajeRealizado() { return kilometrajeRealizado; }
    public void setKilometrajeRealizado(Double v) { this.kilometrajeRealizado = v; }

    public LocalDateTime getFechaMantenimiento() { return fechaMantenimiento; }
    public void setFechaMantenimiento(LocalDateTime v) { this.fechaMantenimiento = v; }

    public Double getCoste() { return coste; }
    public void setCoste(Double coste) { this.coste = coste; }

    public String getRealizadoPor() { return realizadoPor; }
    public void setRealizadoPor(String v) { this.realizadoPor = v; }
}
