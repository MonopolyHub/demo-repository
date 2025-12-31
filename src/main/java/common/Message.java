package common;

public class Message {
    public MessageType type;
    public String messageId;
    public Object payload;
    public Message() {}

    public Message(MessageType type, String messageId, Object payload) {
        this.type = type;
        this.messageId = messageId;
        this.payload = payload;
    }
}
