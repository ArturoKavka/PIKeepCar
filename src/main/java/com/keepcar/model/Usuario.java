package com.keepcar.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * KEEPCAR - Entidad Usuario
 * Gestión de permisos y acceso por roles.
 * Roles: ADMIN (Arturo, Amael) | USER (Juan, Marcos, Manu, Rosa)
 */
@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(name = "nombre_completo", nullable = false)
    private String nombreCompleto;

    @Column
    private String email;

    @Column
    private String telefono;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol rol;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;

    @Column(name = "activo")
    private boolean activo = true;

    @Column(name = "avatar_iniciales")
    private String avatarIniciales;

    @com.fasterxml.jackson.annotation.JsonIgnore
    @OneToMany(mappedBy = "propietario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Vehiculo> vehiculos;

    public enum Rol {
        ADMIN, USER
    }

    public boolean isAdmin() {
        return this.rol == Rol.ADMIN;
    }

    @PrePersist
    protected void onCreate() {
        if (fechaRegistro == null) fechaRegistro = LocalDateTime.now();
        if (nombreCompleto != null && avatarIniciales == null) {
            String[] partes = nombreCompleto.trim().split("\\s+");
            avatarIniciales = partes.length >= 2
                    ? "" + partes[0].charAt(0) + partes[1].charAt(0)
                    : "" + partes[0].charAt(0);
            avatarIniciales = avatarIniciales.toUpperCase();
        }
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
    public String getAvatarIniciales() { return avatarIniciales; }
    public void setAvatarIniciales(String avatarIniciales) { this.avatarIniciales = avatarIniciales; }
    public List<Vehiculo> getVehiculos() { return vehiculos; }
    public void setVehiculos(List<Vehiculo> vehiculos) { this.vehiculos = vehiculos; }
}
