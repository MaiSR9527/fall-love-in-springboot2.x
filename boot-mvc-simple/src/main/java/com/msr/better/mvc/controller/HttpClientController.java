package com.msr.better.mvc.controller;

import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.SocketTimeoutException;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2022-04-21 21:59
 **/
@RestController
public class HttpClientController {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientController.class);

    @GetMapping("test")
    public Object test() {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(3000)
                .setConnectionRequestTimeout(3000)
                .setSocketTimeout(3000).build();
        HttpGet httpGet = new HttpGet("http://127.0.0.1:8080/test/client");
        httpGet.setConfig(requestConfig);

        for (int i = 1; i <= 3; i++) {
            try {
                CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
                int statusCode = httpResponse.getStatusLine().getStatusCode();
                String httpResult = EntityUtils.toString(httpResponse.getEntity());
                if (statusCode == HttpStatus.SC_OK) {
                    return httpResult;
                }
                logger.info("第{}次重试：, 响应数据：{}",i , httpResult);
            } catch (IOException e) {
                if (e instanceof SocketTimeoutException) {
                    logger.error("请求超时第{}次重试", i, e);
                } else {
                    logger.error("转化数据回传到失败: ", e);
                }
            }
        }
        return "123";
    }
}
