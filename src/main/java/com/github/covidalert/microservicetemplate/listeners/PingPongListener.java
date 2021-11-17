package com.github.covidalert.microservicetemplate.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class PingPongListener
{

    @Autowired
    private KafkaTemplate<String, String> template;

    @KafkaListener(topics = "ping")
    public void onPing()
    {
        this.template.send("pong", "pong");
    }

}
