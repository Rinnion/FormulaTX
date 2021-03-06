package com.formulatx.archived.database.model.ApiObjects;

/**
 * Created by tretyakov on 28.12.2015.
 * Represents card object
 */
public class Partner {

    public long id;
    public String thumb;
    public String title;
    public String status;
    public String link;

    public Partner(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Card:" + id;
    }
}
