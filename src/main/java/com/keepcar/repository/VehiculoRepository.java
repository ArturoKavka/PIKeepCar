package com.keepcar.repository;

import com.keepcar.model.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {

    // Filtrado por propietario (control de acceso)
    @Query("SELECT v FROM Vehiculo v WHERE v.propietario.id = :propietarioId AND v.activo = true")
    List<Vehiculo> findByPropietarioIdAndActivoTrue(Long propietarioId);

    // Admin: todos
    List<Vehiculo> findByActivoTrue();

    // Alertas por usuario
    @Query("SELECT v FROM Vehiculo v WHERE v.activo = true AND v.propietario.id = :uid AND (v.proximoCambioAceiteKm - v.kilometrajeActual) <= 1000")
    List<Vehiculo> findAlertasProximasByUsuario(Long uid);

    @Query("SELECT v FROM Vehiculo v WHERE v.activo = true AND v.propietario.id = :uid AND v.kilometrajeActual >= v.proximoCambioAceiteKm")
    List<Vehiculo> findVencidosByUsuario(Long uid);

    // Admin: todas las alertas
    @Query("SELECT v FROM Vehiculo v WHERE v.activo = true AND (v.proximoCambioAceiteKm - v.kilometrajeActual) <= 1000")
    List<Vehiculo> findTodasAlertasProximas();

    @Query("SELECT v FROM Vehiculo v WHERE v.activo = true AND v.kilometrajeActual >= v.proximoCambioAceiteKm")
    List<Vehiculo> findTodosVencidos();
}
