package com.rinnion.archived.network.tasks;

import com.rinnion.archived.network.MyNetwork;

/**
 * Async sending message
 */
public class SendMessageTask extends AsyncActivityTask<Void, Void, Void> {
    private final String mMessage;

    public SendMessageTask(String message, IAsyncHandler<Void> asyncHandler) {
        super(asyncHandler);
        this.mMessage = message;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            MyNetwork.sendMessage(mMessage);
        } catch (Exception ex) {
            cancel(true);
        }
        return null;
    }
}

