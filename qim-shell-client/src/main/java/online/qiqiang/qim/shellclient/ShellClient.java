package online.qiqiang.qim.shellclient;

import online.qiqiang.qim.client.ImClientService;
import online.qiqiang.qim.client.event.AbstractClientEventCallback;
import online.qiqiang.qim.client.im.ImClient;
import online.qiqiang.qim.client.service.LoginService;
import online.qiqiang.qim.common.server.ImServerInfo;
import online.qiqiang.qim.common.utils.QimResponseUtils;
import online.qiqiang.qim.common.vo.QimResponse;
import online.qiqiang.qim.protocol.ImProtocol;
import online.qiqiang.qim.protocol.ImProtocolVersion;
import online.qiqiang.qim.protocol.msg.GroupChatMsg;
import online.qiqiang.qim.protocol.msg.MsgType;
import online.qiqiang.qim.protocol.msg.PrivateChatMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * @author qiqiang
 */
@Component
public class ShellClient {

    private ImClient imClient;
    @Value("${qim-route.address}")
    private String routeAddress;
    private Long userId;
    @Autowired
    private LoginContext loginContext;

    public LoginResult login(String userId, String password) {
        LoginResult loginResult = new LoginResult();
        ImClientService imClientService = new ImClientService(routeAddress);
        LoginService loginService = imClientService.getService(LoginService.class);
        Call<QimResponse<ImServerInfo>> call = loginService.login(userId, password);
        try {
            Response<QimResponse<ImServerInfo>> response = call.execute();
            if (response.isSuccessful()) {
                QimResponse<ImServerInfo> qimResponse = response.body();
                if (QimResponseUtils.isOk(qimResponse)) {
                    String address = qimResponse.getData().getAddress();
                    createClient(userId, address);
                    imClient.start();
                    this.userId = Long.parseLong(userId);
                    loginResult.ok = true;
                    loginResult.message = "[" + userId + "]登陆成功";
                    loginContext.setAddress(address);
                } else {
                    loginResult.message = "登陆失败";
                }
                return loginResult;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        loginResult.message = "登陆失败";
        return loginResult;

    }

    private void createClient(String userId, String address) {
        imClient = new ImClient(userId);
        String[] split = address.split(":");
        imClient.setInetSocketAddress(new InetSocketAddress(split[0], Integer.parseInt(split[1])));
        imClient.setClientEventCallback(new AbstractClientEventCallback() {
            @Override
            protected void connected(InetSocketAddress inetSocketAddress) {
                System.out.println("连接到服务器" + inetSocketAddress + "成功");
            }

            @Override
            protected void reconnect(InetSocketAddress inetSocketAddress) {
                System.out.println("重新连接服务器" + inetSocketAddress + "…………");
            }

            @Override
            protected void close() {
                System.out.println("关闭");
            }
        });
    }


    public String send(String receiver, boolean group, String message) {
        if (notLogin()) {
            return "用户未登陆";
        }
        ImProtocol protocol = new ImProtocol();
        protocol.setVersion(ImProtocolVersion.V1);
        String result;
        if (group) {
            protocol.setMsgType(MsgType.CHAT_GROUP.ordinal());
            GroupChatMsg chatMsg = new GroupChatMsg(imClient.getUserId(), receiver);
            chatMsg.setContent(message);
            protocol.setBody(chatMsg);
            result = "[群聊(" + receiver + ")]我:" + message;
        } else {
            protocol.setMsgType(MsgType.CHAT_PRIVATE.ordinal());
            PrivateChatMsg chatMsg = new PrivateChatMsg(imClient.getUserId(), receiver);
            chatMsg.setContent(message);
            protocol.setBody(chatMsg);
            result = "[私聊给(" + receiver + ")]我:" + message;
        }
        boolean ok = imClient.write(protocol);
        if (ok) {
            return result;
        } else {
            return "消息发送失败";
        }

    }

    public void logout() {
        this.userId = null;
        this.imClient.close();
        this.imClient = null;
    }

    public Long getUserId() {
        return userId;
    }

    public boolean notLogin() {
        return imClient == null;
    }

    public static class LoginResult {
        public boolean ok;
        public String message;
    }
}