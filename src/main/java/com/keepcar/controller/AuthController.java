package com.keepcar.controller;

import com.keepcar.model.Usuario;
import com.keepcar.repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UsuarioRepository repo;

    public AuthController(UsuarioRepository repo) {
        this.repo = repo;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String u = body.getOrDefault("usuario", "").toLowerCase().trim();
        String p = body.getOrDefault("password", "").trim();

        return repo.findByUsername(u)
            .filter(user -> user.getPassword().equals(p) && user.isActivo())
            .map(user -> {
                Map<String, Object> r = new LinkedHashMap<>();
                r.put("success", true);
                r.put("id", user.getId());
                r.put("nombre", user.getNombreCompleto());
                r.put("usuario", user.getUsername());
                r.put("email", user.getEmail());
                r.put("telefono", user.getTelefono());
                r.put("rol", user.getRol().name());
                r.put("esAdmin", user.isAdmin());
                r.put("avatarIniciales", user.getAvatarIniciales());
                r.put("icono", user.getAvatarIniciales());
                return ResponseEntity.ok(r);
            })
            .orElseGet(() -> ResponseEntity.status(401)
                .body(Map.of("success", false, "mensaje", "Credenciales incorrectas")));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok(Map.of("success", true));
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(
            @RequestHeader(value = "X-User-Id", defaultValue = "0") Long userId) {
        return repo.findById(userId)
            .map(user -> {
                Map<String, Object> r = new LinkedHashMap<>();
                r.put("id", user.getId());
                r.put("nombre", user.getNombreCompleto());
                r.put("usuario", user.getUsername());
                r.put("email", user.getEmail());
                r.put("telefono", user.getTelefono());
                r.put("rol", user.getRol().name());
                r.put("esAdmin", user.isAdmin());
                r.put("avatarIniciales", user.getAvatarIniciales());
                return ResponseEntity.ok(r);
            })
            .orElse(ResponseEntity.status(404).body(Map.of("error", "Usuario no encontrado")));
    }

    /**
     * Callback OAuth2 de Google. Spring Security maneja el flujo; aquí
     * se recibe el principal ya autenticado, se crea el usuario si no existe
     * y se redirige al frontend con los datos en la URL.
     */
    @GetMapping("/google/callback")
    public ResponseEntity<Void> googleCallback(
            @AuthenticationPrincipal OAuth2User principal) {

        if (principal == null) {
            return ResponseEntity.status(302).header("Location", "/index.html?error=google").build();
        }

        String email  = Optional.ofNullable(principal.<String>getAttribute("email"))
                .orElse("").toLowerCase();
        String nombre = Optional.ofNullable(principal.<String>getAttribute("name"))
                .orElse("Usuario Google");

        if (email.isBlank()) {
            return ResponseEntity.status(302).header("Location", "/index.html?error=google").build();
        }

        Usuario user = repo.findByUsername(email).orElseGet(() -> {
            Usuario n = new Usuario();
            n.setUsername(email);
            n.setEmail(email);
            n.setNombreCompleto(nombre);
            n.setRol(Usuario.Rol.USER);
            n.setPassword("oauth2_user");
            n.setActivo(true);
            n.setAvatarIniciales(nombre.substring(0, 1).toUpperCase());
            return repo.save(n);
        });

        String redirect = "/index.html"
            + "?googleLogin=1"
            + "&id=" + user.getId()
            + "&nombre=" + URLEncoder.encode(user.getNombreCompleto(), StandardCharsets.UTF_8)
            + "&rol=" + user.getRol().name()
            + "&esAdmin=" + user.isAdmin()
            + "&icono=" + user.getAvatarIniciales();

        return ResponseEntity.status(302).header("Location", redirect).build();
    }
}
