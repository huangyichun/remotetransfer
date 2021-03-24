package com.huang.remotetransfer.handler;

import java.lang.reflect.Method;

/**
 * @Author: huangyichun
 * @Date: 2021/2/24
 */
public interface RemoteTransferHandler {

    Object handler(String host, Method method, Object[] args);
}
