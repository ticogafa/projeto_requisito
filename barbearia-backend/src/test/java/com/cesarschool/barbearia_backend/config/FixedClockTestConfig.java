package com.cesarschool.barbearia_backend.config;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;


@TestConfiguration
public class FixedClockTestConfig {

  @Bean
  Clock clock() {
    // fixa “agora” em 22 (segunda-feira) de setembro de 2025, 09:00 (horário de Recife) para testes determinísticos
    ZoneId zone = ZoneId.of("America/Recife");
    Instant fixedInstant = LocalDateTime.of(2025, 9, 22, 9, 0).atZone(zone).toInstant();
    return Clock.fixed(fixedInstant, zone);
  }
}
