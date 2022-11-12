package brocoeur.example.nerima;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
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
    private final NerimaConfigProperties nerimaConfigProperties;

    @Bean
    public RabbitAdmin admin() {
        return new RabbitAdmin(connectionFactory);
    }

    /**
     * <h1><u>This section concerning the DIRECT game request:</u></h1>
     * <p>
     * Set sending RPCQueue message
     * Configure the Send Message Queue
     */
    @Bean
    Queue msgQueue() {
        return new Queue(nerimaConfigProperties.getRpcMessageQueue(), false, false, true);
    }

    /**
     * Return Queue Configuration
     */
    @Bean
    Queue replyQueue() {
        return new Queue(nerimaConfigProperties.getRpcReplyMessageQueue(), false, false, true);
    }

    /**
     * Switch setting
     */
    @Bean
    TopicExchange exchange() {

        return new TopicExchange(nerimaConfigProperties.getRpcExchange());
    }

    /**
     * Queuing and Switch Link Request
     */
    @Bean
    Binding msgBinding() {

        return BindingBuilder.bind(msgQueue()).to(exchange()).with(nerimaConfigProperties.getRpcMessageQueue());
    }

    /**
     * Back to Queue and Switch Link
     */
    @Bean
    Binding replyBinding() {

        return BindingBuilder.bind(replyQueue()).to(exchange()).with(nerimaConfigProperties.getRpcReplyMessageQueue());
    }

    /**
     * Use RabbitTemplate Send and receive messages
     * And set callback queue address
     */
    @Bean
    RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {

        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setReplyAddress(nerimaConfigProperties.getRpcReplyMessageQueue());
        template.setReplyTimeout(6000);
        return template;
    }

    /**
     * Configure listener for return queue
     */
    @Bean
    SimpleMessageListenerContainer replyContainer(final ConnectionFactory connectionFactory) {

        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(nerimaConfigProperties.getRpcReplyMessageQueue());
        container.setMessageListener(rabbitTemplate(connectionFactory));
        return container;
    }
}
