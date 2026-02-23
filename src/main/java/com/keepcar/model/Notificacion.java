package com.keepcar.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notificacion")
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vehiculo_id", nullable = false)
    @JsonIgnore
    private Vehiculo vehiculo;

    @Transient
    private Long vehiculoId;
    @Transient
    private String vehiculoNombre;
    @Transient
    private String vehiculoMatricula;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonIgnore
    private Usuario usuario;

    @Transient
    private Long usuarioId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoNotificacion tipo;

    @Column(nullable = false, length = 500)
    private String mensaje;

    @Column
    private boolean leida = false;

    @Column(nullable = false)
    private LocalDateTime fecha;

    public enum TipoNotificacion {
        CAMBIO_ACEITE_URGENTE("🔴 Aceite Urgente"),
        CAMBIO_ACEITE_PROXIMO("🟡 Aceite Próximo"),
        REVISION_GENERAL("🔵 Revisión General"),
        ITV_PROXIMA("📋 ITV Próxima"),
        NEUMATICOS("🔧 Neumáticos"),
        SISTEMA_INFO("ℹ️ Sistema");

        private final String etiqueta;
        TipoNotificacion(String e) { this.etiqueta = e; }
        public String getEtiqueta() { return etiqueta; }
    }

    @PostLoad
    @PostPersist
    protected void calcular() {
        if (vehiculo != null) {
            this.vehiculoId = vehiculo.getId();
            this.vehiculoNombre = vehiculo.getMarca() + " " + vehiculo.getModelo();
            this.vehiculoMatricula = vehiculo.getMatricula();
        }
        if (usuario != null) this.usuarioId = usuario.getId();
    }

    @PrePersist
    protected void onCreate() {
        if (fecha == null) fecha = LocalDateTime.now();
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Vehiculo getVehiculo() { return vehiculo; }
    public void setVehiculo(Vehiculo v) { this.vehiculo = v; }
    public Long getVehiculoId() { return vehiculoId; }
    public String getVehiculoNombre() { return vehiculoNombre; }
    public String getVehiculoMatricula() { return vehiculoMatricula; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario u) { this.usuario = u; }
    public Long getUsuarioId() { return usuarioId; }
    public TipoNotificacion getTipo() { return tipo; }
    public void setTipo(TipoNotificacion t) { this.tipo = t; }
    public String getMensaje() { return mensaje; }
    public void setMensaje(String m) { this.mensaje = m; }
    public boolean isLeida() { return leida; }
    public void setLeida(boolean l) { this.leida = l; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime f) { this.fecha = f; }
}
