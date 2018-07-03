package com.example.rickydam.rickybooks;

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
    private List<String> imageUrls;

    Textbook(String id, String title, String author, String edition, String condition, String type,
             String coursecode, String price, String sellerId, String sellerName,
             List<String> imageUrls) {
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
        this.imageUrls = imageUrls;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public List<String> getImageUrls() {
        return imageUrls;
    }
}
