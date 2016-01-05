package com.rinnion.archived.database.model;

import java.io.Serializable;

public class GalleryItem implements Serializable {
    private final String TAG = GalleryItem.class.getSimpleName();

    public final long id;
    public long gallery_id;
    public final String type;
    public final String url;
    public String link;

    public GalleryItem(long id, long gallery_id, String type, String url, String link) {
        this.id = id;
        this.gallery_id = gallery_id;
        this.type = type;
        this.url = url;
        this.link = link;
    }



}
