package com.github.covidalert.alerts;

import com.github.covidalert.alerts.listeners.AlertsListener;
import com.github.covidalert.alerts.models.Alert;
import com.github.covidalert.alerts.repositories.AlertsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
public class AlertsListenerTest
{

    @Autowired
    private AlertsListener alertsListener;

    @Autowired
    private AlertsRepository alertsRepository;

    @BeforeEach
    public void initialize()
    {
        alertsRepository.deleteAll();
    }

    @Test
    public void givenReceivedAlertMessage_whenFindAlertByUserId_shouldReturnOneAlert()
    {
        alertsListener.onSendAlert("user-id");
        List<Alert> alerts = alertsRepository.findByUserId("user-id");

        assertThat(alerts).hasSize(1);
    }
}
