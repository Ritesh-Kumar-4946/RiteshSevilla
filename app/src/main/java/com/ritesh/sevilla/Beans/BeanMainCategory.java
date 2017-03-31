package com.ritesh.sevilla.Beans;

/**
 * Created by ritesh on 15/2/17.
 */

public class BeanMainCategory {

    private String Catid;
    private String CatName;
    private String CatImage;

    public String getId() {
        return Catid;
    }

    public void setId(String id) {
        this.Catid = id;
    }

    public String getUsername() {
        return CatName;
    }

    public void setUsername(String username) {
        this.CatName = username;
    }

    public String getImage() {
        return CatImage;
    }

    public void setImage(String image) {
        this.CatImage = image;
    }

}
