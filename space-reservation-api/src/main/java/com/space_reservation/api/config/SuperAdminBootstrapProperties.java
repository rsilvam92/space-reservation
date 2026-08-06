package com.space_reservation.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.bootstrap.super-admin")
public record SuperAdminBootstrapProperties(
        boolean enabled,
        String email,
        String password,
        String document
) {
}
