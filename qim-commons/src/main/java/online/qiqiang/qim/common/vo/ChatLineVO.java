package online.qiqiang.qim.common.vo;

/**
 * @author qiqiang
 */
public class ChatLineVO {
    private Long timestamp;
    private String userId;
    private String sender;
    private String content;

    public ChatLineVO() {
    }

    public ChatLineVO(String userId) {
        this.userId = userId;
        this.timestamp = System.currentTimeMillis();
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}