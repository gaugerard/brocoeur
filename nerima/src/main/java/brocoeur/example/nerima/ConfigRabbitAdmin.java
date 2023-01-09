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
    private NerimaConfigProperties nerimaConfigProperties;

    @Bean
    public RabbitAdmin admin() {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    Queue msgQueue() {
        //Somehow all the value from the configuration work but serviceRequestQueueName gives null every time wtf ??
        return new Queue("ServiceRequestQueue", false, false, true);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange("myexchange1");
    }

    @Bean
    Binding msgBinding() {
        return BindingBuilder.bind(msgQueue()).to(exchange()).with(nerimaConfigProperties.getServiceRequestQueueName());
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
