package vn.edu.iuh.fit;

import org.springframework.stereotype.Component;

public class ObjectResponse {
    private String message;
    private String chatId;


    public ObjectResponse(String message, String chatId) {
        this.message = message;
        this.chatId = chatId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }
}
