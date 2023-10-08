package notification.mq;

import notification.config.Queues;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Component
public class RabbitSend {

    @Autowired
    private AmqpTemplate rabbitTemplate;
    private static final Logger logger = LogManager.getLogger(RabbitSend.class);

    public void send(String val) {
        logger.info("send val:" + val);
        this.rabbitTemplate.convertAndSend(Queues.queueName, val);
    }

}
