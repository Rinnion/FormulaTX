package com.rinnion.archived.database.model;

import java.io.Serializable;

public class Profile implements Serializable {
    public final String name;
    public final String avatar;

    public Profile(String name, String avatar) {
        this.name = name;
        this.avatar = avatar;
    }
}
