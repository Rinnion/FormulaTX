package com.rinnion.archived.network.tasks;

import com.rinnion.archived.network.MyNetwork;

public class SendLikeTask extends AsyncActivityTask<Void, Void, Void> {

    private final long messageId;

    public SendLikeTask(long messageId, IAsyncHandler<Void> asyncHandler) {
        super(asyncHandler);
        this.messageId = messageId;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            MyNetwork.sendLike(messageId);
        } catch (Exception ex) {
            cancel(true);
        }
        return null;
    }

}
