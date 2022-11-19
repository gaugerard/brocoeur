package brocoeur.example.analytics;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ConfigRabbitAdmin {

    private final ConnectionFactory connectionFactory;

    @Bean
    public RabbitAdmin admin() {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    Queue msgQueue() {
        return new Queue("analyticInput", false, false, true);
    }

    @Bean
    DirectExchange exchange() {
        return new DirectExchange("analyticDirectExchange", false, true);
    }

    @Bean
    Binding msgBinding() {
        return BindingBuilder.bind(msgQueue()).to(exchange()).with("analyticInput");
    }

    @Bean
    RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }

    // This generates the exchange and queue at module start.
    @Bean
    SimpleMessageListenerContainer replyContainer(final ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        return container;
    }
}
