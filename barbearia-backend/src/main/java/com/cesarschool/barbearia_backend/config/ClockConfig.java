package com.cesarschool.barbearia_backend.config;

import java.time.Clock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClockConfig {
    @Bean
    Clock systemClock() {
        return Clock.systemDefaultZone();
    }
}