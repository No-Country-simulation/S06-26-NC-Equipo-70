package com.appbit.geoanalytics.infrastructure.config.monitor;

import com.appbit.geoanalytics.infrastructure.security.monitor.MonitorTokenValidator;
import com.appbit.geoanalytics.infrastructure.security.monitor.UptimeMonitorTokenFilter;
import jakarta.servlet.DispatcherType;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import java.util.EnumSet;
import java.util.List;

@Configuration
public class UptimeMonitorFilterConfig {

    @Bean
    public FilterRegistrationBean<UptimeMonitorTokenFilter> uptimeMonitorTokenFilter(
            MonitorTokenValidator monitorTokenValidator
    ) {
        FilterRegistrationBean<UptimeMonitorTokenFilter> registration =
                new FilterRegistrationBean<>();

        registration.setFilter(new UptimeMonitorTokenFilter(monitorTokenValidator));
        registration.setName("uptimeMonitorTokenFilter");
        registration.setUrlPatterns(List.of("/internal/uptime/db"));
        registration.setDispatcherTypes(EnumSet.of(DispatcherType.REQUEST));
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);

        return registration;
    }
}
