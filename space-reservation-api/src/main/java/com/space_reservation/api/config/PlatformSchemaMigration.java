package com.space_reservation.api.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.regex.Pattern;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class PlatformSchemaMigration implements ApplicationRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlatformSchemaMigration.class);
    private static final Pattern SAFE_COLUMN_TYPE = Pattern.compile(
            "^[a-z0-9]+(?:\\(\\d+(?:,\\d+)?\\))?(?: unsigned)?$"
    );

    private final SuperAdminBootstrapProperties properties;
    private final JdbcTemplate jdbcTemplate;

    public PlatformSchemaMigration(
            SuperAdminBootstrapProperties properties,
            JdbcTemplate jdbcTemplate
    ) {
        this.properties = properties;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(ApplicationArguments arguments) {
        if (!properties.enabled()) {
            return;
        }

        String nullable = jdbcTemplate.queryForObject(
                """
                SELECT IS_NULLABLE
                FROM information_schema.COLUMNS
                WHERE TABLE_SCHEMA = DATABASE()
                  AND TABLE_NAME = 'usuario'
                  AND COLUMN_NAME = 'apartamento_id'
                """,
                String.class
        );

        if ("YES".equalsIgnoreCase(nullable)) {
            return;
        }

        String columnType = jdbcTemplate.queryForObject(
                """
                SELECT COLUMN_TYPE
                FROM information_schema.COLUMNS
                WHERE TABLE_SCHEMA = DATABASE()
                  AND TABLE_NAME = 'usuario'
                  AND COLUMN_NAME = 'apartamento_id'
                """,
                String.class
        );

        String safeColumnType = validateColumnType(columnType);
        jdbcTemplate.execute(
                "ALTER TABLE usuario MODIFY COLUMN apartamento_id " + safeColumnType + " NULL"
        );
        LOGGER.info("Migración aplicada: usuario.apartamento_id ahora permite NULL");
    }

    private String validateColumnType(String columnType) {
        if (columnType == null) {
            throw new IllegalStateException("No se encontró la columna usuario.apartamento_id");
        }

        String normalized = columnType.trim().toLowerCase(Locale.ROOT);
        if (!SAFE_COLUMN_TYPE.matcher(normalized).matches()) {
            throw new IllegalStateException("Tipo inesperado para usuario.apartamento_id: " + columnType);
        }
        return normalized;
    }
}
