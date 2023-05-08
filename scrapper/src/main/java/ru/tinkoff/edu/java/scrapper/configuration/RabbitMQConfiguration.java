package ru.tinkoff.edu.java.scrapper.configuration;


import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RabbitMQConfiguration {

    private final ApplicationProperties properties;

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(properties.rabbitmq().exchange());
    }

    @Bean
    public Queue queue() {
        return QueueBuilder.durable(properties.rabbitmq().queue())
                .withArgument("x-dead-letter-exchange", properties.rabbitmq().dlx())
                .withArgument("x-dead-letter-routing-key", properties.rabbitmq().key())
                .build();
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue()).to(exchange()).with(properties.rabbitmq().key());
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }


    // needed to create queue/exchanges/bindings manually
    @Bean
    public AmqpAdmin amqpAdmin(ConnectionFactory connectionFactory) {
        var admin = new RabbitAdmin(connectionFactory);
        admin.declareExchange(exchange());
        admin.declareQueue(queue());
        admin.declareBinding(binding());
        return admin;
    }

}
