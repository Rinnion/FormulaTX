package com.formulatx.archived.database.model.ApiObjects;

import java.io.Serializable;

/**
 * Created by tretyakov on 28.12.2015.
 */
public class Product implements Serializable {

    public long id;
    public String thumb;
    public String title;
    public String content;
    public String price;
    public String top;
    public boolean favorite;

    public Product(long id) {
        this.id = id;
        this.favorite=false;
    }

    @Override
    public String toString() {
        return "Product:" + id;
    }
}
