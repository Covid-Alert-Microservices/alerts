package com.github.covidalert.alerts;

import com.c4_soft.springaddons.security.oauth2.test.annotations.OpenIdClaims;
import com.c4_soft.springaddons.security.oauth2.test.annotations.keycloak.KeycloakAccess;
import com.c4_soft.springaddons.security.oauth2.test.annotations.keycloak.KeycloakAccessToken;
import com.c4_soft.springaddons.security.oauth2.test.annotations.keycloak.WithMockKeycloakAuth;
import com.c4_soft.springaddons.security.oauth2.test.mockmvc.MockMvcSupport;
import com.c4_soft.springaddons.security.oauth2.test.mockmvc.keycloak.ServletKeycloakAuthUnitTestingSupport;
import com.github.covidalert.alerts.controllers.AlertsController;
import com.github.covidalert.alerts.models.Alert;
import com.github.covidalert.alerts.repositories.AlertsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AlertsController.class)
@Import({
        ServletKeycloakAuthUnitTestingSupport.UnitTestConfig.class,
        KeycloakSecurityConfig.class,
        TestConfig.class
})
public class AlertsControllerWebTest
{

    @Autowired
    private AlertsController alertsController;

    @MockBean
    private AlertsRepository alertsRepository;

    @Autowired
    MockMvcSupport api;

    @BeforeEach
    public void beforeEach()
    {
        when(alertsRepository.findAll()).thenReturn(new ArrayList<>());
    }

    @Test
    public void givenUnauthenticatedUser_whenGetAndDeleteAlerts_shouldBeUnauthorized() throws Exception
    {
        api.get("/api")
                .andExpect(status().isUnauthorized());
        api.delete("/api/0")
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockKeycloakAuth
    public void givenAuthenticatedUserWithoutRole_whenGetAndDeleteAlerts_shouldBeForbidden() throws Exception
    {
        api.get("/api")
                .andExpect(status().isForbidden());
        api.delete("/api/0")
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockKeycloakAuth(
            claims = @OpenIdClaims(
                    sub = "user-id",
                    preferredUsername = "username"
            ),
            accessToken = @KeycloakAccessToken(
                    realmAccess = @KeycloakAccess(
                            roles = "user"
                    )
            )
    )
    public void givenAuthenticatedUserWithRole_whenGetAndDeleteAlerts_shouldBeAuthorized() throws Exception
    {
        api.get("/api")
                .andExpect(status().isOk());
        api.delete("/api/0")
                .andExpect(status().isOk()); // Here getting a NotFound indicates we're authorized
    }

    @Test
    @WithMockKeycloakAuth("user")
    public void givenOneAlertInRepository_whenGetAlerts_shouldReturnOneAlert() throws Exception
    {
        when(alertsRepository.findByUserId(any()))
                .thenReturn(
                        List.of(new Alert("user-id", "alert-message"))
                );

        api.get("/api")
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", nullValue()))
                .andExpect(jsonPath("$[0].userId", is("user-id")))
                .andExpect(jsonPath("$[0].message", is("alert-message")));
    }

    @Test
    @WithMockKeycloakAuth("user")
    public void givenNoAlertInRepository_whenGetAlerts_shouldReturnEmptyArray() throws Exception
    {
        when(alertsRepository.findByUserId(any()))
                .thenReturn(List.of());

        api.get("/api")
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @WithMockKeycloakAuth("user")
    public void givenOneAlertInRepository_whenDeleteAlert_shouldReturnTrue() throws Exception
    {
        when(alertsRepository.deleteByIdAndUserId(eq(0L), any()))
                .thenReturn(1);

        api.delete("/api/0")
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    @WithMockKeycloakAuth("user")
    public void givenUnknownAlertId_whenDeleteAlert_shouldReturnNotFound() throws Exception
    {
        doThrow(new RuntimeException("Not found"))
                .when(alertsRepository).deleteByIdAndUserId(eq(4L), any());

        api.delete("/api/4")
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockKeycloakAuth("user")
    public void givenMalformedAlertId_whenDeleteAlert_shouldReturnNotFound() throws Exception
    {
        api.delete("/api/not-a-long")
                .andExpect(status().isNotFound());
    }

}
