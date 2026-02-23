package com.keepcar.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * KEEPCAR - Registro de Auditoría
 * Captura TODAS las acciones de los usuarios en tiempo real.
 * Visible únicamente para administradores en el panel /admin.html
 */
@Entity
@Table(name = "audit_log")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id")
    private Long usuarioId;

    @Column(name = "usuario_nombre")
    private String usuarioNombre;

    @Column(name = "usuario_rol")
    private String usuarioRol;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoAccion accion;

    @Column(nullable = false, length = 1000)
    private String detalle;

    @Column(name = "entidad_tipo")
    private String entidadTipo;

    @Column(name = "entidad_id")
    private Long entidadId;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column
    private String ip;

    public enum TipoAccion {
        LOGIN("🔐 Login"),
        LOGOUT("🚪 Logout"),
        CREAR_VEHICULO("🚗 Crear Vehículo"),
        EDITAR_VEHICULO("✏️ Editar Vehículo"),
        ELIMINAR_VEHICULO("🗑️ Eliminar Vehículo"),
        ACTUALIZAR_KM("📍 Actualizar Km"),
        REGISTRAR_MANTENIMIENTO("🔧 Mantenimiento"),
        CAMBIAR_PASSWORD("🔑 Cambiar Password"),
        MARCAR_NOTIFICACION("🔔 Marcar Notificación");

        private final String etiqueta;
        TipoAccion(String e) { this.etiqueta = e; }
        public String getEtiqueta() { return etiqueta; }
    }

    @PrePersist
    protected void onCreate() {
        if (timestamp == null) timestamp = LocalDateTime.now();
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long v) { this.usuarioId = v; }
    public String getUsuarioNombre() { return usuarioNombre; }
    public void setUsuarioNombre(String v) { this.usuarioNombre = v; }
    public String getUsuarioRol() { return usuarioRol; }
    public void setUsuarioRol(String v) { this.usuarioRol = v; }
    public TipoAccion getAccion() { return accion; }
    public void setAccion(TipoAccion a) { this.accion = a; }
    public String getDetalle() { return detalle; }
    public void setDetalle(String d) { this.detalle = d; }
    public String getEntidadTipo() { return entidadTipo; }
    public void setEntidadTipo(String v) { this.entidadTipo = v; }
    public Long getEntidadId() { return entidadId; }
    public void setEntidadId(Long v) { this.entidadId = v; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime v) { this.timestamp = v; }
    public String getIp() { return ip; }
    public void setIp(String v) { this.ip = v; }
}
