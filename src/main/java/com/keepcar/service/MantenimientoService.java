package com.keepcar.service;

import com.keepcar.model.*;
import com.keepcar.model.AuditLog.TipoAccion;
import com.keepcar.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class MantenimientoService {

    private static final double KM_DIARIOS_DEFAULT = 50.0;

    private final VehiculoRepository vehiculoRepo;
    private final UsuarioRepository usuarioRepo;
    private final MantenimientoRepository mantenimientoRepo;
    private final NotificacionRepository notificacionRepo;
    private final AuditService auditService;

    public MantenimientoService(VehiculoRepository vr, UsuarioRepository ur,
                                MantenimientoRepository mr, NotificacionRepository nr,
                                AuditService as) {
        this.vehiculoRepo = vr;
        this.usuarioRepo = ur;
        this.mantenimientoRepo = mr;
        this.notificacionRepo = nr;
        this.auditService = as;
    }

    //  VEHICULOS - filtrados por usuario

    public List<Vehiculo> obtenerVehiculosDeUsuario(Long usuarioId, boolean isAdmin) {
        return isAdmin ? vehiculoRepo.findByActivoTrue()
                       : vehiculoRepo.findByPropietarioIdAndActivoTrue(usuarioId);
    }

    public Optional<Vehiculo> obtenerVehiculoPorId(Long vehiculoId, Long usuarioId, boolean isAdmin) {
        return vehiculoRepo.findById(vehiculoId).filter(v ->
                isAdmin || v.getPropietario().getId().equals(usuarioId));
    }

    public Vehiculo crearVehiculo(Vehiculo vehiculo, Long usuarioId, String usuarioNombre, String rol) {
        Usuario propietario = usuarioRepo.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        vehiculo.setPropietario(propietario);
        vehiculo.setFechaRegistro(LocalDateTime.now());
        Vehiculo saved = vehiculoRepo.save(vehiculo);

        // Generar notificación de bienvenida al vehículo
        crearNotificacion(saved, propietario, Notificacion.TipoNotificacion.SISTEMA_INFO,
                "Vehículo " + saved.getMarca() + " " + saved.getModelo() + " registrado correctamente.");

        auditService.registrar(usuarioId, usuarioNombre, rol,
                TipoAccion.CREAR_VEHICULO,
                "Nuevo vehículo: " + saved.getMarca() + " " + saved.getModelo() + " (" + saved.getMatricula() + ")",
                "Vehiculo", saved.getId());

        return saved;
    }

    public Optional<Vehiculo> actualizarVehiculo(Long id, Vehiculo datos, Long usuarioId, String usuarioNombre, String rol, boolean isAdmin) {
        return vehiculoRepo.findById(id)
                .filter(v -> isAdmin || v.getPropietario().getId().equals(usuarioId))
                .map(v -> {
                    v.setMarca(datos.getMarca());
                    v.setModelo(datos.getModelo());
                    v.setAnio(datos.getAnio());
                    v.setMatricula(datos.getMatricula());
                    v.setKilometrajeActual(datos.getKilometrajeActual());
                    v.setUltimoCambioAceiteKm(datos.getUltimoCambioAceiteKm());
                    v.setProximoCambioAceiteKm(datos.getProximoCambioAceiteKm());
                    if (datos.getMotorCc() != null)   v.setMotorCc(datos.getMotorCc());
                    if (datos.getPotenciaCv() != null) v.setPotenciaCv(datos.getPotenciaCv());
                    if (datos.getCombustible() != null) v.setCombustible(datos.getCombustible());
                    if (datos.getTransmision() != null) v.setTransmision(datos.getTransmision());
                    if (datos.getColor() != null)    v.setColor(datos.getColor());
                    if (datos.getNumPuertas() != null) v.setNumPuertas(datos.getNumPuertas());
                    if (datos.getNumBastidor() != null) v.setNumBastidor(datos.getNumBastidor());
                    // Campos ITV y neumáticos
                    if (datos.getFechaUltimaItv()   != null) v.setFechaUltimaItv(datos.getFechaUltimaItv());
                    if (datos.getFechaProximaItv()  != null) v.setFechaProximaItv(datos.getFechaProximaItv());
                    if (datos.getAnioFabricacionNeumaticos() != null) v.setAnioFabricacionNeumaticos(datos.getAnioFabricacionNeumaticos());
                    if (datos.getDibujoNeumaticoMm() != null) v.setDibujoNeumaticoMm(datos.getDibujoNeumaticoMm());
                    Vehiculo saved = vehiculoRepo.save(v);
                    auditService.registrar(usuarioId, usuarioNombre, rol,
                            TipoAccion.EDITAR_VEHICULO,
                            "Editado: " + saved.getMarca() + " " + saved.getModelo() + " (" + saved.getMatricula() + ")",
                            "Vehiculo", saved.getId());
                    return saved;
                });
    }

    public Optional<Vehiculo> actualizarKilometraje(Long id, double nuevoKm, Long usuarioId, String usuarioNombre, String rol, boolean isAdmin) {
        return vehiculoRepo.findById(id)
                .filter(v -> isAdmin || v.getPropietario().getId().equals(usuarioId))
                .map(v -> {
                    double anterior = v.getKilometrajeActual();
                    v.setKilometrajeActual(nuevoKm);
                    Vehiculo saved = vehiculoRepo.save(v);

                    // Generar alertas si corresponde
                    generarAlertasSiNecesario(saved);

                    auditService.registrar(usuarioId, usuarioNombre, rol,
                            TipoAccion.ACTUALIZAR_KM,
                            saved.getMarca() + " " + saved.getModelo() + ": " + (int)anterior + " → " + (int)nuevoKm + " km",
                            "Vehiculo", saved.getId());
                    return saved;
                });
    }

    public boolean eliminarVehiculo(Long id, Long usuarioId, String usuarioNombre, String rol, boolean isAdmin) {
        return vehiculoRepo.findById(id)
                .filter(v -> isAdmin || v.getPropietario().getId().equals(usuarioId))
                .map(v -> {
                    v.setActivo(false);
                    vehiculoRepo.save(v);
                    auditService.registrar(usuarioId, usuarioNombre, rol,
                            TipoAccion.ELIMINAR_VEHICULO,
                            "Eliminado: " + v.getMarca() + " " + v.getModelo() + " (" + v.getMatricula() + ")",
                            "Vehiculo", v.getId());
                    return true;
                }).orElse(false);
    }

    //  ALERTAS

    private void generarAlertasSiNecesario(Vehiculo v) {
        // --- Aceite ---
        Vehiculo.EstadoAceite estado = v.getEstadoAceite();
        if (estado == Vehiculo.EstadoAceite.ROJO) {
            crearNotificacion(v, v.getPropietario(), Notificacion.TipoNotificacion.CAMBIO_ACEITE_URGENTE,
                    "🔴 " + v.getMarca() + " " + v.getModelo() + ": cambio de aceite VENCIDO. Km actual: " +
                    v.getKilometrajeActual().intValue() + " km (límite: " + v.getProximoCambioAceiteKm().intValue() + " km).");
        } else if (estado == Vehiculo.EstadoAceite.AMARILLO) {
            crearNotificacion(v, v.getPropietario(), Notificacion.TipoNotificacion.CAMBIO_ACEITE_PROXIMO,
                    "🟡 " + v.getMarca() + " " + v.getModelo() + ": quedan " +
                    (int)v.getKmRestantesParaCambio() + " km para el cambio de aceite.");
        }

        Vehiculo.EstadoItv estadoItv = v.getEstadoItv();
        if (estadoItv == Vehiculo.EstadoItv.VENCIDA) {
            crearNotificacion(v, v.getPropietario(), Notificacion.TipoNotificacion.ITV_PROXIMA,
                    "🚨 " + v.getMarca() + " " + v.getModelo() + ": ITV VENCIDA. Renueva urgentemente.");
        } else if (estadoItv == Vehiculo.EstadoItv.URGENTE) {
            crearNotificacion(v, v.getPropietario(), Notificacion.TipoNotificacion.ITV_PROXIMA,
                    "📋 " + v.getMarca() + " " + v.getModelo() + ": ITV urgente en " + v.getDiasHastaItv() + " días.");
        } else if (estadoItv == Vehiculo.EstadoItv.PROXIMA) {
            crearNotificacion(v, v.getPropietario(), Notificacion.TipoNotificacion.ITV_PROXIMA,
                    "📋 " + v.getMarca() + " " + v.getModelo() + ": ITV próxima en " + v.getDiasHastaItv() + " días.");
        }
        // --- Neumáticos ---
        Vehiculo.EstadoNeumaticos estadoNeu = v.getEstadoNeumaticos();
        if (estadoNeu == Vehiculo.EstadoNeumaticos.CAMBIAR) {
            String razon = (v.getDibujoNeumaticoMm() != null && v.getDibujoNeumaticoMm() < 3.0)
                    ? "dibujo " + v.getDibujoNeumaticoMm() + "mm (mín. 3mm)"
                    : "edad " + v.getEdadNeumaticosAnios() + " años (máx. recomendado: 6)";
            crearNotificacion(v, v.getPropietario(), Notificacion.TipoNotificacion.NEUMATICOS,
                    "🔧 " + v.getMarca() + " " + v.getModelo() + ": ¡Cambiar neumáticos! Motivo: " + razon);
        } else if (estadoNeu == Vehiculo.EstadoNeumaticos.REVISAR) {
            crearNotificacion(v, v.getPropietario(), Notificacion.TipoNotificacion.NEUMATICOS,
                    "⚠️ " + v.getMarca() + " " + v.getModelo() + ": revisar neumáticos pronto.");
        }
    }

    private void crearNotificacion(Vehiculo v, Usuario u, Notificacion.TipoNotificacion tipo, String mensaje) {
        Notificacion n = new Notificacion();
        n.setVehiculo(v);
        n.setUsuario(u);
        n.setTipo(tipo);
        n.setMensaje(mensaje);
        n.setFecha(LocalDateTime.now());
        notificacionRepo.save(n);
    }

    public Map<String, Object> resumenAlertas(Long usuarioId, boolean isAdmin) {
        List<Vehiculo> todos = isAdmin
                ? vehiculoRepo.findByActivoTrue()
                : vehiculoRepo.findByPropietarioIdAndActivoTrue(usuarioId);

        List<Map<String, Object>> rojos = new ArrayList<>();
        List<Map<String, Object>> amarillos = new ArrayList<>();
        List<Map<String, Object>> verdes = new ArrayList<>();

        for (Vehiculo v : todos) {
            Map<String, Object> info = new LinkedHashMap<>();
            info.put("id", v.getId());
            info.put("nombre", v.getMarca() + " " + v.getModelo());
            info.put("matricula", v.getMatricula());
            info.put("propietarioNombre", v.getPropietario() != null ? v.getPropietario().getNombreCompleto() : "");
            info.put("kmActual", v.getKilometrajeActual());
            info.put("kmRestantes", v.getKmRestantesParaCambio());
            info.put("porcentajeVida", v.getPorcentajeVidaAceite());
            info.put("estado", v.getEstadoAceite().name());
            info.put("estadoDescripcion", v.getEstadoAceite().getDescripcion());
            info.put("colorHex", v.getEstadoAceite().getColorHex());
            info.put("diasEstimados", v.getDiasEstimadosHastaCambio(KM_DIARIOS_DEFAULT));

            switch (v.getEstadoAceite()) {
                case ROJO     -> rojos.add(info);
                case AMARILLO -> amarillos.add(info);
                case VERDE    -> verdes.add(info);
            }
        }

        Map<String, Object> r = new LinkedHashMap<>();
        r.put("timestamp", LocalDateTime.now().toString());
        r.put("totalVehiculos", todos.size());
        r.put("alertasUrgentes", rojos.size());
        r.put("alertasProximas", amarillos.size());
        r.put("vehiculosOk", verdes.size());
        r.put("rojos", rojos);
        r.put("amarillos", amarillos);
        r.put("verdes", verdes);
        return r;
    }

    //  NOTIFICACIONES

    public List<Notificacion> obtenerNotificaciones(Long usuarioId) {
        return notificacionRepo.findByUsuarioIdOrderByFechaDesc(usuarioId);
    }

    public long contarNoLeidas(Long usuarioId) {
        return notificacionRepo.countByUsuarioIdAndLeidaFalse(usuarioId);
    }

    public Optional<Notificacion> marcarLeida(Long notifId, Long usuarioId, String usuarioNombre, String rol) {
        return notificacionRepo.findById(notifId)
                .filter(n -> n.getUsuario().getId().equals(usuarioId))
                .map(n -> {
                    n.setLeida(true);
                    Notificacion saved = notificacionRepo.save(n);
                    auditService.registrar(usuarioId, usuarioNombre, rol,
                            TipoAccion.MARCAR_NOTIFICACION,
                            "Notificación marcada como leída: " + n.getMensaje().substring(0, Math.min(50, n.getMensaje().length())),
                            "Notificacion", notifId);
                    return saved;
                });
    }

    public void marcarTodasLeidas(Long usuarioId, String usuarioNombre, String rol) {
        List<Notificacion> noLeidas = notificacionRepo.findByUsuarioIdAndLeidaFalseOrderByFechaDesc(usuarioId);
        noLeidas.forEach(n -> n.setLeida(true));
        notificacionRepo.saveAll(noLeidas);
        auditService.registrar(usuarioId, usuarioNombre, rol,
                TipoAccion.MARCAR_NOTIFICACION,
                "Marcadas todas las notificaciones como leídas (" + noLeidas.size() + ")",
                "Notificacion", null);
    }

    //  MANTENIMIENTO

    public Optional<Notificacion> marcarNoLeida(Long notifId, Long usuarioId) {
        return notificacionRepo.findById(notifId).map(n -> {
            if (n.getUsuario() != null && n.getUsuario().getId().equals(usuarioId)) {
                n.setLeida(false);
                return notificacionRepo.save(n);
            }
            return n;
        });
    }

    public Optional<RegistroMantenimiento> registrarMantenimiento(Long vehiculoId, RegistroMantenimiento reg,
                                                                   Long usuarioId, String usuarioNombre, String rol, boolean isAdmin) {
        return vehiculoRepo.findById(vehiculoId)
                .filter(v -> isAdmin || v.getPropietario().getId().equals(usuarioId))
                .map(v -> {
                    reg.setVehiculo(v);
                    if (reg.getFechaMantenimiento() == null) reg.setFechaMantenimiento(LocalDateTime.now());
                    if (reg.getTipoMantenimiento() == RegistroMantenimiento.TipoMantenimiento.CAMBIO_ACEITE) {
                        v.setUltimoCambioAceiteKm(v.getKilometrajeActual());
                        v.setProximoCambioAceiteKm(v.getKilometrajeActual() + 10000);
                        vehiculoRepo.save(v);
                        crearNotificacion(v, v.getPropietario(), Notificacion.TipoNotificacion.SISTEMA_INFO,
                                "✅ Cambio de aceite realizado en " + v.getMarca() + " " + v.getModelo() +
                                " a los " + v.getKilometrajeActual().intValue() + " km. Próximo cambio: " +
                                v.getProximoCambioAceiteKm().intValue() + " km.");
                    }
                    RegistroMantenimiento saved = mantenimientoRepo.save(reg);
                    auditService.registrar(usuarioId, usuarioNombre, rol,
                            TipoAccion.REGISTRAR_MANTENIMIENTO,
                            reg.getTipoMantenimiento().getDescripcion() + " en " + v.getMarca() + " " + v.getModelo(),
                            "Mantenimiento", saved.getId());
                    return saved;
                });
    }

    public List<RegistroMantenimiento> obtenerHistorial(Long vehiculoId) {
        return mantenimientoRepo.findByVehiculoIdOrderByFechaMantenimientoDesc(vehiculoId);
    }

    //  USUARIOS

    public Optional<Usuario> obtenerUsuario(Long id) {
        return usuarioRepo.findById(id);
    }

    public boolean cambiarPassword(Long usuarioId, String passwordActual, String nuevaPassword,
                                   String usuarioNombre, String rol) {
        return usuarioRepo.findById(usuarioId).map(u -> {
            if (!u.getPassword().equals(passwordActual)) return false;
            u.setPassword(nuevaPassword);
            usuarioRepo.save(u);
            auditService.registrar(usuarioId, usuarioNombre, rol,
                    TipoAccion.CAMBIAR_PASSWORD,
                    "Contraseña actualizada correctamente",
                    "Usuario", usuarioId);
            return true;
        }).orElse(false);
    }

    public Map<String, Object> healthCheck() {
        long start = System.currentTimeMillis();
        Map<String, Object> h = new LinkedHashMap<>();
        h.put("status", "UP");
        h.put("app", "KeepCar v2.0.0");
        h.put("equipo", List.of("Amael Silva (Admin)", "El Almendra (Admin)", "Arturo Kavka (Admin)"));
        h.put("usuariosRegistrados", usuarioRepo.count());
        h.put("vehiculosRegistrados", vehiculoRepo.count());
        h.put("tiempoRespuesta_ms", System.currentTimeMillis() - start);
        h.put("timestamp", LocalDateTime.now().toString());
        return h;
    }
}
