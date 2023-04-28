package ru.tinkoff.edu.java.scrapper.jpa;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaChatRepository;
import util.JpaIntegrationEnvironment;

@Slf4j
@Transactional
@Rollback
public class JpaChatRepositoryTest extends JpaIntegrationEnvironment {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    JpaChatRepository chatRepository;
}
