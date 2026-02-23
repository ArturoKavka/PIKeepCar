package com.keepcar.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "vehiculo")
public class Vehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String marca;

    @NotBlank
    @Column(nullable = false)
    private String modelo;

    @Column
    private Integer anio;

    @Column(unique = true)
    private String matricula;

    @NotNull
    @Column(name = "kilometraje_actual", nullable = false)
    private Double kilometrajeActual;

    @Column(name = "ultimo_cambio_aceite_km")
    private Double ultimoCambioAceiteKm;

    @Column(name = "proximo_cambio_aceite_km")
    private Double proximoCambioAceiteKm;

    // --- Especificaciones técnicas ---
    @Column(name = "motor_cc")
    private Integer motorCc;

    @Column(name = "potencia_cv")
    private Integer potenciaCv;

    @Column
    private String combustible;

    @Column
    private String transmision;

    @Column
    private String color;

    @Column(name = "num_puertas")
    private Integer numPuertas;

    @Column(name = "num_bastidor")
    private String numBastidor;

    @Column(name = "fecha_ultima_itv")
    private LocalDate fechaUltimaItv;

    @Column(name = "fecha_proxima_itv")
    private LocalDate fechaProximaItv;

    // --- Neumáticos ---
    @Column(name = "anio_fabricacion_neumaticos")
    private Integer anioFabricacionNeumaticos;

    @Column(name = "dibujo_neumatico_mm")
    private Double dibujoNeumaticoMm;

    // --- Propietario ---
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "propietario_id", nullable = false)
    @JsonIgnore
    private Usuario propietario;

    @Transient private Long propietarioId;
    @Transient private String propietarioNombre;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;

    @Column
    private boolean activo = true;

    @OneToMany(mappedBy = "vehiculo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<RegistroMantenimiento> historialMantenimiento;

    @OneToMany(mappedBy = "vehiculo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Notificacion> notificaciones;

    //  LÓGICA DE NEGOCIO - Aceite

    public double getKmRestantesParaCambio() {
        if (proximoCambioAceiteKm == null || kilometrajeActual == null) return 0;
        return proximoCambioAceiteKm - kilometrajeActual;
    }

    public EstadoAceite getEstadoAceite() {
        double km = getKmRestantesParaCambio();
        if (km < 0)     return EstadoAceite.ROJO;
        if (km <= 1000) return EstadoAceite.AMARILLO;
        return EstadoAceite.VERDE;
    }

    public double getPorcentajeVidaAceite() {
        if (proximoCambioAceiteKm == null || ultimoCambioAceiteKm == null) return 100.0;
        double intervalo = proximoCambioAceiteKm - ultimoCambioAceiteKm;
        if (intervalo <= 0) return 0.0;
        return Math.max(0.0, Math.min(100.0, (getKmRestantesParaCambio() / intervalo) * 100.0));
    }

    public long getDiasEstimadosHastaCambio(double kmDiarios) {
        if (kmDiarios <= 0) return -1;
        double km = getKmRestantesParaCambio();
        return km <= 0 ? 0 : Math.round(km / kmDiarios);
    }

    //  LÓGICA DE NEGOCIO - ITV

    public EstadoItv getEstadoItv() {
        if (fechaProximaItv == null) return EstadoItv.SIN_DATOS;
        long diasRestantes = java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), fechaProximaItv);
        if (diasRestantes < 0)   return EstadoItv.VENCIDA;
        if (diasRestantes <= 30) return EstadoItv.URGENTE;
        if (diasRestantes <= 90) return EstadoItv.PROXIMA;
        return EstadoItv.OK;
    }

    public long getDiasHastaItv() {
        if (fechaProximaItv == null) return Long.MAX_VALUE;
        return java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), fechaProximaItv);
    }

    //  LÓGICA DE NEGOCIO - Neumáticos

    public EstadoNeumaticos getEstadoNeumaticos() {
        boolean dibujoMal = dibujoNeumaticoMm != null && dibujoNeumaticoMm < 3.0;
        boolean edadMal   = anioFabricacionNeumaticos != null &&
                            (LocalDate.now().getYear() - anioFabricacionNeumaticos) >= 6;
        if (dibujoMal || edadMal) return EstadoNeumaticos.CAMBIAR;
        boolean dibujoPronto = dibujoNeumaticoMm != null && dibujoNeumaticoMm < 4.0;
        boolean edadPronto   = anioFabricacionNeumaticos != null &&
                               (LocalDate.now().getYear() - anioFabricacionNeumaticos) >= 5;
        if (dibujoPronto || edadPronto) return EstadoNeumaticos.REVISAR;
        return EstadoNeumaticos.OK;
    }

    public int getEdadNeumaticosAnios() {
        if (anioFabricacionNeumaticos == null) return 0;
        return LocalDate.now().getYear() - anioFabricacionNeumaticos;
    }

    //  ENUMS

    public enum EstadoAceite {
        VERDE("En buen estado", "#22C55E"),
        AMARILLO("Revisión próxima", "#EAB308"),
        ROJO("¡Mantenimiento urgente!", "#EF4444");
        private final String descripcion; private final String colorHex;
        EstadoAceite(String d, String c) { this.descripcion=d; this.colorHex=c; }
        public String getDescripcion() { return descripcion; }
        public String getColorHex() { return colorHex; }
    }

    public enum EstadoItv {
        OK("ITV vigente","#22C55E"),
        PROXIMA("ITV próxima","#EAB308"),
        URGENTE("ITV urgente","#F97316"),
        VENCIDA("ITV vencida","#EF4444"),
        SIN_DATOS("Sin datos ITV","#64748B");
        private final String descripcion; private final String colorHex;
        EstadoItv(String d, String c) { this.descripcion=d; this.colorHex=c; }
        public String getDescripcion() { return descripcion; }
        public String getColorHex() { return colorHex; }
    }

    public enum EstadoNeumaticos {
        OK("Neumáticos en buen estado","#22C55E"),
        REVISAR("Revisar neumáticos pronto","#EAB308"),
        CAMBIAR("¡Cambiar neumáticos!","#EF4444");
        private final String descripcion; private final String colorHex;
        EstadoNeumaticos(String d, String c) { this.descripcion=d; this.colorHex=c; }
        public String getDescripcion() { return descripcion; }
        public String getColorHex() { return colorHex; }
    }

    @PostLoad @PostPersist
    protected void calcularCamposTransient() {
        if (propietario != null) {
            this.propietarioId     = propietario.getId();
            this.propietarioNombre = propietario.getNombreCompleto();
        }
    }

    @PrePersist
    protected void onCreate() { if (fechaRegistro == null) fechaRegistro = LocalDateTime.now(); }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMarca() { return marca; }
    public void setMarca(String m) { this.marca = m; }
    public String getModelo() { return modelo; }
    public void setModelo(String m) { this.modelo = m; }
    public Integer getAnio() { return anio; }
    public void setAnio(Integer a) { this.anio = a; }
    public String getMatricula() { return matricula; }
    public void setMatricula(String m) { this.matricula = m; }
    public Double getKilometrajeActual() { return kilometrajeActual; }
    public void setKilometrajeActual(Double k) { this.kilometrajeActual = k; }
    public Double getUltimoCambioAceiteKm() { return ultimoCambioAceiteKm; }
    public void setUltimoCambioAceiteKm(Double v) { this.ultimoCambioAceiteKm = v; }
    public Double getProximoCambioAceiteKm() { return proximoCambioAceiteKm; }
    public void setProximoCambioAceiteKm(Double v) { this.proximoCambioAceiteKm = v; }
    public Integer getMotorCc() { return motorCc; }
    public void setMotorCc(Integer v) { this.motorCc = v; }
    public Integer getPotenciaCv() { return potenciaCv; }
    public void setPotenciaCv(Integer v) { this.potenciaCv = v; }
    public String getCombustible() { return combustible; }
    public void setCombustible(String v) { this.combustible = v; }
    public String getTransmision() { return transmision; }
    public void setTransmision(String v) { this.transmision = v; }
    public String getColor() { return color; }
    public void setColor(String v) { this.color = v; }
    public Integer getNumPuertas() { return numPuertas; }
    public void setNumPuertas(Integer v) { this.numPuertas = v; }
    public String getNumBastidor() { return numBastidor; }
    public void setNumBastidor(String v) { this.numBastidor = v; }
    public LocalDate getFechaUltimaItv() { return fechaUltimaItv; }
    public void setFechaUltimaItv(LocalDate v) { this.fechaUltimaItv = v; }
    public LocalDate getFechaProximaItv() { return fechaProximaItv; }
    public void setFechaProximaItv(LocalDate v) { this.fechaProximaItv = v; }
    public Integer getAnioFabricacionNeumaticos() { return anioFabricacionNeumaticos; }
    public void setAnioFabricacionNeumaticos(Integer v) { this.anioFabricacionNeumaticos = v; }
    public Double getDibujoNeumaticoMm() { return dibujoNeumaticoMm; }
    public void setDibujoNeumaticoMm(Double v) { this.dibujoNeumaticoMm = v; }
    public Usuario getPropietario() { return propietario; }
    public void setPropietario(Usuario p) { this.propietario = p; }
    public Long getPropietarioId() { return propietarioId; }
    public String getPropietarioNombre() { return propietarioNombre; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime v) { this.fechaRegistro = v; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean a) { this.activo = a; }
    public List<RegistroMantenimiento> getHistorialMantenimiento() { return historialMantenimiento; }
    public void setHistorialMantenimiento(List<RegistroMantenimiento> h) { this.historialMantenimiento = h; }
}
