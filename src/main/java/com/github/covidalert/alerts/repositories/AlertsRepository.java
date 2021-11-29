package com.github.covidalert.alerts.repositories;

import com.github.covidalert.alerts.models.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AlertsRepository extends JpaRepository<Alert, Long>
{
    List<Alert> findByUserId(String userId);

    @Transactional
    Long deleteByIdAndUserId(Long id, String UserId);
}
