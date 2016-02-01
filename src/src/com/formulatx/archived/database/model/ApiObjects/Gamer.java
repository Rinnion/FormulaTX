package com.formulatx.archived.database.model.ApiObjects;

/**
 * Created by tretyakov on 28.12.2015.
 */
public class Gamer {

    public long id;
    public String thumb;
    public Boolean favorite;
    public String name;
    public String surname;
    public String full_name;
    public String title;
    public float rating;
    public String country;
    public String flag;

    public Gamer(long id) {
        this.id = id;
    }



}
