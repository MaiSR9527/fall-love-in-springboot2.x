package top.maishuren.better.thread;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2022/4/25
 */
@Component
public class KafkaConsumerThreadPool {

    /**处理器数量*/
    public static final int CORES = Runtime.getRuntime().availableProcessors();
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumerThreadPool.class);
    /**自定义线程池*/
    private final static ExecutorService executor = new ThreadPoolExecutor(
            10,
            1000,
            10L,
            TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<>(1000),
            new ThreadFactoryBuilder().setNameFormat("kafka-worker-%d").build()
    );

    /**
     * 执行任务
     *
     * @param task 任务
     */
    public void run(Runnable task) {
        try {
            executor.execute(task);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
