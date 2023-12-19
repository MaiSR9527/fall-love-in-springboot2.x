package com.msr.better.simple;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;

/**
 * @date: 2023-12-15
 * @author: maisrcn@qq.com
 */
public class SendOrderMessage {

    private static final Logger log = LoggerFactory.getLogger(SendOrderMessage.class);

    public static void main(String[] args) throws MQClientException, UnsupportedEncodingException, MQBrokerException, RemotingException, InterruptedException {
        // 要满足消息生产的顺序性，需要满足：
        // 1、单一生产者 如果是多个生产者的话，即使设置了相同的分区键，不同生产者之间发送的消息也无法判断先后顺序
        // 2、串行发送 生产者支持多线程安全访问，如果生产者使用多线程并行发送，那么不同线程发送的消息也无法判断先后顺序
        // 满足了以上两个条件后，顺序消息发送到服务端后，会保证设置统一分区键的消息，按照发送顺序存储在同一个队列中。
        DefaultMQProducer producer = new DefaultMQProducer("order_message_producer_group");
        producer.setNamesrvAddr("localhost:9876");
        producer.setMqClientApiTimeout(3 * 1000 * 3);
        producer.start();
        String[] tags = new String[]{"TagA", "TagB", "TagC", "TagD", "TagE"};
        for (int i = 0; i < 100; i++) {
            // 每10条消息会发送到一个队列
            int orderId = i % 10;
            // topic、tag、key
            Message msg = new Message("TopicOrder", tags[i % tags.length], "KEY" + i,
                    ("Order Message index " + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
            // orderId => arg  0-9
            // mqs 默认是每个 broker 上有四个队列。所以默认 mqs.size() = broker数量(例如：2) * 4
            SendResult sendResult = producer.send(msg, (mqs, msg1, arg) -> {
                // 实现 MessageQueueSelector 选择列队进行发送
                Integer id = (Integer) arg;
                // 0-9 % 8 => 0-8 queueId0 和 queueId1 会多比其他10条
                int index = id % mqs.size();
                return mqs.get(index);
            }, orderId);
            // 默认情况下，是保证可用性。如果有 broker 掉线 就会导致消息在分片的时候发生改变（broker数量发生改变，总的队列数变少）
            // 消息会被发送到不同的队列上，造成乱序。所以说默认情况下保证的是可用性

            // 如果需要保证一致性需要在创建 Topic 的时候设置 order=true。{@link org.apache.rocketmq.common.TopicConfig}。
            // 同时 NameServer 需要配置 orderMessageEnable=true returnOrderTopicConfigToBroker=true.
            // 如果都不满足，则保证的是可用性，而不是严格的有序
            log.info("send success: {}", sendResult);
        }

    }
}
