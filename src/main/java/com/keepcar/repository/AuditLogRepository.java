package com.keepcar.repository;

import com.keepcar.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findTop200ByOrderByTimestampDesc();
    List<AuditLog> findByUsuarioIdOrderByTimestampDesc(Long usuarioId);

    // FIX: @Param es obligatorio con queries JPQL nombradas en Spring Data JPA
    @Query("SELECT a FROM AuditLog a WHERE a.id > :sinceId ORDER BY a.id ASC")
    List<AuditLog> findNewSince(@Param("sinceId") Long sinceId);
}
