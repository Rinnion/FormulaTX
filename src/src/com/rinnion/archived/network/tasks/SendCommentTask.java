package com.rinnion.archived.network.tasks;

import com.rinnion.archived.network.MyNetwork;

/**
 * Created by tretyakov on 10.07.2015.
 */
public class SendCommentTask extends AsyncActivityTask<Void, Void, Void> {
    private final long mMessageId;
    private final String mComment;

    public SendCommentTask(long messageId, String comment, IAsyncHandler<Void> asyncHandler) {
        super(asyncHandler);
        this.mMessageId = messageId;
        this.mComment = comment;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            MyNetwork.sendComment(mMessageId, mComment);
        } catch (Exception ex) {
            cancel(true);
        }
        return null;
    }
}

