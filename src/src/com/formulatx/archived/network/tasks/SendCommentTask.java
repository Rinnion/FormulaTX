package com.formulatx.archived.network.tasks;

import com.formulatx.archived.network.MyNetwork;

public class SendCommentTask extends AsyncActivityTask<Void, Void, Void> {
    private final String author;
    private final String comment;
    private final String email;
    private final String phone;

    public SendCommentTask(String author, String comment, String email, String phone, IAsyncHandler<Void> handler) {
        super(handler);

        this.author = author;
        this.comment = comment;
        this.email = email;
        this.phone = phone;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            MyNetwork.sendComment(author, comment, email, phone);
        } catch (Exception ex) {
            cancel(true);
        }
        return null;
    }
}

