package brocoeur.example;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class DefineQueues {

    private final RabbitAdmin rabbitAdmin;

    @Autowired
    private final ConfigProperties configProperties;

    @PostConstruct
    public void createQueue() {
        rabbitAdmin.declareQueue(new Queue(configProperties.getQueueName()));
        rabbitAdmin.declareBinding(new Binding(
                configProperties.getQueueName(),
                Binding.DestinationType.QUEUE,
                configProperties.getExchangeName(),
                configProperties.getRoutingKey(),
                new HashMap<>()));
    }
}
