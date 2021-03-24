package com.huang.remotetransfer.handler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: huangyichun
 * @Date: 2021/2/23
 */
@Component
public class RemoteTransferConfig {

    public static Map<String, String> map = new HashMap<>();

    @Value("${remote.transfer.host}")
    private String host;

    @PostConstruct
    public void init() {
        map.put("TRADE-PLATFORM", host);
    }
}
