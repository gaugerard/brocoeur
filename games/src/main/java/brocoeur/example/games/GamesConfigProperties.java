package brocoeur.example.games;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "games")
public class GamesConfigProperties {

    private String rpcMessageQueue;
    private String rpcReplyMessageQueue;
    private String rpcExchange;
    private String autoStartup;

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

    public String getRpcExchange() {
        return rpcExchange;
    }

    public void setRpcExchange(final String rpcExchange) {
        this.rpcExchange = rpcExchange;
    }

    public String getAutoStartup() {
        return autoStartup;
    }

    public void setAutoStartup(String autoStartup) {
        this.autoStartup = autoStartup;
    }
}