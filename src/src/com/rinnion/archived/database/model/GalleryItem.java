package com.rinnion.archived.database.model;

import java.io.Serializable;

public class GalleryItem implements Serializable {
    private final String TAG = GalleryItem.class.getSimpleName();

    public final long id;
    public long gallery_id;
    public final String type;
    public final String url;

    public GalleryItem(long id, long gallery_id, String type, String url) {
        this.id = id;
        this.gallery_id = gallery_id;
        this.type = type;
        this.url = url;
    }



}
