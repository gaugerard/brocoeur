package brocoeur.example;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
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

    public static final String RPC_MESSAGE_QUEUE = "MyQ1";
    public static final String RPC_REPLY_MESSAGE_QUEUE = "MyQ2";
    public static final String RPC_EXCHANGE = "myexchange1";

    /**
     * Set sending RPCQueue message
     * Configure the Send Message Queue
     */
    @Bean
    Queue msgQueue() {

        return new Queue(RPC_MESSAGE_QUEUE);
    }

    /**
     * Return Queue Configuration
     */
    @Bean
    Queue replyQueue() {

        return new Queue(RPC_REPLY_MESSAGE_QUEUE);
    }

    /**
     * Switch setting
     */
    @Bean
    TopicExchange exchange() {

        return new TopicExchange(RPC_EXCHANGE);
    }

    /**
     * Queuing and Switch Link Request
     */
    @Bean
    Binding msgBinding() {

        return BindingBuilder.bind(msgQueue()).to(exchange()).with(RPC_MESSAGE_QUEUE);
    }

    /**
     * Back to Queue and Switch Link
     */
    @Bean
    Binding replyBinding() {

        return BindingBuilder.bind(replyQueue()).to(exchange()).with(RPC_REPLY_MESSAGE_QUEUE);
    }

    /**
     * Use RabbitTemplate Send and receive messages
     * And set callback queue address
     */
    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {

        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setReplyAddress(RPC_REPLY_MESSAGE_QUEUE);
        template.setReplyTimeout(6000);
        return template;
    }

    /**
     * Configure listener for return queue
     */
    @Bean
    SimpleMessageListenerContainer replyContainer(ConnectionFactory connectionFactory) {

        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(RPC_REPLY_MESSAGE_QUEUE);
        container.setMessageListener(rabbitTemplate(connectionFactory));
        return container;
    }
}
