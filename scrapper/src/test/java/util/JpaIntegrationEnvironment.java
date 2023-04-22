package util;


import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import ru.tinkoff.edu.java.scrapper.configuration.database.JpaAccessConfiguration;


@ContextConfiguration(classes = JpaAccessConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class JpaIntegrationEnvironment extends IntegrationEnvironment {
}
