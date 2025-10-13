package by.teachmeskills.devteam.config;


import org.springframework.boot.restclient.RestTemplateCustomizer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.JdkClientHttpRequestFactory;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.http.HttpClient;

@TestConfiguration(proxyBeanMethods = false)
public class TestHttpClientConfig {

    @Bean
    CookieManager testCookieManager() {
        return new CookieManager(null, CookiePolicy.ACCEPT_ALL);
    }

    @Bean
    RestTemplateCustomizer cookieRestTemplateCustomizer(CookieManager cookieManager) {
        var httpClient = HttpClient.newBuilder().cookieHandler(cookieManager).build();
        var requestFactory = new JdkClientHttpRequestFactory(httpClient);
        return rt -> rt.setRequestFactory(requestFactory);
    }
}
