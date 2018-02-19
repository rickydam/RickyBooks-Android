package com.example.rickydam.rickybooks;

public class Textbook {
    private String title;
    private String author;
    private String edition;
    private String condition;
    private String type;
    private String coursecode;
    private String price;

    public Textbook() {}

    public Textbook(String title, String author, String edition, String condition, String type,
                    String coursecode, String price) {
        this.title = title;
        this.author = author;
        this.edition = edition;
        this.condition = condition;
        this.type = type;
        this.coursecode = coursecode;
        this.price = price;
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

    public void setAuthor(String author) {
        this.author = author;
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

    public void setCoursecode(String course) {
        this.coursecode = coursecode;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
