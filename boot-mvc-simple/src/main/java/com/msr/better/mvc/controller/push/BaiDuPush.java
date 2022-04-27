package com.msr.better.mvc.controller.push;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.msr.better.common.util.HttpClientUtils;
import com.msr.better.common.util.constants.MaiConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2022-04-27 21:59
 **/
@Slf4j
public class BaiDuPush<T> {

    public T pushToBaidu(Class<T> clazz) throws IOException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        CloseableHttpClient httpsClient = HttpClientUtils.createHttpClient(MaiConstant.httpTimeOutMs);
        HttpPost httpPost = new HttpPost(MaiConstant.pushBaiDuRawUrl);
        List<String> pushUrlList = getPushUrl();
        StringBuilder stringBuilder = new StringBuilder();
        pushUrlList.forEach(e -> {
            if (StringUtils.isNotBlank(stringBuilder.toString())) {
                log.info("push url:{}",e);
                stringBuilder.append(MaiConstant.separatorNewline).append(e);
            } else {
                stringBuilder.append(e);
            }
        });
        HttpEntity h = new StringEntity(stringBuilder.toString());
        httpPost.setHeader("Content-Type","text/plain;charset=UTF-8");
        httpPost.setEntity(h);
        CloseableHttpResponse response = httpsClient.execute(httpPost);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            byte[] bytes = response.getEntity().getContent().readAllBytes();
            String result = new String(bytes);
            JSONObject jsonObject = JSON.parseObject(result);
            Constructor<T> constructor = clazz.getConstructor();
            constructor.setAccessible(true);
            Object instance = constructor.newInstance();
            jsonObject.keySet().forEach(key -> {
                Object fieldValue = jsonObject.get(key);
                if (key.contains(MaiConstant.separatorUnderLine)) {
                    key = lineToHump(key);
                }
                try {
                    Field field = clazz.getDeclaredField(key);
                    field.setAccessible(true);
                    field.set(instance, fieldValue);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            });
            return (T) instance;
        }

        return null;
    }

    private List<String> getPushUrl() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException, IOException {
        CloseableHttpClient httpsClient = HttpClientUtils.createHttpsClient(MaiConstant.httpTimeOutMs);
        // 创建Post请求
        HttpGet httpGet = new HttpGet(MaiConstant.siteMapUrl);
        httpGet.setProtocolVersion(HttpVersion.HTTP_1_1);
        CloseableHttpResponse response = httpsClient.execute(httpGet);
        if (response.getStatusLine().getStatusCode() == 200) {
            byte[] bytes = response.getEntity().getContent().readAllBytes();
            // 字节数组
            String siteMap = new String(bytes, StandardCharsets.UTF_8);
            Document document = Jsoup.parse(siteMap);
            Elements urlTags = document.getElementsByTag("url");
            return urlTags.stream().map(urlTag -> urlTag.child(0).text())
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public String lineToHump(String str) {
        Pattern linePattern = Pattern.compile("_(\\w)");
        str = str.toLowerCase();
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

}
