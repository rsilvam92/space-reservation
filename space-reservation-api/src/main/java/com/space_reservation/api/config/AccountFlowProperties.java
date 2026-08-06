package com.space_reservation.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.account")
public record AccountFlowProperties(
        String webBaseUrl,
        long invitationExpirationHours
) {
}
