package com.keepcar.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.Map;
import java.util.LinkedHashMap;

/**
 * KEEPCAR - Elimina el WhiteLabel Error de /api
 * Devuelve info útil en lugar del error genérico de Spring.
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ApiInfoController {

    @GetMapping({"", "/"})
    public ResponseEntity<Map<String, Object>> apiInfo() {
        Map<String, Object> info = new LinkedHashMap<>();
        info.put("app", "KeepCar API v3.0");
        info.put("status", "online");
        info.put("descripcion", "Sistema de gestión de mantenimiento vehicular");
        info.put("equipo", "Amael Silva · El Almendra · Arturo Kavka - DAM 2025/26");
        info.put("endpoints", Map.of(
            "auth",    "/api/auth/login | /api/auth/logout",
            "vehiculos", "/api/vehiculos",
            "alertas",   "/api/alertas",
            "notif",     "/api/notificaciones",
            "ai",        "/api/ai/chat",
            "admin",     "/api/admin/logs (solo ADMIN)",
            "health",    "/api/health"
        ));
        return ResponseEntity.ok(info);
    }
}
