package com.msr.better.mq;

import com.msr.better.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-09-01 23:14:54
 */
@Component
public abstract class AbstractMessageHandler<T> implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(AbstractMessageHandler.class);
    private final String messageType;
    private final Class<T> clazz;
    @Autowired
    protected MessageTrunk messageTrunk;
    @Autowired
    private RedisUtil redisUtil;
    private boolean monitor;
    private int retryTimes = 3;

    public AbstractMessageHandler(String messageType, Class<T> clazz, int retryTimes) {
        this.messageType = messageType;
        this.clazz = clazz;
        this.retryTimes = retryTimes;
    }

    public AbstractMessageHandler(String messageType, Class<T> clazz) {
        this.messageType = messageType;
        this.clazz = clazz;
    }

    @PostConstruct
    public void startListen() {
        (new Thread(this)).start();
    }

    @Override
    public void run() {
        while (true) {
            this.listen();
        }
    }

    public void listen() {
        final Message obj = this.redisUtil.blpop(this.messageType, 2147483647, Message.class);
        if (obj == null) {
            this.monitor = false;
            logger.warn("消息分发器获取redis连接失败");

            try {
                Thread.sleep(5000L);
            } catch (InterruptedException var4) {
                logger.warn("消息分发器线程暂停失败");
            }

        } else {
            if (!this.monitor) {
                logger.warn("消息分发开始");
                this.monitor = true;
            }

            try {
                this.messageTrunk.getThreadPool().submit(() -> {

                    try {
                        handle((T) obj.getContent());
                    } catch (Exception var4) {
                        AbstractMessageHandler.logger.error(String.valueOf(var4));
                        if (obj.getFailTimes() >= AbstractMessageHandler.this.retryTimes) {
                            handleFailed((T) obj.getContent());
                        } else {
                            obj.setFailTimes(obj.getFailTimes() + 1);
                            AbstractMessageHandler.this.messageTrunk.put(obj);
                            if (AbstractMessageHandler.logger.isDebugEnabled()) {
                                AbstractMessageHandler.logger.debug("msg:[" + obj + "], 执行失败，准备重试。");
                            }
                        }
                    }

                });
            } catch (TaskRejectedException var6) {
                logger.warn("线程池已满，准备回写任务，暂停本线程");
                this.messageTrunk.put(obj);

                try {
                    Thread.sleep(this.messageTrunk.getThreadPoolFullSleepSeconds() * 1000L);
                } catch (InterruptedException var5) {
                    logger.warn("生产者暂停异常", var6);
                }
            } catch (Exception var7) {
                logger.error("消息总线发生异常", var7);
            }

        }
    }

    public abstract void handle(T var1);

    public abstract void handleFailed(T var1);
}

