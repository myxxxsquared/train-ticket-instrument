package preserveOther.mq;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import preserveOther.config.Queues;


@Component
public class RabbitSend {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    private static final Logger logger = LogManager.getLogger(RabbitSend.class);

    public void send(String val) {
        logger.info("send info to mq:" + val);
        this.rabbitTemplate.convertAndSend(Queues.queueName, val);
    }

}
