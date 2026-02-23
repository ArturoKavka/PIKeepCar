package com.keepcar.repository;

import com.keepcar.model.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
    @Query("SELECT n FROM Notificacion n WHERE n.usuario.id = :usuarioId ORDER BY n.fecha DESC")
    List<Notificacion> findByUsuarioIdOrderByFechaDesc(Long usuarioId);

    @Query("SELECT n FROM Notificacion n WHERE n.usuario.id = :usuarioId AND n.leida = false ORDER BY n.fecha DESC")
    List<Notificacion> findByUsuarioIdAndLeidaFalseOrderByFechaDesc(Long usuarioId);

    @Query("SELECT COUNT(n) FROM Notificacion n WHERE n.usuario.id = :usuarioId AND n.leida = false")
    long countByUsuarioIdAndLeidaFalse(Long usuarioId);
}
