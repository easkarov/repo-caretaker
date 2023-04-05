package ru.tinkoff.edu.java.scrapper;

import org.junit.jupiter.api.Test;
import org.testcontainers.delegate.AbstractDatabaseDelegate;

import javax.swing.plaf.nimbus.State;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EnvironmentSetupTest extends IntegrationEnvironment {
    @Test
    void checkIfMigrationRun() throws SQLException {
        var SQL_QUERY = """
                WITH tables AS (SELECT * FROM information_schema.tables
                                         WHERE table_schema = 'public')
                SELECT ('link' IN (SELECT * FROM tables))
                        AND ('chat' IN (SELECT * FROM tables))
                        AND ('chat_link' IN (SELECT * FROM tables))""";

        try (var conn = DB_CONTAINER.createConnection("")) {
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(SQL_QUERY);;
            assertTrue(result.next());
            assertTrue(result.getBoolean(0));
        } ;
    }

}
