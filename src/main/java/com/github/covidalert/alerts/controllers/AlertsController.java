package com.github.covidalert.alerts.controllers;

import com.github.covidalert.alerts.models.Alert;
import com.github.covidalert.alerts.repositories.AlertsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RestController
@RequestMapping("/api")
public class AlertsController
{
    @Autowired
    private AlertsRepository alertsRepository;

    @GetMapping
    public Alert[] getAlerts(Principal principal)
    {
        String userId = principal.getName();
        return alertsRepository.findAllFromUserId(userId);
    }

    @DeleteMapping("{alertId}")
    public Alert deleteAlert(Principal principal, @PathVariable("alertId") String sAlertId)
    {
        String userId = principal.getName();
        try{
            Long alertId = Long.parseLong(sAlertId);
            return alertsRepository.deleteByIdAndUserId(alertId,userId);
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "alert not found");
        }
    }

}
