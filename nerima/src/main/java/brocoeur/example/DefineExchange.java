package brocoeur.example;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class DefineExchange {

    private final RabbitAdmin rabbitAdmin;

    @Autowired
    private final ConfigProperties configProperties;

    @PostConstruct
    public void createExchangeAndQueues(){
        rabbitAdmin.declareExchange(ExchangeBuilder.topicExchange("myexchange1").build());

        rabbitAdmin.declareQueue(new Queue("MyQ1"));
        rabbitAdmin.declareBinding(new Binding(
                "MyQ1",
                Binding.DestinationType.QUEUE,
                "myexchange1",
                configProperties.getRoutingKey(),
                new HashMap<>()));

        rabbitAdmin.declareQueue(new Queue("MyQ2"));
        rabbitAdmin.declareBinding(new Binding(
                "MyQ2",
                Binding.DestinationType.QUEUE,
                "myexchange1",
                configProperties.getRoutingKey(),
                new HashMap<>()));
    }
}
