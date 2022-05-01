package com.msr.better.common.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.ServiceUnavailableRetryStrategy;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultServiceUnavailableRetryStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.lang.NonNull;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Objects;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2022-04-27 22:00
 **/
public class HttpClientUtils {

    /**
     * Timeout (Default is 5s).
     */
    private static final int TIMEOUT = 5000;

    private static final int DEFAULT_TOTAL = 20;
    private static final int MAX_TOTAL = 100;
    private static final PoolingHttpClientConnectionManager poolingHttpClientConnectionManager;

    static {
        poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager();
        poolingHttpClientConnectionManager.setMaxTotal(MAX_TOTAL);
        poolingHttpClientConnectionManager.setDefaultMaxPerRoute(DEFAULT_TOTAL);
    }

    private HttpClientUtils() {
    }

    /**
     * Creates https client.
     *
     * @param timeout connection timeout (ms)
     * @return https client
     * @throws KeyStoreException        key store exception
     * @throws NoSuchAlgorithmException no such algorithm exception
     * @throws KeyManagementException   key management exception
     */
    @NonNull
    public static CloseableHttpClient createHttpsClient(int timeout)
            throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        SSLContext sslContext = new SSLContextBuilder()
                .loadTrustMaterial(null, (certificate, authType) -> true)
                .build();

