# QIM 分布式即时通讯服务

# 模块划分

# 架构介绍

![QIM架构图](https://gitee.com/qitea/images/raw/master/img/QIM%E6%9E%B6%E6%9E%84.jpg)

1. IM-Server 启动成功后会将自己注册到 Zookeeper 中，并且监听 Zookeeper 中所有 IM-Server 节点的变化，与所有其它 IM-Server 节点进行长连接。
2. Client 连接 Route 服务进行登陆，登陆将会转发到 Manage 服务，Manage 进行校验并通过后，从 Zookeeper 中获取一个 IM-Server 地址返回给 Client，Client 与该 IM-Server 进行长连接。
3. 当 Client 与 IM-Server node-1 连接成功后，node-1 会维护该用户的状态信息和连接状态。
4. Client 通过 Route 维护和查询好友列表和群组成员信息，Roure 会将请求转发到 Manage 处理。
5. Client 发送私聊消息到 IM-Server node-1，node-1 从 Redis 中获取接收人的连接状态，如果用户不在线，则保存消息；如果接收人与 node-1 本机保持通讯，那么将该私聊消息发送给接收人；如果接收人与其它 IM-Server 节点相连，那么将该私聊消息转发给这个 IM-Server 节点。
6. Client 发送群聊消息到 IM-Server node-1，node-1 从Redis 中获取该组的全部成员和与自己连接的所有成员信息，将自己维护的接收人的消息处理掉，然后将消息转发给其它 IM-Server 进行相同的处理。

# 快速启动

1. 搭建 redis，zookeeper，mysql 基础服务。

2. 启动 qim-server 服务。配置 zookeeper 地址，给服务一个唯一id

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

3. 启动 qim-manage 服务，启动后 swagger 地址访问`http://127.0.0.1:7777/swagger-ui/index.html`。配置 zookeeper 、redis和 mysql 地址。

   ```yaml
   server:
     port: 7777
   spring:
     datasource:
       url: jdbc:mysql://mysql.qiqiang.com:3307/qim
       username: root
       password: root
       driver-class-name: com.mysql.cj.jdbc.Driver
     redis:
       host: localhost
       port: 6379
   naming:
     zookeeper: zookeeper.qiqiang.com:2181
   ```

4. 启动 qim-route 服务。配置 qim-manage 地址：

   ```yaml
   server:
     port: 7750
   qim-manage:
     # qim -manage 地址
     address: http://localhost:7777
   ```

5. 最后启动若干个 qim-shell-client。输入 help 查看命令。

   ```
   Built-In Commands
           clear: Clear the shell screen.
           exit, quit: Exit the shell.
           help: Display help about available commands.
           script: Read and execute commands from a file.
           stacktrace: Display the full stacktrace of the last error.
   
   friend
           fadd: 添加好友
           fls: 获取好友列表
   
   group
           gls: 获取群成员  -g [groupId]
           gpush: 添加群成员  -g [groupId] -us [userId]
   
   login
           login: 登陆    login -u [uerId] -p [password]
   
   send
           send: 发送消息    send -r[receiver] -g[groupId] -m [message]
   ```

   

# Todo-list

- [ ] 消息存储
- [ ] 好友操作完善
- [ ] 群组操作完善
- [ ] 鉴权控制
- [ ] 消息顺序控制



