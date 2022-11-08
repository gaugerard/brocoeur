package brocoeur.example.games;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "games")
public class GamesConfigProperties {

    private String rpcMessageQueue;
    private String rpcReplyMessageQueue;
    private String rpcExchange;

    public String getRpcMessageQueue() {
        return rpcMessageQueue;
    }

    public void setRpcMessageQueue(String rpcMessageQueue) {
        this.rpcMessageQueue = rpcMessageQueue;
    }

    public String getRpcReplyMessageQueue() {
        return rpcReplyMessageQueue;
    }

    public void setRpcReplyMessageQueue(String rpcReplyMessageQueue) {
        this.rpcReplyMessageQueue = rpcReplyMessageQueue;
    }

    public String getRpcExchange() {
        return rpcExchange;
    }

    public void setRpcExchange(String rpcExchange) {
        this.rpcExchange = rpcExchange;
    }
}