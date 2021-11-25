package com.github.covidalert.alerts;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;

@TestConfiguration
public class TestConfig
{

    @Bean
    public GrantedAuthoritiesMapper authoritiesMapper()
    {
        return new SimpleAuthorityMapper();
    }

}
