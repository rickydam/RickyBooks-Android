package com.rickybooks.rickybooks.Models;

public class NotifyItem {
    private String id;
    private String category;
    private String input;

    public NotifyItem(String id, String category, String input) {
        this.id = id;
        this.category = category;
        this.input = input;
    }

    public String getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public String getInput() {
        return input;
    }
}
