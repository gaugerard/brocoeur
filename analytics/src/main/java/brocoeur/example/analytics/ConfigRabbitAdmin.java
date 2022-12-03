package brocoeur.example.analytics;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
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

    /**
     * For AnalyticRequests
     */
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

    /**
     * For GamblingRequests
     */
    @Bean
    Queue msgAQueue() {
        return new Queue("MyA1", false, false, true);
    }

    @Bean
    DirectExchange exchangeA() {
        return new DirectExchange("A1DirectExchange", false, true);
    }

    @Bean
    Binding msgBindingA() {
        return BindingBuilder.bind(msgAQueue()).to(exchangeA()).with("MyA1");
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
