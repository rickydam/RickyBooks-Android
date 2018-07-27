package com.rickybooks.rickybooks.Models;

import java.util.List;

public class Textbook {
    private String id;
    private String title;
    private String author;
    private String edition;
    private String condition;
    private String type;
    private String coursecode;
    private String price;
    private String sellerId;
    private String sellerName;
    private String timestamp;
    private List<String> imageUrls;

    public Textbook(String id, String title, String author, String edition, String condition,
                    String type, String coursecode, String price, String sellerId,
                    String sellerName, String timestamp, List<String> imageUrls) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.edition = edition;
        this.condition = condition;
        this.type = type;
        this.coursecode = coursecode;
        this.price = price;
        this.sellerId = sellerId;
        this.sellerName = sellerName;
        this.timestamp = timestamp;
        this.imageUrls = imageUrls;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getEdition() {
        return edition;
    }

    public String getCondition() {
        return condition;
    }

    public String getType() {
        return type;
    }

    public String getCoursecode() {
        return coursecode;
    }

    public String getPrice() {
        return price;
    }

    public String getSellerId() {
        return sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }
}
