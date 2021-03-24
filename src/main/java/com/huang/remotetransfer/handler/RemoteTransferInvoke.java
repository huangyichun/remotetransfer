package com.huang.remotetransfer.handler;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.huang.remotetransfer.annotation.RemoteTransfer;
import com.huang.remotetransfer.utils.RemoteTransferUtil;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @Author: huangyichun
 * @Date: 2021/2/22
 */

@Slf4j
@Data
public class RemoteTransferInvoke {

    private final RestTemplate restTemplate;

    private Map<Class<? extends Annotation>, RemoteTransferHandler> requestMethodMap = new HashMap<>();

    public RemoteTransferInvoke(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        init();
    }

    private void init() {
        requestMethodMap.put(GetMapping.class, (host, method, args) -> {
            GetMapping methodAnnotation = method.getAnnotation(GetMapping.class);
            String path = methodAnnotation.value()[0];
            return getOrDeleteMapping(method, args, host, path, HttpMethod.GET);
        });

        requestMethodMap.put(PutMapping.class, (host, method, args) -> {
            PutMapping methodAnnotation = method.getAnnotation(PutMapping.class);
            String path = methodAnnotation.value()[0];
            return putOrPostMapping(method, args, host, path, HttpMethod.PUT);
        });


        requestMethodMap.put(PostMapping.class, (host, method, args) -> {
            PostMapping methodAnnotation = method.getAnnotation(PostMapping.class);
            String path = methodAnnotation.value()[0];
            return putOrPostMapping(method, args, host, path, HttpMethod.POST);
        });
        requestMethodMap.put(DeleteMapping.class, (host, method, args) -> {
            DeleteMapping methodAnnotation = method.getAnnotation(DeleteMapping.class);
            String path = methodAnnotation.value()[0];
            return getOrDeleteMapping(method, args, host, path, HttpMethod.DELETE);
        });
    }


    public Object invoke(Class<?> tClass, Method method, Object[] args) {
        RemoteTransfer remoteAnnotation = tClass.getAnnotation(RemoteTransfer.class);
        String host = RemoteTransferConfig.map.get(remoteAnnotation.hostName());

        Annotation[] annotations = method.getAnnotations();
        Optional<Annotation> first = Arrays.stream(annotations).filter(annotation1 -> requestMethodMap.containsKey(annotation1.annotationType())).findFirst();
        Preconditions.checkArgument(first.isPresent(), "注解使用错误");

        Annotation methodAnnotation = first.get();
        return requestMethodMap.get(methodAnnotation.annotationType()).handler(host, method, args);
    }

    private Object putOrPostMapping(Method method, Object[] args, String host, String url, HttpMethod httpMethod) {
        url = RemoteTransferUtil.dealPathVariable(method, args, url);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(host + url);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<JSONObject> entity = new HttpEntity<>(RemoteTransferUtil.extractBody(method, args), httpHeaders);

        ResponseEntity<String> exchange = getResponseEntity(httpMethod, builder, entity);
        return JSONObject.parseObject(exchange.getBody(), method.getGenericReturnType());
    }

    private Object getOrDeleteMapping(Method method, Object[] args, String host, String url, HttpMethod httpMethod) {

        UriComponentsBuilder builder = buildGetUrl(method, args, host, url);
        log.info("requestUrl={}", builder.toUriString());
        HttpHeaders httpHeaders = new HttpHeaders();
        HttpEntity<JSONObject> entity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<String> exchange = getResponseEntity(httpMethod, builder, entity);
        return JSONObject.parseObject(exchange.getBody(), method.getGenericReturnType());
    }

    private ResponseEntity<String> getResponseEntity(HttpMethod httpMethod, UriComponentsBuilder builder, HttpEntity<JSONObject> entity) {
        ResponseEntity<String> exchange = restTemplate.exchange(builder.toUriString(), httpMethod, entity, String.class);
        if (exchange.getStatusCode() != HttpStatus.OK) {
            log.info("remote transfer error = {} statusCode={}, statusName={}", exchange.getBody(), exchange.getStatusCodeValue(), exchange.getStatusCode().name());
            //TODO 调用失败
        }
        return exchange;
    }


    private UriComponentsBuilder buildGetUrl(Method method, Object[] args, String host, String url) {
        url = RemoteTransferUtil.dealPathVariable(method, args, url);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(host + url);

        for (Map.Entry<String, String> entry : RemoteTransferUtil.extractParams(method, args).entrySet()) {
            builder.queryParam(entry.getKey(), entry.getValue());
        }
        return builder;
    }
}
