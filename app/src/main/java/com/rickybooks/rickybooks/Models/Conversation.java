package com.rickybooks.rickybooks.Models;

public class Conversation {
    private String conversationId;
    private String recipientName;

    public Conversation(String conversationId, String recipientName) {
        this.conversationId = conversationId;
        this.recipientName = recipientName;
    }

    public String getConversationId() {
        return conversationId;
    }

    public String getRecipientName() {
        return recipientName;
    }
}
