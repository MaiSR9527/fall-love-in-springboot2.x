package com.msr.better.mvc.http;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ServiceUnavailableRetryStrategy;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2022-05-01 22:18
 **/
public class MyServiceUnavailableRetryStrategy implements ServiceUnavailableRetryStrategy {

    private static final Logger logger = LoggerFactory.getLogger(MyServiceUnavailableRetryStrategy.class);

    private final int maxRetries;
    private final long retryInterval;

    public MyServiceUnavailableRetryStrategy(final int maxRetries, final long retryInterval) {
        this.maxRetries = maxRetries;
        this.retryInterval = retryInterval;
    }

    public MyServiceUnavailableRetryStrategy() {
        this(3, 1000);
    }

    @Override
    public boolean retryRequest(HttpResponse response, int executionCount, HttpContext context) {
        logger.info("Come in MyServiceUnavailableRetryStrategy");
        if (executionCount > maxRetries) {
            logger.warn("RetryStrategy 重试次数已达：{}", maxRetries);
            return false;
        }
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            try {
                String resultResp = EntityUtils.toString(response.getEntity());
                JsonObject jsonObject = JsonParser.parseString(resultResp).getAsJsonObject();
                // 假设第三方请求返回业务状态码使用code字段
                int code = jsonObject.get("error_code").getAsInt();
                if (code == 5000) {
                    logger.info("开始第{}重试", executionCount);
                    return true;
                }
            } catch (IOException e) {
                logger.error("读取结果异常");
            }
        }
        // Http Status 503 也重试
        return response.getStatusLine().getStatusCode() == HttpStatus.SC_SERVICE_UNAVAILABLE;
    }

    @Override
    public long getRetryInterval() {
        return this.retryInterval;
    }
}
