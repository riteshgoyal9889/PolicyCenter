package com.wipfli.training.repository;

import com.wipfli.training.model.Policy;
import com.wipfli.training.model.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PolicyRepository extends JpaRepository<Policy, String> {

    LocalDate REF_DATE = LocalDate.of(2026, 8, 9);

    List<Policy> findByPolicyHolderNameIgnoreCase(String name);

    List<Policy> findByVehicleType(VehicleType vehicleType);

    List<Policy> findByExpiryDateBetween(LocalDate startDate, LocalDate endDate);

    @Query("""
        select p
        from Policy p
        where p.expiryDate >= :startDate
          and p.expiryDate <= :endDate
        order by p.expiryDate asc
    """)
    List<Policy> findExpiringBetween(@Param("startDate") LocalDate startDate,
                                     @Param("endDate") LocalDate endDate);
}
