# QIM 分布式即时通讯服务

# 模块划分

# 架构介绍

![QIM架构图](https://gitee.com/qitea/images/raw/master/img/QIM%E6%9E%B6%E6%9E%84.png)

# 快速启动

1. 搭建 redis，zookeeper，mysql 基础服务。

2. 启动 qim-server 服务。配置文件如下：

   ```yaml
   server:
     port: 7781
   im-server:
     # 启动多个实例时，id必须唯一
     id: node-1
     host: 127.0.0.1
     port: 9999
   naming:
     zookeeper: zookeeper.qiqiang.com:2181
   ```

3. 启动 qim-manage 服务，启动后 swagger 地址访问`http://127.0.0.1:7777/swagger-ui/index.html`。配置文件如下：

   ```yaml
   server:
     port: 7777
   spring:
     datasource:
       url: jdbc:mysql://mysql.qiqiang.com:3307/qim
       username: root
       password: root
       driver-class-name: com.mysql.cj.jdbc.Driver
     jpa:
       hibernate:
         ddl-auto: update
     redis:
       host: localhost
       port: 6379
   naming:
     zookeeper: zookeeper.qiqiang.com:2181
   ```

4. 启动 qim-route 服务。配置文件如下：

   ```yaml
   server:
     port: 7750
   qim-manage:
     # qim -manage 地址
     address: http://localhost:7777
   spring:
     cloud:
       gateway:
         routes:
           # 登陆
           - id: login
             uri: ${qim-manage.address}
             predicates:
               - Path=/login
           # 群组
           - id: group
             uri: ${qim-manage.address}
             predicates:
               - Path=/group/**
           # 好友
           - id: friend
             uri: ${qim-manage.address}
             predicates:
               - Path=/friend/**
   ```

5. 最后启动若干个 qim-shell-client。

   