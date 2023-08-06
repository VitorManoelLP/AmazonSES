package com.moneyflow.flow.configuration;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationInitializer;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayConfig extends FlywayAutoConfiguration.FlywayConfiguration {

    @Override
    public FlywayMigrationInitializer flywayInitializer(Flyway flyway, ObjectProvider<FlywayMigrationStrategy> migrationStrategy) {
        flyway.repair();
        return super.flywayInitializer(flyway, migrationStrategy);
    }
}
