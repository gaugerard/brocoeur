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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ConfigRabbitAdmin {

    private final ConnectionFactory connectionFactory;
    @Autowired
    private AnalyticsConfigProperties analyticsConfigProperties;

    @Bean
    public RabbitAdmin admin() {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    Queue msgQueue() {
        return new Queue(analyticsConfigProperties.getAnalyticInputQueueName(), false, false, true);
    }

    @Bean
    DirectExchange exchange() {
        return new DirectExchange("analyticDirectExchange", false, true);
    }

    @Bean
    Binding msgBinding() {
        return BindingBuilder.bind(msgQueue()).to(exchange()).with(analyticsConfigProperties.getAnalyticInputQueueName());
    }

    @Bean
    Queue msgAQueue() {
        return new Queue(analyticsConfigProperties.getNerimaToAnalyticsQueueName(), false, false, true);
    }

    @Bean
    DirectExchange exchangeA() {
        return new DirectExchange("A1DirectExchange", false, true);
    }

    @Bean
    Binding msgBindingA() {
        return BindingBuilder.bind(msgAQueue()).to(exchangeA()).with(analyticsConfigProperties.getNerimaToAnalyticsQueueName());
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
