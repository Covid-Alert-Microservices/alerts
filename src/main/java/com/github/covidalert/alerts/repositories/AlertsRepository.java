package com.github.covidalert.alerts.repositories;

import com.github.covidalert.alerts.models.Alert;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface AlertsRepository extends JpaRepository<Alert, Long>
{
    @Query(value = "SELECT * FROM Alert WHERE userId = :userId ", nativeQuery = true)
    Alert[] findAllFromUserId(@Param("userId") String userId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "DELETE FROM Alert WHERE id = :id AND userId = :userId", nativeQuery = true)
    Alert deleteByIdAndUserId(@Param("id") Long alertId, @Param("userId") String userId);
}
