package com.msr.better.delay;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * @date: 2023-12-19
 * @author: maisrcn@qq.com
 */
public class DelayMessageProducer {

    public static void main(String[] args) throws MQClientException, MQBrokerException, RemotingException, InterruptedException {
        DefaultMQProducer producer = new DefaultMQProducer("delay_message_producer_group");
        producer.setNamesrvAddr("localhost:9876");
        producer.start();

        int sendNum = 100;
        for (int i = 0; i < sendNum; i++) {
            Message message = new Message("DelayTopic", "delay", ("delay message " + i).getBytes(StandardCharsets.UTF_8));
            // https://rocketmq.apache.org/zh/docs/4.x/producer/04message3 投递的延迟级别
            message.setDelayTimeLevel(3);
            producer.send(message);
            TimeUnit.MILLISECONDS.sleep(1000);
        }
        producer.shutdown();
    }
}
