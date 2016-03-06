package com.superlifesize.chatappwithfirebase;

/**
 * Created by Nick Millward on 3/5/16.
 */
public class ChatMessage {

    String user;
    String message;

    public ChatMessage(String user , String message) {
        this.user = user;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getUser() {
        return user;
    }
}
