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
public class AlertsRepositoryTest
{

    final String userOneId = "2134-345";
    final String userTwoId = "1234-653";
    final String mockMessage = "You are contact case";

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AlertsRepository alertsRepository;

    @BeforeEach
    public void initialize()
    {
        entityManager.clear();
    }

    @Test
    public void givenOneAlert_whenFindByUserId_shouldReturnIt()
    {
        Alert alert = new Alert(userOneId, mockMessage);
        entityManager.persist(alert);
        entityManager.flush();

        List<Alert> alerts = alertsRepository.findByUserId(userOneId);

        assertThat(alerts).hasSize(1).contains(alert);
    }

    @Test
    public void givenTwoAlertsOfSameUser_whenFindByUserId_shouldReturnBoth()
    {
        Alert alert1 = new Alert(userOneId, mockMessage);
        Alert alert2 = new Alert(userOneId, mockMessage);
        entityManager.persist(alert1);
        entityManager.persist(alert2);
        entityManager.flush();

        List<Alert> alerts = alertsRepository.findByUserId(userOneId);

        assertThat(alerts).hasSize(2).contains(alert1).contains(alert2);
    }

    @Test
    public void givenTwoAlertsOfDifferentUsers_whenFindByUserId_shouldReturnOneOfIt()
    {
        Alert alert1 = new Alert(userOneId, mockMessage);
        Alert alert2 = new Alert(userTwoId, mockMessage);
        entityManager.persist(alert1);
        entityManager.persist(alert2);
        entityManager.flush();

        List<Alert> alerts = alertsRepository.findByUserId(userOneId);

        assertThat(alerts).hasSize(1).contains(alert1).doesNotContain(alert2);
    }

    @Test
    public void givenOneAlertForAnUser_whenFindByIdOfAnotherUser_shouldReturnEmptyList()
    {
        Alert alert = new Alert(userTwoId, mockMessage);
        entityManager.persist(alert);
        entityManager.flush();

        List<Alert> alerts = alertsRepository.findByUserId(userOneId);

        assertThat(alerts).hasSize(0);
    }

    @Test
    public void givenOneAlert_whenDeleteByCorrectUserIdAndCorrectAlertId_shouldReturnOneIsDeleted()
    {
        Alert alert = new Alert(userOneId, mockMessage);
        Alert savedAlert = entityManager.persist(alert);
        entityManager.flush();
        Long alertId = savedAlert.getId();

        Long deletedAlert = alertsRepository.deleteByIdAndUserId(alertId, userOneId);

        assertThat(deletedAlert).isEqualTo(1L);
    }

    @Test
    public void givenOneAlert_whenDeleteByWrongUserIdAndCorrectAlertId_shouldReturnZeroIsDeleted()
    {
        Alert alert = new Alert(userOneId, mockMessage);
        Alert savedAlert = entityManager.persist(alert);
        entityManager.flush();
        Long alertId = savedAlert.getId();

        Long otherDeletedAlert = alertsRepository.deleteByIdAndUserId(alertId, userTwoId);

        assertThat(otherDeletedAlert).isEqualTo(0L);
    }

    @Test
    public void givenOneAlert_whenDeleteByCorrectUserIdAndWrongAlertId_shouldReturnZeroIsDeleted()
    {
        Alert alert = new Alert(userOneId, mockMessage);
        Alert savedAlert = entityManager.persist(alert);
        entityManager.flush();
        Long alertId = savedAlert.getId();

        Long deletedAlert = alertsRepository.deleteByIdAndUserId(alertId + 1, userOneId);

        assertThat(deletedAlert).isEqualTo(0L);
    }

}
