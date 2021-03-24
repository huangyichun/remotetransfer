package com.huang.remotetransfer.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Author: huangyichun
 * @Date: 2021/2/23
 */
@Slf4j
public class RemoteTransferUtil {

    public static Map<String, String> extractPath(Method method, Object[] args) {
        Map<String, String> params = new HashMap<>();
        Parameter[] parameters = method.getParameters();

        if (parameters.length == 0) {
            return params;
        }

        for (int i = 0; i < parameters.length; i++) {
            PathVariable param = parameters[i].getAnnotation(PathVariable.class);
            if (param != null) {
                params.put(param.value(), String.valueOf(args[i]));
            }
        }
        return params;
    }

    public static JSONObject extractBody(Method method, Object[] args) {
        JSONObject object = new JSONObject();
        Parameter[] parameters = method.getParameters();
        if (parameters.length == 0) {
            return null;
        }

        for (int i = 0; i < parameters.length; i++) {
            RequestBody param = parameters[i].getAnnotation(RequestBody.class);
            if (param != null) {
                String returnStr = JSON.toJSONString(args[i], SerializerFeature.WriteMapNullValue, SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.WriteDateUseDateFormat);
                object = JSONObject.parseObject(returnStr);
            }
        }
        return object;
    }

    public static String dealPathVariable(Method method, Object[] args, String url) {
        for (Map.Entry<String, String> entry : RemoteTransferUtil.extractPath(method, args).entrySet()) {
            if (url.contains("{" + entry.getKey() + "}")) {
                url = url.replace("{" + entry.getKey() + "}", entry.getValue());
            }
        }
        return url;
    }


    public static LinkedHashMap<String, String> extractParams(Method method, Object[] args) {
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        Parameter[] parameters = method.getParameters();

        if (parameters.length == 0) {
            return params;
        }

        for (int i = 0; i < parameters.length; i++) {
            RequestParam param = parameters[i].getAnnotation(RequestParam.class);
            //判断参数是否必传
            if (param != null && (param.required() || args[i] != null)) {
                params.put(param.value(), String.valueOf(args[i]));
            }
        }
        return params;
    }
}
