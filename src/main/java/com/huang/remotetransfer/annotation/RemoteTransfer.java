package com.huang.remotetransfer.annotation;

import java.lang.annotation.*;

/**
 * 远程调用，替换SpringCloud Feign
 * @Author: huangyichun
 * @Date: 2021/2/22
 */
@Target(ElementType.TYPE)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RemoteTransfer {

    String hostName();
}
