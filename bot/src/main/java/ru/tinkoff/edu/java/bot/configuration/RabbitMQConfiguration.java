package ru.tinkoff.edu.java.bot.configuration;


import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.bot.dto.LinkUpdate;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class RabbitMQConfiguration {

    private static final String LINK_UPDATE_FQN = "ru.tinkoff.edu.java.scrapper.dto.LinkUpdate";

    private final ApplicationProperties properties;

    @Bean
    public MessageConverter jsonMessageConverter() {
        Jackson2JsonMessageConverter jsonConverter = new Jackson2JsonMessageConverter();
        jsonConverter.setClassMapper(classMapper());
        return jsonConverter;
    }

    @Bean
    public DefaultClassMapper classMapper() {
        DefaultClassMapper classMapper = new DefaultClassMapper();
        Map<String, Class<?>> idClassMapping = new HashMap<>();
        idClassMapping.put(LINK_UPDATE_FQN, LinkUpdate.class);
        classMapper.setIdClassMapping(idClassMapping);
        return classMapper;
    }

    @Bean
    public DirectExchange dlx() {
        return new DirectExchange(properties.rabbitmq().dlx());
    }

    @Bean
    public Queue dlq() {
        return new Queue(properties.rabbitmq().dlq());
    }

    @Bean
    public Binding dlBinding() {
        return BindingBuilder.bind(dlq()).to(dlx()).with(properties.rabbitmq().key());
    }
}
