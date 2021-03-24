package com.huang.remotetransfer.base;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * jcloud api gateway open api standard error object
 *
 * @author wangjiehong
 * @date 2019/9/20 16:37
 */
@Data
public class ServiceError implements Serializable {

    private int code;
    private String message;
    private String status;
    private Map<String, String>[] details;

    @SuppressWarnings("unchecked")
    public void addDetailKeyValue(String key, String value) {
        if (details == null) {
            details = (Map<String, String>[]) new Map[1];
            details[0] = new HashMap<>();
        }
        details[0].put(key, value);
    }

}
