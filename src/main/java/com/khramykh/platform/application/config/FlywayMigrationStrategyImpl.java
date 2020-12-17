package com.khramykh.platform.application.config;

import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.stereotype.Component;

@Component
public class FlywayMigrationStrategyImpl implements FlywayMigrationStrategy {
    @Override
    public void migrate(Flyway flyway) {
        flyway.migrate();
    }
}
