package com.github.covidalert.microservicetemplate;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@EnableKafka
public class KafkaConfig
{

    @Bean
    public NewTopic ping()
    {
        return TopicBuilder.name("ping").config(TopicConfig.RETENTION_MS_CONFIG, "1").build();
    }

    @Bean
    public NewTopic pong()
    {
        return TopicBuilder.name("pong").config(TopicConfig.RETENTION_MS_CONFIG, "1").build();
    }

}
