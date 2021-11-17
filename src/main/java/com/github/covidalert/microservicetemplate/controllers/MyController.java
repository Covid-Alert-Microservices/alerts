package com.github.covidalert.microservicetemplate.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class MyController
{

    @GetMapping
    public Map<String, String> getName(Principal principal)
    {
        var attributes = new HashMap<String, String>();
        attributes.put("username", principal.getName());
        return attributes;
    }

}
