package com.lykke.api.testing.api.common;

import static com.lykke.api.testing.api.common.JsonConversionUtils.convertFromJson;
import static com.lykke.api.testing.api.common.JsonConversionUtils.convertToJson;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import com.lykke.api.testing.api.common.model.rabbitmq.RabbitMqData;
import java.util.concurrent.TimeUnit;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.val;
import org.awaitility.Awaitility;
import org.awaitility.Duration;

@UtilityClass
public class RabbitMqUtils {
    private static final int AWAITILITY_DEFAULT_MIN = 5;

    @SneakyThrows
    public static <E> void sendMessage(RabbitMqData data, E objectToSend) {
        try (val connection = data.getFactory().newConnection();
                val channel = connection.createChannel()) {

            channel.basicPublish(data.getExchange(), EMPTY, null, convertToJson(objectToSend).getBytes("UTF-8"));
            System.out.println(" [x] Sent '" + convertToJson(objectToSend) + "'");
        }
    }

    @SneakyThrows
    public static <E extends Object> void receiveMessage(RabbitMqData data, Class<E> type) {

        try (val connection = data.getFactory().newConnection();
                val channel = connection.createChannel()) {
            val queueName = channel.queueDeclare().getQueue();

            Awaitility.await().atMost(AWAITILITY_DEFAULT_MIN, TimeUnit.MINUTES)
                    .pollInterval(Duration.FIVE_SECONDS)
                    .until(() -> {
                        val response = channel.basicGet(queueName, false);
                        // channel.queueDeclare(queueName, false, false, false, null);
                        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

                        E message = null;
                        byte[] body = null;
                        if (null != response) {
                            body = response.getBody();
                            System.out.println(body);
                            val deliveryTag = response.getEnvelope().getDeliveryTag();
                            message = convertFromJson(body.toString(), type);
                        }
                        return null != message || null != body;
                    });
        }
    }
}
