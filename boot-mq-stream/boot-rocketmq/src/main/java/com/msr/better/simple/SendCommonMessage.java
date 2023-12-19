package com.msr.better.simple;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @date: 2023-12-15
 * @author: maisrcn@qq.com
 */
public class SendCommonMessage {

    private static final Logger log = LoggerFactory.getLogger(SendCommonMessage.class);

    public static void main(String[] args) throws Exception {
        // sendSync();
        sendAsesync();
    }

    private static void sendSync() throws Exception {
        DefaultMQProducer commonMessageProducer = new DefaultMQProducer("COMMON_MESSAGE_PRODUCER");
        commonMessageProducer.setNamesrvAddr("localhost:9876");
        commonMessageProducer.start();
        for (int i = 0; i < 100; i++) {
            Message message = new Message();
            message.setTopic("COMMON_MESSAGE_TOPIC");
            message.setTags("TagS");
            String msg = "This common Message producer";
            message.setBody(msg.getBytes(RemotingHelper.DEFAULT_CHARSET));
            // sync sending message
            SendResult sendResult = commonMessageProducer.send(message);
            log.info("发送成功：{}", sendResult);
        }
        // commonMessageProducer.shutdown();
    }

    private static void sendAsesync() throws Exception {
        // 初始化一个producer并设置Producer group name
        DefaultMQProducer producer = new DefaultMQProducer("please_rename_unique_group_name");
        // 设置NameServer地址
        producer.setNamesrvAddr("localhost:9876");
        // 启动producer
        producer.start();
        producer.setRetryTimesWhenSendAsyncFailed(0);
        int messageCount = 5;
        final CountDownLatch countDownLatch = new CountDownLatch(messageCount);
        for (int i = 0; i < messageCount; i++) {
            try {
                final int index = i;
                String body = "Hello world" + i;
                // 创建一条消息，并指定topic、tag、body等信息，tag可以理解成标签，对消息进行再归类，RocketMQ可以在消费端对tag进行过滤
                Message msg = new Message("TopicTest",
                        "TagA",
                        body.getBytes(RemotingHelper.DEFAULT_CHARSET));
                // 异步发送消息, 发送结果通过callback返回给客户端
                producer.send(msg, new SendCallback() {
                    @Override
                    public void onSuccess(SendResult sendResult) {
                        log.info("index:{} OK message: {}", index, sendResult.getMsgId());
                        countDownLatch.countDown();
                    }

                    @Override
                    public void onException(Throwable e) {
                        log.error("index:{} Exception", index, e);
                        countDownLatch.countDown();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                countDownLatch.countDown();
            }
        }
        // 异步发送，如果要求可靠传输，必须要等回调接口返回明确结果后才能结束逻辑，否则立即关闭Producer可能导致部分消息尚未传输成功
        boolean await = countDownLatch.await(5, TimeUnit.SECONDS);
        // 一旦producer不再使用，关闭producer
        // producer.shutdown();
    }

    private static void sendOnewayMessage() throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("common_one_way_message_producer");
        producer.setNamesrvAddr("localhost:9876");
        producer.start();
        for (int i = 0; i < 10; i++) {
            Message message = new Message("one_way_topic", "TagONE", ("one way message" + i).getBytes(StandardCharsets.UTF_8));
            // 单向消息，只负责发送。不等待待返回结果，不管消息是否发送成功。是一种不可靠的消息发送方式。
            producer.sendOneway(message);
        }
    }
}
