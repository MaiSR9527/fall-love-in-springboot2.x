package com.msr.better.simple;

import cn.hutool.core.collection.CollUtil;
import org.apache.rocketmq.client.consumer.DefaultLitePullConsumer;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.consumer.rebalance.AllocateMessageQueueAveragely;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @date: 2023-12-15
 * @author: maisrcn@qq.com
 */
public class ConsumeCommonMessage {

    private static final Logger log = LoggerFactory.getLogger(ConsumeCommonMessage.class);

    public static void main(String[] args) throws MQClientException {
        // push消费模式。服务端主动推送消息给客户端，优点是及时性较好，但是如果客户端没有做好流控。一旦服务端推送了大量的消息，客户端可能会消息堆积甚至崩溃。
        // pushConsume();
        // pull消费模式
        DefaultLitePullConsumer defaultLitePullConsumer = new DefaultLitePullConsumer("pull_consumer_order_topic");
        defaultLitePullConsumer.setNamesrvAddr("localhost:9876");
        defaultLitePullConsumer.subscribe("TopicTest", "*");
        defaultLitePullConsumer.setPullBatchSize(10);
        defaultLitePullConsumer.start();
        while (true) {
            try {
                List<MessageExt> messageExtList = defaultLitePullConsumer.poll(300);
                if (CollUtil.isEmpty(messageExtList)) {
                    TimeUnit.SECONDS.sleep(3);
                } else {
                    for (MessageExt messageExt : messageExtList) {
                        log.info("{} consume success {}", Thread.currentThread().getName(), messageExt);
                        log.info("message body {}", new String(messageExt.getBody()));
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void pushConsume() throws MQClientException {
        DefaultMQPushConsumer pushConsumerCommonTopic = new DefaultMQPushConsumer("push_consumer_common_topic");
        // 集群模式下，消费者分配策略：平均分配策略、机房优先匹配策略、一致性hash匹配策略等。
        // topic下的队列如何分配给消费者。https://rocketmq.apache.org/zh/docs/4.x/consumer/01concept2
        pushConsumerCommonTopic.setAllocateMessageQueueStrategy(new AllocateMessageQueueAveragely());
        pushConsumerCommonTopic.setNamesrvAddr("localhost:9876");
        pushConsumerCommonTopic.subscribe("COMMON_MESSAGE_TOPIC", "*");
        // 并发消费
        pushConsumerCommonTopic.registerMessageListener((MessageListenerConcurrently) (msgs, consumeConcurrentlyContext) -> {
            log.info("{} Receive New Messages: {}", Thread.currentThread().getName(), msgs);
            // 返回消息消费状态，ConsumeConcurrentlyStatus.CONSUME_SUCCESS为消费成功。RECONSUME_LATER表示消费失败，一段时间后再重新消费
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        // 顺序消费
        // pushConsumerCommonTopic.registerMessageListener((MessageListenerOrderly) (msgs, ConsumeOrderlyContext) -> {
        //     log.info("{} Receive New Messages: {}", Thread.currentThread().getName(), msgs);
        //     // 返回消息消费状态，ConsumeOrderlyStatus.SUCCESS为消费成功。SUSPEND_CURRENT_QUEUE_A_MOMENT表示消费失败，一段时间后再重新消费
        //     return ConsumeOrderlyStatus.SUCCESS;
        // });

        // 集群模式和广播模式是在消费者指定的。
        // 集群模式，每个消费者组只有一个消费者会收到消息。push消费默认是集群模式。
        // pushConsumerCommonTopic.setMessageModel(MessageModel.CLUSTERING);
        // 广播模式，每个消费者都能收到消息。
        // pushConsumerCommonTopic.setMessageModel(MessageModel.BROADCASTING);
        pushConsumerCommonTopic.start();
        log.info("Consumer started success");
    }
}
