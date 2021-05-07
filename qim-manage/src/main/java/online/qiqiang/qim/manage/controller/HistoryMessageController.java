package online.qiqiang.qim.manage.controller;

import io.swagger.annotations.Api;
import online.qiqiang.qim.common.vo.ChatLineVO;
import online.qiqiang.qim.common.vo.QimResponse;
import online.qiqiang.qim.managesdk.ChatLine;
import online.qiqiang.qim.managesdk.store.ChatMessageStore;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 历史消息
 *
 * @author qiqiang
 */
@RestController
@Api("历史消息API")
public class HistoryMessageController {
    @Resource
    private ChatMessageStore chatMessageStore;

    @RequestMapping("/historyMessage/{userId}")
    public QimResponse<List<ChatLineVO>> historyMessage(@PathVariable String userId) {
        List<ChatLine> list = chatMessageStore.read(userId);
        List<ChatLineVO> collect = list.stream().map(chatLine -> {
            ChatLineVO chatLineVO = new ChatLineVO(chatLine.getUserId());
            chatLineVO.setTimestamp(chatLine.getTimestamp());
            chatLineVO.setContent(chatLine.getContent());
            chatLineVO.setSender(chatLine.getSender());
            return chatLineVO;
        }).collect(Collectors.toList());
        return new QimResponse<>(collect);
    }
}