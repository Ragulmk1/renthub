package com.rental.property.repo;

import com.rental.property.entity.Maintenance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaintenanceRepository extends JpaRepository<Maintenance, Long> {

    @Query("SELECT m FROM Maintenance m WHERE m.property.user.id = :landlordId")
    List<Maintenance> findByProperty_User_Id(@Param("landlordId") Long landlordId);

    @Query("SELECT m FROM Maintenance m WHERE m.tenant.id = :tenantId")
    List<Maintenance> findByTenant_Id(@Param("tenantId") Long tenantId);


}