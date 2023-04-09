package ru.tinkoff.edu.java.scrapper;

import org.junit.jupiter.api.Test;
import util.IntegrationEnvironment;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class EnvironmentSetupTest extends IntegrationEnvironment {
    @Test
    void checkIfMigrationRun() {
        // given
        var SQL_QUERY = """
                WITH tables AS (SELECT table_name FROM information_schema.tables
                                         WHERE table_schema = 'public')
                SELECT ('link' IN (SELECT * FROM tables))
                        AND ('chat' IN (SELECT * FROM tables))
                        AND ('chat_link' IN (SELECT * FROM tables))
                """;

        try (var conn = DB_CONTAINER.createConnection("")) {
            // when
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(SQL_QUERY);

            // then
            assertTrue(result.next());
            assertTrue(result.getBoolean(1));
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

}
