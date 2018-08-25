package com.rickybooks.rickybooks.Models;

public class NotifyItem {
    private String category;
    private String input;

    public NotifyItem(String category, String input) {
        this.category = category;
        this.input = input;
    }

    public String getCategory() {
        return category;
    }

    public String getInput() {
        return input;
    }
}
