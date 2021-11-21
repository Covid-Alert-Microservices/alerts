package com.github.covidalert.alerts;

import com.github.covidalert.alerts.models.Alert;
import com.github.covidalert.alerts.repositories.AlertsRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class RepositoryUnitTests {

    final String mockUserId = "2134-345";
    final String mockOtherId = "1234-653";
    final String mockMessage = "You are contact case";

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AlertsRepository alertsRepository;

    @BeforeEach
    public void initialize(){
        entityManager.clear();
    }

    @Test
    public void testFindAllFromUserId_oneAlert() {
        // given
        Alert alert = new Alert(mockUserId,mockMessage);
        entityManager.persist(alert);
        entityManager.flush();

        // when
        List<Alert> alerts = alertsRepository.findByUserId(mockUserId);

        // then
        assertThat(alerts).hasSize(1).contains(alert);
    }

    @Test
    public void testFindAllFromUserId_someAlerts() {
        // given
        Alert alert1 = new Alert(mockUserId,mockMessage);
        Alert alert2 = new Alert(mockUserId,mockMessage);
        entityManager.persist(alert1);
        entityManager.persist(alert2);
        entityManager.flush();

        // when
        List<Alert> alerts = alertsRepository.findByUserId(mockUserId);

        // then
        assertThat(alerts).hasSize(2).contains(alert1).contains(alert2);
    }

    @Test
    public void testFindAllFromUserId_someOtherAlerts() {
        // given
        Alert alert1 = new Alert(mockUserId,mockMessage);
        Alert alert2 = new Alert(mockOtherId,mockMessage);
        entityManager.persist(alert1);
        entityManager.persist(alert2);
        entityManager.flush();

        // when
        List<Alert> alerts = alertsRepository.findByUserId(mockUserId);

        // then
        assertThat(alerts).hasSize(1).contains(alert1).doesNotContain(alert2);
    }

    @Test
    public void testFindAllFromUserId_noAlerts() {
        // given
        Alert alert = new Alert(mockOtherId,mockMessage);
        entityManager.persist(alert);
        entityManager.flush();

        // when
        List<Alert> alerts = alertsRepository.findByUserId(mockUserId);

        // then
        assertThat(alerts).hasSize(0);
    }

    @Test
    public void testDeleteByIdAndUser_oneAlert() {
        // given
        Alert alert = new Alert(mockUserId,mockMessage);
        Alert savedAlert = entityManager.persist(alert);
        entityManager.flush();
        Long alertId = savedAlert.getId();

        // when
        Integer deletedAlert = alertsRepository.deleteByIdAndUserId(alertId, mockUserId);
        Integer otherDeletedAlert = alertsRepository.deleteByIdAndUserId(alertId, mockOtherId);

        // then
        assertThat(deletedAlert).isEqualTo(1);
        assertThat(otherDeletedAlert).isEqualTo(0);
    }

    @Test
    public void testDeleteByIdAndUser_noAlert() {
        // given
        Alert alert = new Alert(mockUserId,mockMessage);
        Alert savedAlert = entityManager.persist(alert);
        entityManager.flush();
        Long alertId = savedAlert.getId();

        // when
        Integer deletedAlert = alertsRepository.deleteByIdAndUserId(alertId + 1, mockUserId);

        // then
        assertThat(deletedAlert).isEqualTo(0);
    }

}
