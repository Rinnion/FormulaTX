package com.rinnion.archived.database.model.ApiObjects;

/**
 * Created by tretyakov on 28.12.2015.
 * Represents card object
 */
public class Card {

    public long id;
    public String thumb;
    public String title;
    public String status;
    public String link;

    public Card(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Card:" + id;
    }
}
