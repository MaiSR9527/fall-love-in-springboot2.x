package top.maishuren.better.thread;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import top.maishuren.better.kafka.AbstractKafkaConsumerHandler;

import java.util.Optional;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2022/4/25
 */
public class KafkaConsumerThread<T> implements Runnable{

    private final ConsumerRecord<String, String> consumerRecord;
    private final AbstractKafkaConsumerHandler abstractKafkaConsumerHandler;

    public KafkaConsumerThread(ConsumerRecord<String, String> consumerRecord, AbstractKafkaConsumerHandler abstractKafkaConsumerHandler) {
        this.consumerRecord = consumerRecord;
        this.abstractKafkaConsumerHandler = abstractKafkaConsumerHandler;
    }

    @Override
    public void run() {
        try {
            if(null == consumerRecord ){
                return;
            }
            Optional<?> optional = Optional.ofNullable(consumerRecord.value());
            if (!optional.isPresent()) {
                return;
            }
            abstractKafkaConsumerHandler.onMessage(consumerRecord);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
