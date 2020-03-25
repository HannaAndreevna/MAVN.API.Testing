package com.lykke.api.testing.api.common.model.rabbitmq;

import com.rabbitmq.client.ConnectionFactory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.val;

@AllArgsConstructor
@Builder
@Data
public class RabbitMqData {

    private String host;
    private String exchange;
    private String queue;
    private String username;
    private String password;

    public ConnectionFactory getFactory() {
        val factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setUsername(username);
        factory.setPassword(password);
        return factory;
    }
}
