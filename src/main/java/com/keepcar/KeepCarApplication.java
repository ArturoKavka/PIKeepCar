package com.keepcar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * KEEPCAR - Sistema de Gestión de Mantenimiento Vehicular
 *
 * Aplicación desarrollada para el módulo de Proyecto del ciclo DAM.
 * Curso 2025/26
 *
 * Equipo de desarrollo:
 *   - Amael Silva    (Project Manager / Analista)
 *   - El Almendra    (Desarrollador Full-Stack Backend)
 *   - Arturo Kavka   (Desarrollador Móvil / UX-UI)
 *
 * Stack tecnológico:
 *   - Java 17 + Spring Boot 3.x
 *   - Spring Data JPA + H2 (in-memory)
 *   - HTML5 / CSS3 / JavaScript (Vanilla)
 *
 * Para ejecutar: mvn spring-boot:run
 * Dashboard:     http://localhost:8080
 * H2 Console:    http://localhost:8080/h2-console
 * API REST:      http://localhost:8080/api/vehiculos
 * Health:        http://localhost:8080/api/health
 */
@SpringBootApplication
public class KeepCarApplication {

    public static void main(String[] args) {
        SpringApplication.run(KeepCarApplication.class, args);
        System.out.println("\n" +
            "╔══════════════════════════════════════════════╗\n" +
            "║          KeepCar v1.0.0 - INICIADO          ║\n" +
            "║                                              ║\n" +
            "║  Dashboard  → http://localhost:8080          ║\n" +
            "║  H2 Console → http://localhost:8080/h2-console ║\n" +
            "║  API REST   → http://localhost:8080/api      ║\n" +
            "║                                              ║\n" +
            "║  Equipo: Amael | Almendra | Arturo           ║\n" +
            "╚══════════════════════════════════════════════╝\n"
        );
    }
}
