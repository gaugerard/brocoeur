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

    private String offlineRpcMessageQueue;
    private String offlineRpcReplyMessageQueue;
    private String offlineRpcExchange;

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

    public String getRoutingKey() {
        return routingKey;
    }

    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }

    public String getRpcExchange() {
        return rpcExchange;
    }

    public void setRpcExchange(String rpcExchange) {
        this.rpcExchange = rpcExchange;
    }

    public String getOfflineRpcMessageQueue() {
        return offlineRpcMessageQueue;
    }

    public void setOfflineRpcMessageQueue(String offlineRpcMessageQueue) {
        this.offlineRpcMessageQueue = offlineRpcMessageQueue;
    }

    public String getOfflineRpcReplyMessageQueue() {
        return offlineRpcReplyMessageQueue;
    }

    public void setOfflineRpcReplyMessageQueue(String offlineRpcReplyMessageQueue) {
        this.offlineRpcReplyMessageQueue = offlineRpcReplyMessageQueue;
    }

    public String getOfflineRpcExchange() {
        return offlineRpcExchange;
    }

    public void setOfflineRpcExchange(String offlineRpcExchange) {
        this.offlineRpcExchange = offlineRpcExchange;
    }
}