        return resolveProxySetting(HttpClients.custom())
                .setSSLContext(sslContext)
                .setSSLHostnameVerifier(new NoopHostnameVerifier())
                .setDefaultRequestConfig(getRequestConfig(timeout))
                .build();
    }

    /**
     * Creates https client.
     *
     * @param timeout connection timeout (ms)
     * @return https client
     * @throws KeyStoreException        key store exception
     * @throws NoSuchAlgorithmException no such algorithm exception
     * @throws KeyManagementException   key management exception
     */
    @NonNull
    public static CloseableHttpClient createHttpsClient(int timeout, HttpRequestRetryHandler retryHandler)
            throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        if (Objects.isNull(retryHandler)) {
            return createHttpsClient(timeout);
        }
        SSLContext sslContext = new SSLContextBuilder()
                .loadTrustMaterial(null, (certificate, authType) -> true)
                .build();

        return resolveProxySetting(HttpClients.custom())
                .setSSLContext(sslContext)
                .setSSLHostnameVerifier(new NoopHostnameVerifier())
                .setRetryHandler(retryHandler)
                .setDefaultRequestConfig(getRequestConfig(timeout))
                .build();
    }

    @NonNull
    public static CloseableHttpClient createHttpsClient(int timeout, HttpRequestRetryHandler retryHandler, ServiceUnavailableRetryStrategy retryStrategy)
            throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        if (Objects.isNull(retryHandler)) {
            return createHttpsClient(timeout);
        }
        if (Objects.isNull(retryStrategy)) {
            return createHttpsClient(timeout, retryHandler);
        }
        SSLContext sslContext = new SSLContextBuilder()
                .loadTrustMaterial(null, (certificate, authType) -> true)
                .build();

        return resolveProxySetting(HttpClients.custom())
                .setSSLContext(sslContext)
                .setSSLHostnameVerifier(new NoopHostnameVerifier())
                .setRetryHandler(retryHandler)
                .setDefaultRequestConfig(getRequestConfig(timeout))
                .build();
    }

    /**
     * Creates http client
     * author MaiShuRen
     *
     * @param timeout connection timeout (ms)
     * @return http client
     */
    @NonNull
    public static CloseableHttpClient createHttpClient(int timeout) {
        return resolveProxySetting(HttpClients.custom())
                .setDefaultRequestConfig(getRequestConfig(timeout))
                .build();
    }

    /**
     * Creates http client with retry handler
     *
     * @param timeout      connection timeout (ms)
     * @param retryHandler retry handler
     * @return http client
     */
    @NonNull
    public static CloseableHttpClient createHttpClient(int timeout, HttpRequestRetryHandler retryHandler) {
        if (Objects.isNull(retryHandler)) {
            return createHttpClient(timeout);
        }
        return resolveProxySetting(HttpClients.custom())
                .setDefaultRequestConfig(getRequestConfig(timeout))
                .setConnectionManager(poolingHttpClientConnectionManager)
                .setRetryHandler(retryHandler)
                .setServiceUnavailableRetryStrategy(new DefaultServiceUnavailableRetryStrategy())
                .build();
    }

    @NonNull
    public static CloseableHttpClient createHttpClient(int timeout, HttpRequestRetryHandler retryHandler, ServiceUnavailableRetryStrategy retryStrategy) {
        if (Objects.isNull(retryHandler)) {
            return createHttpClient(timeout);
        }
        if (Objects.isNull(retryStrategy)) {
            return createHttpClient(timeout, retryHandler);
        }
        return resolveProxySetting(HttpClients.custom())
                .setDefaultRequestConfig(getRequestConfig(timeout))
                .setConnectionManager(poolingHttpClientConnectionManager)
                .setRetryHandler(retryHandler)
                .setServiceUnavailableRetryStrategy(retryStrategy)
                .build();
    }

    /**
     * resolve system proxy config
     *
     * @param httpClientBuilder the httpClientBuilder
     * @return the argument
     */
    private static HttpClientBuilder resolveProxySetting(
            final HttpClientBuilder httpClientBuilder) {
        final String httpProxyEnv = System.getenv("http_proxy");
        if (StringUtils.isNotBlank(httpProxyEnv)) {
            final String[] httpProxy = resolveHttpProxy(httpProxyEnv);
            final HttpHost httpHost = HttpHost.create(httpProxy[0]);
            httpClientBuilder.setProxy(httpHost);
            if (httpProxy.length == 3) {
                //set proxy credentials
                final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                credentialsProvider
                        .setCredentials(new AuthScope(httpHost.getHostName(), httpHost.getPort()),
                                new UsernamePasswordCredentials(httpProxy[1], httpProxy[2]));
                httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
            }
        }
        return httpClientBuilder;
    }

    /**
     * @param httpProxy http proxy env values
     * @return resolved http proxy values; first is host(@nonNull), second is username(@nullable)
     * , third is password(@nullable)
     */
    private static String[] resolveHttpProxy(final String httpProxy) {
        final URI proxyUri = URI.create(httpProxy);
        int port = proxyUri.getPort();
        if (port == -1) {
            if (Objects.equals("http", proxyUri.getScheme())) {
                port = 80;
            }
            if (Objects.equals("https", proxyUri.getScheme())) {
                port = 443;
            }
        }
        final String hostUrl = proxyUri.getScheme() + "://" + proxyUri.getHost() + ":" + port;
        final String usernamePassword = proxyUri.getUserInfo();
        if (StringUtils.isNotBlank(usernamePassword)) {
            final String username;
            final String password;
            final int atColon = usernamePassword.indexOf(':');
            if (atColon >= 0) {
                username = usernamePassword.substring(0, atColon);
                password = usernamePassword.substring(atColon + 1);
            } else {
                username = usernamePassword;
                password = null;
            }
            return new String[]{hostUrl, username, password};
        } else {
            return new String[]{hostUrl};
        }
    }

    /**
     * Gets request config.
     *
     * @param timeout connection timeout (ms)
     * @return request config
     */
    private static RequestConfig getRequestConfig(int timeout) {
        return RequestConfig.custom()
                .setConnectTimeout(timeout)
                .setConnectionRequestTimeout(timeout)
                .setSocketTimeout(timeout)
                .build();
    }


    /**
     * Multipart file resource.
     *
     * @author johnniang
     */
    public static class MultipartFileResource extends ByteArrayResource {

        private final String filename;

        public MultipartFileResource(byte[] buf, String filename) {
            super(buf);
            this.filename = filename;
        }

        @Override
        public String getFilename() {
            return this.filename;
        }

    }

    private static SSLContext createSSLContext() {
        SSLContext sslcontext = null;
        try {
            sslcontext = SSLContext.getInstance("SSL");
            sslcontext.init(null, new TrustManager[]{new TrustAnyTrustManager()}, new SecureRandom());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
        return sslcontext;
    }

    private static class TrustAnyTrustManager implements X509TrustManager {

        public void checkClientTrusted(X509Certificate[] chain, String authType) {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }
    }

}
