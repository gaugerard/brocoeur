package brocoeur.example;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class DefineQueues {

    private final RabbitAdmin rabbitAdmin;

    @PostConstruct
    public void createQueue() {
        rabbitAdmin.declareQueue(new Queue("MyQ1"));
        rabbitAdmin.declareBinding(new Binding(
                "MyQ1",
                Binding.DestinationType.QUEUE,
                "myexchange1",
                "",
                new HashMap<>()));
    }
}
