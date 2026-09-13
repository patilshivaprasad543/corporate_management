package com.corporate.travel;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@ActiveProfiles("test")
class FlywayConfigurationTest {

    @Autowired
    private Environment environment;

    @Test
    void flywayDisabledInTestProfile() {
        assertEquals("false", environment.getProperty("spring.flyway.enabled"));
    }

    @Test
    void hibernateCreateDropInTestProfile() {
        assertEquals("create-drop", environment.getProperty("spring.jpa.hibernate.ddl-auto"));
    }

    @Test
    void applicationContextLoads() {
        assertFalse(environment.getActiveProfiles().length == 0);
    }
}
