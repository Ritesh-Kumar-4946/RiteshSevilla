package com.ritesh.sevilla.Beans;

/**
 * Created by ritesh on 23/2/17.
 */

public class BeanSubCategoryProduct {

    private String SubCatProductid;
    private String subCatProductTittle;
    private String subCatProductPrice;
    private String SubCatProductImage;

    public String getId() {
        return SubCatProductid;
    }

    public void setId(String id) {
        this.SubCatProductid = id;
    }

    public String getsubCatProductTittle() {
        return subCatProductTittle;
    }

    public void setsubCatTProductittle(String subCatProductTittle) {
        this.subCatProductTittle = subCatProductTittle;
    }


    public String getsubCatProductPrice() {
        return subCatProductPrice;
    }

    public void setsubCatTProducPrice(String subCatProductPrice) {
        this.subCatProductPrice = subCatProductPrice;
    }

    public String getImage() {
        return SubCatProductImage;
    }

    public void setImage(String image) {
        this.SubCatProductImage = image;
    }


}
