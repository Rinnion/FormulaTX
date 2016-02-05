package com.formulatx.archived.database.model;

import java.io.Serializable;

public class InstagramItem implements Serializable {

    public final long id;
    public long reference_id;
    public final String text;
    public final String link;
    public String date;

    public InstagramItem(long id, long gallery_id, String text, String link, String date) {
        this.id = id;
        this.reference_id = gallery_id;
        this.text = text;
        this.link = link;
        this.date = date;
    }

    @Override
    public String toString() {return InstagramItem.class.getSimpleName() + ":" + id + ":" + reference_id;}

}
