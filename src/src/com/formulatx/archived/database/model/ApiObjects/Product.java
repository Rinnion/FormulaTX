package com.formulatx.archived.database.model.ApiObjects;

/**
 * Created by tretyakov on 28.12.2015.
 */
public class Product {

    public long id;
    public String thumb;
    public String title;
    public String content;
    public String price;
    public String top;

    public Product(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Product:" + id;
    }
}
