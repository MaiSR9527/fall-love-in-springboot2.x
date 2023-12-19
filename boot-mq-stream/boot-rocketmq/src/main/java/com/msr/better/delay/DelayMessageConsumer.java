package com.msr.better.delay;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.consumer.rebalance.AllocateMessageQueueAveragely;
import org.apache.rocketmq.client.exception.MQClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Collectors;

/**
 * @date: 2023-12-19
 * @author: maisrcn@qq.com
 */
public class DelayMessageConsumer {

    private static final Logger log = LoggerFactory.getLogger(DelayMessageConsumer.class);

    public static void main(String[] args) throws MQClientException {
        DefaultMQPushConsumer pushConsumerCommonTopic = new DefaultMQPushConsumer("push_consumer_common_topic");
        pushConsumerCommonTopic.setAllocateMessageQueueStrategy(new AllocateMessageQueueAveragely());
        pushConsumerCommonTopic.setNamesrvAddr("localhost:9876");
        pushConsumerCommonTopic.subscribe("DelayTopic", "*");
        // 并发消费
        pushConsumerCommonTopic.registerMessageListener((MessageListenerConcurrently) (msgs, consumeConcurrentlyContext) -> {
            log.info("{} Receive New Messages: {}", System.currentTimeMillis(),
                    msgs.stream().map(messageExt -> new String(messageExt.getBody())).collect(Collectors.joining(","))
            );
            // 返回消息消费状态，ConsumeConcurrentlyStatus.CONSUME_SUCCESS为消费成功。RECONSUME_LATER表示消费失败，一段时间后再重新消费
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        pushConsumerCommonTopic.start();
        log.info("Consumer started success");
    }
}
