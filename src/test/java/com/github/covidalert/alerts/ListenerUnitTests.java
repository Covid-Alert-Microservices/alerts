package com.github.covidalert.alerts;

import com.github.covidalert.alerts.listeners.AlertsListener;
import com.github.covidalert.alerts.models.Alert;
import com.github.covidalert.alerts.repositories.AlertsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
public class ListenerUnitTests {

    private final String mockUserId = "2134-345";

    @Autowired
    private AlertsListener alertsListener;

    @Autowired
    private AlertsRepository alertsRepository;

    @BeforeEach
    public void initialize(){
        alertsRepository.deleteAll();
    }

    @Test
    public void testOnSendAlert() {
        // given

        // when
        alertsListener.onSendAlert(mockUserId);
        List<Alert> alerts = alertsRepository.findByUserId(mockUserId);

        // then
        assertThat(alerts).hasSize(1);
    }
}
