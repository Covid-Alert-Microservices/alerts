package com.github.covidalert.alerts.listeners;

import com.github.covidalert.alerts.models.Alert;
import com.github.covidalert.alerts.repositories.AlertsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class AlertsListener
{
    @Autowired
    private AlertsRepository alertsRepository;

    @KafkaListener(topics = "send_alert")
    public void onSendAlert(String userId)
    {
        Alert alert = new Alert(userId, "You are considered as contact case. Please stay at home.");
        alertsRepository.save(alert);
    }

}
