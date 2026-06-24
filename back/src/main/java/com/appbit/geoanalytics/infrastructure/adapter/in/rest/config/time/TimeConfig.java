package com.appbit.geoanalytics.infrastructure.adapter.in.rest.config.time;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
class TimeConfig {

    @Bean
    Clock clock() {
        return Clock.systemUTC();
    }
}
