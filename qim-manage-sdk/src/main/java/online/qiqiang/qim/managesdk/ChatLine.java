package online.qiqiang.qim.managesdk;

/**
 * @author qiqiang
 */
public class ChatLine {
    private long timestamp;
    private String userId;
    private String sender;
    private String content;

    public ChatLine() {
    }

    public ChatLine(String userId) {
        this.userId = userId;
        this.timestamp = System.currentTimeMillis();
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
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