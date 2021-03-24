package com.huang.remotetransfer.handler;


import com.huang.remotetransfer.annotation.RemoteTransfer;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.reflections.Reflections;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.nio.charset.StandardCharsets;
import java.util.Set;

/**
 * @Author: huangyichun
 * @Date: 2021/2/23
 */
@Slf4j
@Component
public class RemoteTransferRegister implements BeanFactoryPostProcessor {

    //这个是扫描的包
    private static final String SCAN_PATH = "com.huang";

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

        RemoteTransferInvoke remoteTransferHandler = new RemoteTransferInvoke(invokeRestTemplate());

        Set<Class<?>> classSet = new Reflections(SCAN_PATH).getTypesAnnotatedWith(RemoteTransfer.class);
        for (Class<?> cls : classSet) {
            log.info("create proxy class name:{}", cls.getName());

            InvocationHandler handler = (proxy, method, args) -> remoteTransferHandler.invoke(cls, method, args);

            Object proxy = Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class<?>[]{cls}, handler);
            beanFactory.registerSingleton(cls.getName(), proxy);
        }
    }

    private RestTemplate invokeRestTemplate() {
        CloseableHttpClient httpClient = HttpClientBuilder.create().setMaxConnTotal(1000).setMaxConnPerRoute(100).build();
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        httpRequestFactory.setReadTimeout(5000);
        httpRequestFactory.setConnectionRequestTimeout(5000);
        httpRequestFactory.setConnectTimeout(5000);

        RestTemplate rest = new RestTemplate(httpRequestFactory);
        rest.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));

        rest.getInterceptors().add(((request, body, execution) -> {
            HttpHeaders headers = request.getHeaders();
            //TODO 可以设置公共的请求头
//            headers.add("req-id", RequestContext.getBaseRequestInfo().getRequestId());
//            headers.add("tenant-name", RequestContext.getBaseRequestInfo().getTenantId());
            return execution.execute(request, body);
        }));

        return rest;
    }

}
