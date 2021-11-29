package com.github.covidalert.alerts;

import com.github.covidalert.alerts.controllers.AlertsController;
import com.github.covidalert.alerts.models.Alert;
import com.github.covidalert.alerts.repositories.AlertsRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@WebMvcTest(controllers = AlertsController.class)
public class AlertsControllerTest
{

    private final Principal principal = () -> "user-id";

    private final static List<Alert> alertsList = new ArrayList<>();

    @BeforeAll
    public static void beforeAll()
    {
        var alert = new Alert("user-id", "alert message");
        alert.setId(0L);
        alertsList.add(alert);
    }

    @Autowired
    private AlertsController alertsController;

    @MockBean
    private AlertsRepository alertsRepository;

    @Test
    public void givenEmptyRepository_whenGetAlerts_shouldReturnEmptyList()
    {
        List<Alert> alerts = alertsController.getAlerts(principal);
        assertThat(alerts).isEmpty();
    }

    @Test()
    public void givenEmptyRepository_whenDeleteAlert_shouldReturnFalse()
    {
        assertThatThrownBy(() -> alertsController.deleteAlert(principal, "O"))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    public void givenRepositoryWithOneAlert_whenGetAlerts_shouldReturnOneAlert()
    {
        when(alertsRepository.findByUserId("user-id"))
                .thenReturn(alertsList);

        var obtainedAlertsList = alertsController.getAlerts(principal);
        assertThat(obtainedAlertsList).containsExactlyElementsOf(alertsList);
    }

    @Test
    public void givenRepositoryWithOneAlert_whenDeleteAlert_shouldReturnTrue()
    {
        when(alertsRepository.deleteByIdAndUserId(0L, "user-id"))
                .thenReturn(1L);

        alertsController.deleteAlert(principal, "0");
        verify(alertsRepository).deleteByIdAndUserId(eq(0L), eq("user-id"));
    }

}
