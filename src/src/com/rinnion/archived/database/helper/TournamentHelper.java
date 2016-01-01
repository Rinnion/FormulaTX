package com.rinnion.archived.database.helper;

import com.rinnion.archived.database.DatabaseOpenHelper;
import com.rinnion.archived.database.model.ApiObject;
import com.rinnion.archived.database.model.ApiObjects.Tournament;

/**
 * Helper for working with News repository
 */
public class TournamentHelper extends ApiObjectHelper {

    public TournamentHelper(DatabaseOpenHelper doh) {
        super(doh);
    }

    @Override
    public boolean add(ApiObject apiObject) {
        return super.add(apiObject);
    }

    public boolean add(Tournament tournament) {
        return super.add(tournament);
    }
}
