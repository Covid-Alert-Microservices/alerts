package com.github.covidalert.alerts;

import com.c4_soft.springaddons.security.oauth2.test.annotations.keycloak.*;
import com.github.covidalert.alerts.controllers.AlertsController;
import com.github.covidalert.alerts.models.Alert;
import com.github.covidalert.alerts.repositories.AlertsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakSecurityComponents;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.web.servlet.MockMvc;
import com.c4_soft.springaddons.security.oauth2.test.annotations.OpenIdClaims;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;


@SpringBootTest
@AutoConfigureMockMvc
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
@ComponentScan(basePackageClasses = {KeycloakSecurityComponents.class, KeycloakSpringBootConfigResolver.class})
public class IntegrationTests {

    final String mockUserId1 = "123-456";
    final String mockUserId2 = "654-321";
    final String mockMessage1 = "Lorem ipsum 1";
    final String mockMessage2 = "Lorem ipsum 2";

    @Autowired
    private AlertsController alertsController;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AlertsRepository alertsRepository;

    @BeforeEach()
    public void beforeEach()
    {
        alertsRepository.deleteAll();
    }

    @Test
    public void contextLoads()
    {
        assertThat(alertsController).isNotNull();
    }

    @Test
    public void testUnauthorizedError() throws Exception {mockMvc.perform(get("/api/")).andExpect(status().isUnauthorized());}

    @Test
    @WithMockKeycloakAuth(
            authorities = { "user" },
            claims = @OpenIdClaims(
                    sub = mockUserId1,
                    name = mockUserId1
            ),
            accessToken = @KeycloakAccessToken(
                    realmAccess = @KeycloakAccess(roles = { "user" }),
                    authorization = @KeycloakAuthorization
            )
    )
    public void testGetAllAlerts_someAlerts() throws Exception
    {
        // given
        Alert savedAlert = alertsRepository.save(new Alert(mockUserId1, mockMessage1));

        //when
        mockMvc.perform(get("/api/"))

        //then
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$").isNotEmpty())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].id", is(savedAlert.getId())))
        .andExpect(jsonPath("$[0].userId", is(savedAlert.getUserId())))
        .andExpect(jsonPath("$[0].message", is(savedAlert.getMessage())));
    }

    @Test
    public void testGetAllAlerts_noAlerts() throws Exception
    {
        // given

        //when
        mockMvc.perform(get("/api/"))

        //then
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    public void testDeleteAlertById_deleted() throws Exception
    {
        // given
        Alert savedAlert = alertsRepository.save(new Alert(mockUserId1, mockMessage1));

        //when
        mockMvc.perform(delete("/api/" + savedAlert.getId()))

        //then
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"));
    }

    @Test
    public void testDeleteAlertById_notFoundId() throws Exception
    {
        // given
        String fakeAlertId = "123456789";

        //when
        mockMvc.perform(delete("/api/" + fakeAlertId))

        //then
        .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteAlertById_notFoundCastError() throws Exception
    {
        // given
        String fakeAlertId = "fakeAlertId";

        //when
        mockMvc.perform(delete("/api/" + fakeAlertId))

        //then
        .andExpect(status().isNotFound());
    }

    @Test
    public void testBothRoutes_success() throws Exception
    {
        // given
        Alert savedAlert1 = alertsRepository.save(new Alert(mockUserId1, mockMessage1));
        Alert savedAlert2 = alertsRepository.save(new Alert(mockUserId1, mockMessage2));
        alertsRepository.save(new Alert(mockUserId2, mockMessage1));

        //when
        mockMvc.perform(get("/api/"))

        //then
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$").isNotEmpty())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].id", is(savedAlert1.getId())))
        .andExpect(jsonPath("$[0].userId", is(savedAlert1.getUserId())))
        .andExpect(jsonPath("$[0].message", is(savedAlert1.getMessage())))
        .andExpect(jsonPath("$[1].id", is(savedAlert2.getId())))
        .andExpect(jsonPath("$[1].userId", is(savedAlert2.getUserId())))
        .andExpect(jsonPath("$[1].message", is(savedAlert2.getMessage())));


        //when
        mockMvc.perform(delete("/api/" + savedAlert2.getId()))

        //then
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"));


        //when
        mockMvc.perform(get("/api/"))

        //then
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$").isNotEmpty())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].id", is(savedAlert1.getId())))
        .andExpect(jsonPath("$[0].userId", is(savedAlert1.getUserId())))
        .andExpect(jsonPath("$[0].message", is(savedAlert1.getMessage())));
    }
}
