package com.msr.better.mvc.controller;

import com.msr.better.common.util.HttpClientUtils;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.ServiceUnavailableRetryStrategy;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.net.ssl.SSLException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2022-04-21 21:59
 **/
@RestController
public class HttpClientController {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientController.class);
    private static final int TIMEOUT = 3000;
    private static final int RETRY_COUNT = 3;


    @GetMapping("test")
    public Object test() {
        String url = "http://127.0.0.1:8080/test/client";
//        CloseableHttpClient httpClient = HttpClientUtils.createHttpClient(TIMEOUT, new MyHttpRequestRetryHandler(RETRY_COUNT), new MyRetryStrategy());
        CloseableHttpClient httpClient = HttpClientUtils.createHttpClient(TIMEOUT, new MyHttpRequestRetryHandler(RETRY_COUNT));
        HttpGet httpGet = new HttpGet(url);
        logger.info("开始请求: {}", url);
        try {
            CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            String httpResult = EntityUtils.toString(httpResponse.getEntity());
            if (statusCode == HttpStatus.SC_OK) {
                logger.info("请求结果:{}", httpResult);
                return httpResult;
            }
        } catch (IOException e) {
            logger.error("接口请求异常", e);
        }
        return "接口异常返回";
    }


    private static class MyHttpRequestRetryHandler implements HttpRequestRetryHandler {

        private final int retryCount;

        public MyHttpRequestRetryHandler(int retryCount) {
            this.retryCount = retryCount;
        }

        @Override
        public boolean retryRequest(IOException exception, int executionCount, HttpContext httpContext) {
            if (executionCount > this.retryCount) {
                logger.warn("重试次数已达上限：{}", this.retryCount);
                return false;
            }
            // Unknown host
            if (exception instanceof UnknownHostException) {
                return false;
            }
            // SSL handshake exception
            if (exception instanceof SSLException) {
                return false;
            }
            if (exception instanceof InterruptedIOException
                    || exception instanceof NoHttpResponseException
                    || exception instanceof SocketException) {
                logger.warn("开始请求重试");
                return true;
            }
            HttpClientContext clientContext = HttpClientContext.adapt(httpContext);
            HttpRequest request = clientContext.getRequest();
            // Retry if the request is considered idempotent
            return !(request instanceof HttpEntityEnclosingRequest);
        }
    }

    private static class MyRetryStrategy implements ServiceUnavailableRetryStrategy {

        @Override
        public boolean retryRequest(HttpResponse response, int executionCount, HttpContext context) {
            return false;
        }

        @Override
        public long getRetryInterval() {
            return 0;
        }
    }
}
