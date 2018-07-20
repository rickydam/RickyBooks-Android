package com.rickybooks.rickybooks.Models;

public class Conversation {
    private String conversationId;
    private String otherName;

    public Conversation(String conversationId, String otherName) {
        this.conversationId = conversationId;
        this.otherName = otherName;
    }

    public String getConversationId() {
        return conversationId;
    }

    public String getOtherName() {
        return otherName;
    }
}
