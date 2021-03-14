package online.qiqiang.qim.shellclient;

import online.qiqiang.qim.client.ImClientService;
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

    public String login(String userId, String password) {
        ImClientService imClientService = new ImClientService(routeAddress);
        LoginService loginService = imClientService.getService(LoginService.class);
        Call<QimResponse<ImServerInfo>> call = loginService.login(userId, password);
        try {
            Response<QimResponse<ImServerInfo>> response = call.execute();
            if (response.isSuccessful()) {
                QimResponse<ImServerInfo> qimResponse = response.body();
                if (QimResponseUtils.isOk(qimResponse)) {
                    String address = qimResponse.getData().getAddress();
                    imClient = new ImClient(userId);
                    String[] split = address.split(":");
                    imClient.setInetSocketAddress(new InetSocketAddress(split[0], Integer.parseInt(split[1])));
                    imClient.start();
                } else {
                    System.out.println("登陆失败");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.userId = Long.parseLong(userId);
        return "OK";
    }


    public String send(String receiver, boolean group, String message) {
        if (notLogin()) {
            return "用户未登陆";
        }
        ImProtocol protocol = new ImProtocol();
        protocol.setVersion(ImProtocolVersion.V1);
        if (group) {
            protocol.setMsgType(MsgType.CHAT_GROUP.ordinal());
            GroupChatMsg chatMsg = new GroupChatMsg(imClient.getUserId(), receiver);
            chatMsg.setContent(message);
            protocol.setBody(chatMsg);
        } else {
            protocol.setMsgType(MsgType.CHAT_PRIVATE.ordinal());
            PrivateChatMsg chatMsg = new PrivateChatMsg(imClient.getUserId(), receiver);
            chatMsg.setContent(message);
            protocol.setBody(chatMsg);
        }
        imClient.write(protocol);
        System.out.println("我:" + message);
        return "OK";
    }

    public Long getUserId() {
        return userId;
    }

    public boolean notLogin() {
        return imClient == null;
    }
}