package com.github.covidalert.alerts;

import com.github.covidalert.alerts.controllers.AlertsController;
import com.github.covidalert.alerts.models.Alert;
import com.github.covidalert.alerts.repositories.AlertsRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.security.Principal;
import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) // Keycloak off
public class ControllerUnitTests {

    class PrincipalImpl implements Principal {
        @Override
        public String getName() { return "123-456"; }
    }

    final String mockAlertId = "1234";
    final List<Alert> mockAlerts = List.of(new Alert());
    final Principal principal = new PrincipalImpl();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AlertsController alertsController;

    @MockBean
    private AlertsRepository alertsRepository;

    @Test
    public void testGetAlerts(){
        // given

        // when
        List<Alert> alerts = alertsController.getAlerts(principal);

        // then
        assertThat(alerts).isEmpty();
    }

    @Test
    public void testDeleteAlert(){
        // when
        boolean deleted = alertsController.deleteAlert(principal, mockAlertId);

        // then
        assertThat(deleted).isFalse();
    }

    @Test
    public void testGetAllAlertsShouldBeCalled() throws Exception {
        // given
        when(alertsController.getAlerts(principal)).thenReturn(mockAlerts);

        //when
        mockMvc.perform(get("/api/"))

        //then
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    public void testDeleteAlertShouldBeCalled() throws Exception {
        // given
        when(alertsController.deleteAlert(principal, mockAlertId)).thenReturn(true);

        //when
        mockMvc.perform(delete("/api/" + mockAlertId).contentType(MediaType.APPLICATION_JSON))

        //then
        .andExpect(status().isOk());
    }

}
