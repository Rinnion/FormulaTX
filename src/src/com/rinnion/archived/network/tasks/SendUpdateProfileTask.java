package com.rinnion.archived.network.tasks;

import com.rinnion.archived.database.model.Profile;
import com.rinnion.archived.network.MyNetwork;

/**
 * Created by tretyakov on 21.08.2015.
 */
public class SendUpdateProfileTask extends AsyncActivityTask<Void, Void, Void> {
    private final Profile mUser;

    public SendUpdateProfileTask(Profile user, IAsyncHandler<Void> asyncHandler) {
        super(asyncHandler);

        this.mUser = user;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            MyNetwork.updateProfile(mUser);
        } catch (Exception ex) {
            cancel(true);
        }
        return null;
    }

}
