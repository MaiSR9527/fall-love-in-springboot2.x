package top.maishuren.better.kafka.impl;

import com.msr.better.common.util.JsonUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import top.maishuren.better.common.KafkaTopic;
import top.maishuren.better.dto.UserLoginDto;
import top.maishuren.better.kafka.AbstractKafkaConsumerHandler;
import top.maishuren.better.kafka.KafkaConsumerHandler;
import top.maishuren.better.thread.KafkaConsumerThread;
import top.maishuren.better.thread.KafkaConsumerThreadPool;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2022/4/25
 */
@Component
public class UserLoginLogConsumer extends AbstractKafkaConsumerHandler<UserLoginDto> implements KafkaConsumerHandler<UserLoginDto> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserLoginLogConsumer.class);

    // 本地注入Dao/service 或 RPC调用接口

    /**注入自定义Kafka线程池*/
    @Autowired
    private KafkaConsumerThreadPool threadPool;

    @KafkaListener(topics = KafkaTopic.USER_LOGIN_LOG)
    public void listenMessage(ConsumerRecord<String, String> record) {
        // 交给线程池处理
        LOGGER.info("receive a message: {} from topic: {}", JsonUtils.toJsonString(record), KafkaTopic.USER_LOGIN_LOG);
        threadPool.run(new KafkaConsumerThread<>(record, new UserLoginLogConsumer()));
    }

    @Override
    public void handlerMessage(UserLoginDto userLoginDto) {
        // DB insert user login log
    }

    @Override
    public void executeConsumer(UserLoginDto value) {

    }
}
