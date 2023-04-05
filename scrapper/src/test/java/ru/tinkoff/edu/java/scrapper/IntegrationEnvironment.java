package ru.tinkoff.edu.java.scrapper;


import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.core.PostgresDatabase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.DirectoryResourceAccessor;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.sql.DriverManager;
import java.sql.SQLException;

@Testcontainers
public abstract class IntegrationEnvironment {

    static final PostgreSQLContainer<?> DB_CONTAINER;
    static final Path CHANGELOG_PATH = new File("scrapper").toPath().toAbsolutePath().resolve("migrations");

    static {
        DB_CONTAINER = new PostgreSQLContainer<>("postgres:15")
                .withDatabaseName("scrapper")
                .withUsername("postgres")
                .withPassword("password");
        DB_CONTAINER.start();
    }

    private static void runMigrations(PostgreSQLContainer<?> c) {
        try (var conn = DriverManager.getConnection(c.getJdbcUrl(), c.getUsername(), c.getPassword())) {
            var changeLogDir = new DirectoryResourceAccessor(CHANGELOG_PATH);

            var db = new PostgresDatabase();
            db.setConnection(new JdbcConnection(conn));

            var liquibase = new Liquibase("master.xml", changeLogDir, db);
            liquibase.update(new Contexts(), new LabelExpression());

        }
        catch (SQLException | LiquibaseException | FileNotFoundException exception) {
            throw new RuntimeException(exception);
        }
     }

    public static void main(String[] args) {
        System.out.println(CHANGELOG_PATH);
    }

}
