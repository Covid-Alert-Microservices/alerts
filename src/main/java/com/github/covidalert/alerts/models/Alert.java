package com.github.covidalert.alerts.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Alert
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String userId;

    private String message;

    public Alert()
    {
    }

    public Alert(String userId, String message)
    {
        this.userId = userId;
        this.message = message;
    }

    public Long getId()
    {
        return id;
    }

    public String getUserId()
    {
        return userId;
    }

    public String getMessage()
    {
        return message;
    }

    public void setId(Long id)
    {
        this.id = id;
    }
}
