package mafia.chatThings;

import java.io.Serializable;
public class Message implements Serializable {
    private String senderName;
    private String message;

    /**
     * constructor for messages
     * @param senderName is the sender of the message
     * @param message is the text of the message
     */
    public Message(String senderName, String message) {
        this.senderName = senderName;
        this.message = message;
    }
    public String getSenderName() {
        return senderName;
    }
    public String getMessageText() {
        return message;
    }

}
