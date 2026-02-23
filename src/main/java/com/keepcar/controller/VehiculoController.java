package com.keepcar.controller;

import com.keepcar.model.RegistroMantenimiento;
import com.keepcar.model.Vehiculo;
import com.keepcar.service.MantenimientoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class VehiculoController {

    private final MantenimientoService svc;

    public VehiculoController(MantenimientoService svc) { this.svc = svc; }

    private boolean isAdmin(String rol) { return "ADMIN".equalsIgnoreCase(rol); }

    @GetMapping("/vehiculos")
    public ResponseEntity<List<Vehiculo>> getVehiculos(
            @RequestHeader("X-User-Id") Long userId,
            @RequestHeader(value = "X-User-Rol", defaultValue = "USER") String rol) {
        return ResponseEntity.ok(svc.obtenerVehiculosDeUsuario(userId, isAdmin(rol)));
    }

    @GetMapping("/vehiculos/{id}")
    public ResponseEntity<Vehiculo> getVehiculo(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId,
            @RequestHeader(value = "X-User-Rol", defaultValue = "USER") String rol) {
        return svc.obtenerVehiculoPorId(id, userId, isAdmin(rol))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/vehiculos")
    public ResponseEntity<Vehiculo> crear(
            @RequestBody Vehiculo vehiculo,
            @RequestHeader("X-User-Id") Long userId,
            @RequestHeader(value = "X-User-Name", defaultValue = "Usuario") String nombre,
            @RequestHeader(value = "X-User-Rol", defaultValue = "USER") String rol) {
        return ResponseEntity.ok(svc.crearVehiculo(vehiculo, userId, nombre, rol));
    }

    @PutMapping("/vehiculos/{id}")
    public ResponseEntity<Vehiculo> actualizar(
            @PathVariable Long id,
            @RequestBody Vehiculo datos,
            @RequestHeader("X-User-Id") Long userId,
            @RequestHeader(value = "X-User-Name", defaultValue = "Usuario") String nombre,
            @RequestHeader(value = "X-User-Rol", defaultValue = "USER") String rol) {
        return svc.actualizarVehiculo(id, datos, userId, nombre, rol, isAdmin(rol))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/vehiculos/{id}/km")
    public ResponseEntity<Vehiculo> actualizarKm(
            @PathVariable Long id,
            @RequestBody Map<String, Double> body,
            @RequestHeader("X-User-Id") Long userId,
            @RequestHeader(value = "X-User-Name", defaultValue = "Usuario") String nombre,
            @RequestHeader(value = "X-User-Rol", defaultValue = "USER") String rol) {
        Double km = body.get("kilometrajeActual");
        if (km == null) return ResponseEntity.badRequest().build();
        return svc.actualizarKilometraje(id, km, userId, nombre, rol, isAdmin(rol))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/vehiculos/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId,
            @RequestHeader(value = "X-User-Name", defaultValue = "Usuario") String nombre,
            @RequestHeader(value = "X-User-Rol", defaultValue = "USER") String rol) {
        return svc.eliminarVehiculo(id, userId, nombre, rol, isAdmin(rol))
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/alertas")
    public ResponseEntity<Map<String, Object>> alertas(
            @RequestHeader("X-User-Id") Long userId,
            @RequestHeader(value = "X-User-Rol", defaultValue = "USER") String rol) {
        return ResponseEntity.ok(svc.resumenAlertas(userId, isAdmin(rol)));
    }

    @GetMapping("/notificaciones")
    public ResponseEntity<?> notificaciones(@RequestHeader("X-User-Id") Long userId) {
        return ResponseEntity.ok(svc.obtenerNotificaciones(userId));
    }

    @GetMapping("/notificaciones/count")
    public ResponseEntity<Map<String, Long>> countNoLeidas(@RequestHeader("X-User-Id") Long userId) {
        return ResponseEntity.ok(Map.of("noLeidas", svc.contarNoLeidas(userId)));
    }

    @PutMapping("/notificaciones/{id}/leer")
    public ResponseEntity<?> marcarLeida(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId,
            @RequestHeader(value = "X-User-Name", defaultValue = "") String nombre,
            @RequestHeader(value = "X-User-Rol", defaultValue = "USER") String rol) {
        return svc.marcarLeida(id, userId, nombre, rol)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/notificaciones/leer-todas")
    public ResponseEntity<Void> marcarTodasLeidas(
            @RequestHeader("X-User-Id") Long userId,
            @RequestHeader(value = "X-User-Name", defaultValue = "") String nombre,
            @RequestHeader(value = "X-User-Rol", defaultValue = "USER") String rol) {
        svc.marcarTodasLeidas(userId, nombre, rol);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/notificaciones/{id}/no-leer")
    public ResponseEntity<?> marcarNoLeida(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId) {
        return svc.marcarNoLeida(id, userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/vehiculos/{id}/historial")
    public ResponseEntity<?> historial(@PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId,
            @RequestHeader(value = "X-User-Rol", defaultValue = "USER") String rol) {
        return ResponseEntity.ok(svc.obtenerHistorial(id));
    }

    @PostMapping("/vehiculos/{id}/mantenimiento")
    public ResponseEntity<?> registrarMantenimiento(
            @PathVariable Long id,
            @RequestBody RegistroMantenimiento reg,
            @RequestHeader("X-User-Id") Long userId,
            @RequestHeader(value = "X-User-Name", defaultValue = "") String nombre,
            @RequestHeader(value = "X-User-Rol", defaultValue = "USER") String rol) {
        return svc.registrarMantenimiento(id, reg, userId, nombre, rol, isAdmin(rol))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // -------- USUARIO / AJUSTES --------

    @PostMapping("/usuario/cambiar-password")
    public ResponseEntity<Map<String, Object>> cambiarPassword(
            @RequestBody Map<String, String> body,
            @RequestHeader("X-User-Id") Long userId,
            @RequestHeader(value = "X-User-Name", defaultValue = "") String nombre,
            @RequestHeader(value = "X-User-Rol", defaultValue = "USER") String rol) {
        boolean ok = svc.cambiarPassword(userId, body.get("passwordActual"), body.get("nuevaPassword"), nombre, rol);
        return ResponseEntity.ok(Map.of(
                "success", ok,
                "mensaje", ok ? "Contraseña actualizada correctamente" : "Contraseña actual incorrecta"));
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(svc.healthCheck());
    }
}
