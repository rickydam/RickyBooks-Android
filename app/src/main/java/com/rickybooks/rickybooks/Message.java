package com.rickybooks.rickybooks;

import java.util.Date;

public class Message {
    private String message;
    private String sender;
    private Date createdAt;

    Message(String message, String sender, Date createdAt) {
        this.message = message;
        this.sender = sender;
        this.createdAt = createdAt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public Date getCreatedAt() {
        return createdAt;
    }
}