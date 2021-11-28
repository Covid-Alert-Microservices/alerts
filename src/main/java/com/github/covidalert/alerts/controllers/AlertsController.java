package com.github.covidalert.alerts.controllers;

import com.github.covidalert.alerts.models.Alert;
import com.github.covidalert.alerts.repositories.AlertsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AlertsController
{
    @Autowired
    private AlertsRepository alertsRepository;

    @GetMapping
    public List<Alert> getAlerts(Principal principal)
    {
        String userId = principal.getName();
        return alertsRepository.findByUserId(userId);
    }

    @DeleteMapping("/{alertId}")
    public boolean deleteAlert(Principal principal, @PathVariable("alertId") String sAlertId) throws ResponseStatusException
    {
        String userId = principal.getName();
        try
        {
            Long alertId = Long.parseLong(sAlertId);
            alertsRepository.deleteByIdAndUserId(alertId, userId);
            return true;
        }
        catch (Exception e)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Alert not found");
        }
    }

}
