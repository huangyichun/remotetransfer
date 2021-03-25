# 自定义注解实现简单的RPC远程调用

博客地址：https://www.jianshu.com/p/be95f5e6d054

支持功能：
1. 替换SpringCloud @Feign注解
2. 支持Spring的 @GetMapping、 @DeleteMapping、@PutMapping、@PostMapping注解（注意：注解需要完整，如案例中）

涉及知识点：
- 自定义注解
- 动态代理
- Spring bean加载
- Java8优化的策略模式

实现原理：

扫描包下配置@RemoteTransfer注解的接口，使用JDK动态代理生成代理类，代理类实现远程调用功能。将代理类在Spring Bean初始化之前注入Spring容器，提供给其他类使用。

