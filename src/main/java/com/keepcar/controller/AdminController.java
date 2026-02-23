package com.keepcar.controller;

import com.keepcar.model.AuditLog;
import com.keepcar.model.Usuario;
import com.keepcar.repository.UsuarioRepository;
import com.keepcar.repository.VehiculoRepository;
import com.keepcar.service.AuditService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * KEEPCAR - Panel de Administración
 * Solo accesible para usuarios con rol ADMIN.
 * Muestra actividad en tiempo real del sistema.
 */
@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    private final AuditService auditService;
    private final UsuarioRepository usuarioRepo;
    private final VehiculoRepository vehiculoRepo;
    private final com.keepcar.repository.AuditLogRepository auditLogRepo;

    public AdminController(AuditService as, UsuarioRepository ur, VehiculoRepository vr,
                           com.keepcar.repository.AuditLogRepository alr) {
        this.auditService = as;
        this.usuarioRepo  = ur;
        this.vehiculoRepo = vr;
        this.auditLogRepo = alr;
    }

    // Verificación de permisos
    private boolean verificarAdmin(String rol) { return "ADMIN".equalsIgnoreCase(rol); }

    @GetMapping("/logs")
    public ResponseEntity<?> getLogs(
            @RequestHeader(value = "X-User-Rol", defaultValue = "USER") String rol) {
        if (!verificarAdmin(rol)) return ResponseEntity.status(403).body(Map.of("error", "Acceso denegado"));
        return ResponseEntity.ok(auditService.obtenerResumen());
    }

    @GetMapping("/logs/nuevos")
    public ResponseEntity<?> getLogsNuevos(
            @RequestParam(defaultValue = "0") Long sinceId,
            @RequestHeader(value = "X-User-Rol", defaultValue = "USER") String rol) {
        if (!verificarAdmin(rol)) return ResponseEntity.status(403).body(Map.of("error", "Acceso denegado"));
        List<AuditLog> nuevos = auditService.obtenerNuevos(sinceId);
        return ResponseEntity.ok(Map.of(
                "nuevos", nuevos,
                "cantidad", nuevos.size(),
                "ultimoId", nuevos.isEmpty() ? sinceId : nuevos.get(nuevos.size() - 1).getId()
        ));
    }

    @GetMapping("/usuarios")
    public ResponseEntity<?> getUsuarios(
            @RequestHeader(value = "X-User-Rol", defaultValue = "USER") String rol) {
        if (!verificarAdmin(rol)) return ResponseEntity.status(403).body(Map.of("error", "Acceso denegado"));

        List<Map<String, Object>> usuarios = usuarioRepo.findAll().stream()
                .map(u -> {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("id", u.getId());
                    m.put("username", u.getUsername());
                    m.put("nombre", u.getNombreCompleto());
                    m.put("nombreCompleto", u.getNombreCompleto());
                    m.put("avatarIniciales", u.getAvatarIniciales());
                    m.put("rol", u.getRol().name());
                    m.put("esAdmin", u.isAdmin());
                    m.put("email", u.getEmail());
                    m.put("telefono", u.getTelefono());
                    m.put("activo", u.isActivo());
                    m.put("fechaRegistro", u.getFechaRegistro());
                    m.put("vehiculos", vehiculoRepo.findByPropietarioIdAndActivoTrue(u.getId()).size());
                    return m;
                }).collect(Collectors.toList());

        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/dashboard")
    public ResponseEntity<?> dashboard(
            @RequestHeader(value = "X-User-Rol", defaultValue = "USER") String rol) {
        if (!verificarAdmin(rol)) return ResponseEntity.status(403).body(Map.of("error", "Acceso denegado"));

        long totalUsuarios = usuarioRepo.count();
        long admins = usuarioRepo.findAll().stream().filter(Usuario::isAdmin).count();
        long totalVehiculos = vehiculoRepo.count();
        long vehiculosActivos = vehiculoRepo.findByActivoTrue().size();

        // Alertas ITV
        long itvAlertas = vehiculoRepo.findByActivoTrue().stream()
            .filter(v -> v.getEstadoItv() != com.keepcar.model.Vehiculo.EstadoItv.OK &&
                         v.getEstadoItv() != com.keepcar.model.Vehiculo.EstadoItv.SIN_DATOS)
            .count();

        // Acciones de hoy
        java.time.LocalDateTime hoyInicio = java.time.LocalDate.now().atStartOfDay();
        long accionesHoy = auditLogRepo.findTop200ByOrderByTimestampDesc().stream()
            .filter(a -> a.getTimestamp() != null && a.getTimestamp().isAfter(hoyInicio))
            .count();

        Map<String, Object> dash = new LinkedHashMap<>();
        dash.put("totalUsuarios", totalUsuarios);
        dash.put("totalAdmins", admins);
        dash.put("totalUsers", totalUsuarios - admins);
        dash.put("totalVehiculos", totalVehiculos);
        dash.put("vehiculosActivos", vehiculosActivos);
        dash.put("alertasUrgentes", vehiculoRepo.findTodosVencidos().size());
        dash.put("alertasProximas", vehiculoRepo.findTodasAlertasProximas().size());
        dash.put("itvAlertas", itvAlertas);
        dash.put("accionesHoy", accionesHoy);

        return ResponseEntity.ok(dash);
    }
}
