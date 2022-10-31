package brocoeur.example;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@RequiredArgsConstructor
public class DefineExchange {

    private final RabbitAdmin rabbitAdmin;

    @PostConstruct
    public void createExchange(){
        rabbitAdmin.declareExchange(ExchangeBuilder.directExchange("myexchange1").build());
    }
}
