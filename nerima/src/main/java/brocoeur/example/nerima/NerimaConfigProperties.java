package brocoeur.example.nerima;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "nerima")
public class NerimaConfigProperties {

    private String rpcMessageQueue;
    private String rpcReplyMessageQueue;
    private String routingKey;
    private String rpcExchange;

    public String getRpcMessageQueue() {
        return rpcMessageQueue;
    }

    public void setRpcMessageQueue(final String rpcMessageQueue) {
        this.rpcMessageQueue = rpcMessageQueue;
    }

    public String getRpcReplyMessageQueue() {
        return rpcReplyMessageQueue;
    }

    public void setRpcReplyMessageQueue(final String rpcReplyMessageQueue) {
        this.rpcReplyMessageQueue = rpcReplyMessageQueue;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public void setRoutingKey(final String routingKey) {
        this.routingKey = routingKey;
    }

    public String getRpcExchange() {
        return rpcExchange;
    }

    public void setRpcExchange(final String rpcExchange) {
        this.rpcExchange = rpcExchange;
    }
}