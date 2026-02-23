package com.keepcar.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * Proxy hacia la API de Groq (api.groq.com).
 * Acepta mensajes en formato OpenAI (messages[]) o un campo "prompt" simple.
 * El modelo por defecto es llama3-8b-8192 (gratuito y rápido).
 */
@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "*")
public class GrokController {

    @Value("${grok.api.key:}")
    private String groqApiKey;

    private static final String GROQ_URL = "https://api.groq.com/openai/v1/chat/completions";
    private static final String DEFAULT_MODEL = "llama3-8b-8192";

    @PostMapping("/chat")
    public ResponseEntity<?> chat(
            @RequestBody Map<String, Object> body,
            @RequestHeader(value = "X-Grok-Key", defaultValue = "") String headerKey) {

        String apiKey = headerKey.isBlank() ? groqApiKey : headerKey;

        if (apiKey == null || apiKey.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "API key no configurada",
                "detalle", "Añade grok.api.key en application.properties o proporciona X-Grok-Key"
            ));
        }

        try {
            RestTemplate restTemplate = new RestTemplate();

            List<Map<String, Object>> messages = buildMessages(body);
            String model = body.containsKey("model") ? body.get("model").toString() : DEFAULT_MODEL;

            Map<String, Object> groqRequest = new LinkedHashMap<>();
            groqRequest.put("model", model);
            groqRequest.put("messages", messages);
            groqRequest.put("max_tokens", body.getOrDefault("max_tokens", 600));
            groqRequest.put("temperature", 0.7);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(groqRequest, headers);

            @SuppressWarnings("unchecked")
            ResponseEntity<Map<String, Object>> response =
                (ResponseEntity<Map<String, Object>>) (ResponseEntity<?>) restTemplate.postForEntity(GROQ_URL, entity, Map.class);

            Map<String, Object> responseBody = response.getBody();

            if (responseBody == null) {
                return ResponseEntity.ok(Map.of("error", "Respuesta vacía del modelo"));
            }

            if (body.containsKey("prompt")) {
                // Formato simple: devuelve {respuesta: "..."}
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
                @SuppressWarnings("unchecked")
                Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                return ResponseEntity.ok(Map.of("respuesta", message.get("content").toString()));
            }

            // Formato OpenAI completo: devuelve la respuesta tal cual
            return ResponseEntity.ok(responseBody);

        } catch (HttpClientErrorException e) {
            return ResponseEntity.ok(Map.of(
                "error", "Error en Groq API: " + e.getStatusCode(),
                "detalle", e.getResponseBodyAsString()
            ));
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of("error", "Error de conexión: " + e.getMessage()));
        }
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> buildMessages(Map<String, Object> body) {
        if (body.containsKey("messages")) {
            return (List<Map<String, Object>>) body.get("messages");
        }
        // Compatibilidad con formato simple {prompt: "..."}
        String prompt = body.getOrDefault("prompt", "Hola").toString();
        return List.of(
            Map.of("role", "system", "content",
                "Eres el asistente mecánico de KeepCar. Da precios estimados en euros para España."),
            Map.of("role", "user", "content", prompt)
        );
    }
}
