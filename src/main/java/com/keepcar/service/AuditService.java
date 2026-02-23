package com.keepcar.service;

import com.keepcar.model.AuditLog;
import com.keepcar.model.AuditLog.TipoAccion;
import com.keepcar.repository.AuditLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * KEEPCAR - Servicio de Auditoría
 * Registra cada acción del usuario para el panel de administración.
 */
@Service
public class AuditService {

    private final AuditLogRepository auditLogRepository;

    public AuditService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void registrar(Long usuarioId, String usuarioNombre, String usuarioRol,
                          TipoAccion accion, String detalle,
                          String entidadTipo, Long entidadId) {
        AuditLog log = new AuditLog();
        log.setUsuarioId(usuarioId);
        log.setUsuarioNombre(usuarioNombre);
        log.setUsuarioRol(usuarioRol);
        log.setAccion(accion);
        log.setDetalle(detalle);
        log.setEntidadTipo(entidadTipo);
        log.setEntidadId(entidadId);
        auditLogRepository.save(log);
    }

    public List<AuditLog> obtenerTodos() {
        return auditLogRepository.findTop200ByOrderByTimestampDesc();
    }

    public List<AuditLog> obtenerNuevos(Long sinceId) {
        return auditLogRepository.findNewSince(sinceId);
    }

    public Map<String, Object> obtenerResumen() {
        List<AuditLog> todos = auditLogRepository.findTop200ByOrderByTimestampDesc();
        long totalLogins = todos.stream().filter(a -> a.getAccion() == TipoAccion.LOGIN).count();
        long totalEdiciones = todos.stream().filter(a ->
                a.getAccion() == TipoAccion.CREAR_VEHICULO ||
                a.getAccion() == TipoAccion.EDITAR_VEHICULO ||
                a.getAccion() == TipoAccion.ACTUALIZAR_KM).count();
        long ultimoId = todos.isEmpty() ? 0L : todos.get(0).getId();

        return Map.of(
                "total", todos.size(),
                "logins", totalLogins,
                "ediciones", totalEdiciones,
                "ultimoId", ultimoId,
                "logs", todos
        );
    }
}
