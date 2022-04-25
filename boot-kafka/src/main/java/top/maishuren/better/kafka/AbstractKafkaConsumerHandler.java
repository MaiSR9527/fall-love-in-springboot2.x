package top.maishuren.better.kafka;

import com.msr.better.common.util.AopTargetUtils;
import com.msr.better.common.util.JsonUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Optional;
import java.util.Random;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2022/4/25
 */
public abstract class AbstractKafkaConsumerHandler<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractKafkaConsumerHandler.class);

    //默认休眠5毫秒;
    private int DEFAULT_SLEEP_TIME = 5;


    public abstract void executeConsumer(T value);

    private void setLogger(Class clazz) {
        LOGGER.warn(" abstract  executeConsumer before setLogger clazz=:[{}]", clazz);
    }

    public void onMessage(ConsumerRecord<String, String> record) {
        execute(record);
    }

    private void execute(ConsumerRecord<String, String> record) {
        try {
            Optional<?> messages = Optional.ofNullable(record.value());
            if (!messages.isPresent()) {
                int sleepTime = new Random().nextInt(10);
                sleepTime = sleepTime == 0 ? DEFAULT_SLEEP_TIME : sleepTime;
                Thread.sleep(sleepTime);
                return;
            }

            // 获取方法的真实类
            Method[] methods = AopTargetUtils.getTarget(this).getClass().getDeclaredMethods();

            Method method = null;
            for (Method m : methods) {
                Class[] parameterClasses = m.getParameterTypes();

                if (!m.getName().equals("executeConsumer") || parameterClasses == null || parameterClasses.length < 1) {
                    continue;
                }

                if (parameterClasses[0] == Object.class) {
                    continue;
                }

                method = m;
                break;

            }


            Class<?> clazz = Optional.ofNullable(method).map(m -> m.getParameterTypes()[0]).orElse(null);


            LOGGER.info("  onMessage ConsumerRecord  value=:[{}]", record.value());
            setLogger(clazz);
            if (null == clazz) {
                clazz = String.class;
            }
            if (String.class == clazz || String.class == clazz || clazz == Object.class) {
                executeConsumer((T) record.value());
            } else if (Collection.class.isAssignableFrom(clazz)) {
                Type generic = ((ParameterizedType) method.getGenericParameterTypes()[0]).getActualTypeArguments()[0];
                T instance = (T) JsonUtils.string2List(record.value(), (Class) generic);
                executeConsumer(instance);
            } else {
                T instance = (T) JsonUtils.readValue(record.value(), clazz);
                executeConsumer(instance);
            }
        } catch (Exception e) {
            LOGGER.error("kafka消费失败", e);
            LOGGER.warn(" abstract kafka onMessage  Exception ConsumerRecord =:[{}] ", record.toString());
        }
    }

    private Class<T> getTemplateType() {
        Class<T> clazz = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        return clazz;
    }
}
