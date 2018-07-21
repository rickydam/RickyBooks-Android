package com.rickybooks.rickybooks.Models;

public class Conversation {
    private String id;
    private String otherName;

    public Conversation(String id, String otherName) {
        this.id = id;
        this.otherName = otherName;
    }

    public String getId() {
        return id;
    }

    public String getOtherName() {
        return otherName;
    }
}
