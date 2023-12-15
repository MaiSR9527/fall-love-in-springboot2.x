package top.maishuren.better.kafka;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2022/4/25
 */
public interface KafkaConsumerHandler<T> {
    void handlerMessage(T t);
}
