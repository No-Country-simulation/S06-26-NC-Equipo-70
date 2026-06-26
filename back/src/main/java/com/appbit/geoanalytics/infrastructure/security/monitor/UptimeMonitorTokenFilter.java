package com.appbit.geoanalytics.infrastructure.security.monitor;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class UptimeMonitorTokenFilter extends OncePerRequestFilter {

    private final MonitorTokenValidator monitorTokenValidator;

    public UptimeMonitorTokenFilter(MonitorTokenValidator monitorTokenValidator) {
        this.monitorTokenValidator = monitorTokenValidator;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String token = request.getParameter("t");

        if (!monitorTokenValidator.isValid(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(request, response);
    }
}