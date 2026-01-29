package common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Network message (line-delimited JSON).
 */
public class Message {
    private static final Gson GSON = new GsonBuilder().create();

    public MessageType type;
    public Long messageId;
    public Object payload;

    public Message() {
    }

    public Message(MessageType type, Long messageId, Object payload) {
        this.type = type;
        this.messageId = messageId;
        this.payload = payload;
    }

    public String toJson() {
        return GSON.toJson(this);
    }

    public static Message fromJson(String json) {
        return GSON.fromJson(json, Message.class);
    }
}
