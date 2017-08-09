package com.example.adrien.smartalarm;

public class GeneralCatalog {

    private long id;
    private String category;

    public GeneralCatalog(long id, String category) {
        super();
        this.id = id;
        this.category= category;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
