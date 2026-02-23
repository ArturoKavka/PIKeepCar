package com.keepcar.repository;

import com.keepcar.model.RegistroMantenimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MantenimientoRepository extends JpaRepository<RegistroMantenimiento, Long> {
    @Query("SELECT r FROM RegistroMantenimiento r WHERE r.vehiculo.id = :vehiculoId ORDER BY r.fechaMantenimiento DESC")
    List<RegistroMantenimiento> findByVehiculoIdOrderByFechaMantenimientoDesc(Long vehiculoId);
}
