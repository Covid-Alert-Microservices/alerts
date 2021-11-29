package com.github.covidalert.alerts.controllers;

import com.github.covidalert.alerts.models.Alert;
import com.github.covidalert.alerts.repositories.AlertsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

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

    @DeleteMapping("{alertId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAlert(Principal principal, @PathVariable("alertId") String sAlertId) throws ResponseStatusException
    {
        String userId = principal.getName();
        System.out.println("UserId: " + userId);
        try
        {
            Long alertId = Long.parseLong(sAlertId);
            System.out.println("AlertId: " + alertId);
            Optional<Alert> a = alertsRepository.findById(alertId);
            if(a.isPresent()) {
                System.out.println("AlertId is valid");
            }
            if (alertsRepository.deleteByIdAndUserId(alertId, userId) < 1)
            {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
        }
        catch (Exception e)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Alert not found");
        }
    }

}
