package com.github.covidalert.alerts;

import com.github.covidalert.alerts.controllers.AlertsController;
import com.github.covidalert.alerts.models.Alert;
import com.github.covidalert.alerts.repositories.AlertsRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*@WebMvcTest(AlertsController.class)*/
public class ControllerUnitTests {

    /*final String mockUserId = "2134-345";
    final String mockOtherId = "1234-653";
    final String mockMessage = "You are contact case";
    final List<Alert> alertList = List.of(new Alert[]{new Alert(mockUserId, mockMessage), new Alert(mockOtherId, mockMessage)});

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AlertsRepository alertsRepository;

    @Test
    public void testrootRoute() throws Exception {
        when(alertsRepository.findByUserId((String)notNull())).thenReturn(alertList);
        mockMvc.perform(get("/api")).andDo(System.out::println).andExpect(status().isOk());
    }

    @Test
    public void testAlertIdRoute() throws Exception {
        when(alertsRepository.findByUserId((String)notNull())).thenReturn(alertList);
        mockMvc.perform(get("/api/" + 1)).andDo(System.out::println).andExpect(status().isOk());
    }*/
}
