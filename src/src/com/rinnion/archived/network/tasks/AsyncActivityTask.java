package com.rinnion.archived.network.tasks;

import android.os.AsyncTask;

public abstract class AsyncActivityTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {
    private IAsyncHandler<Result> mAsyncHandler;

    public AsyncActivityTask(IAsyncHandler<Result> asyncHandler) {
        mAsyncHandler = asyncHandler;
    }

    @Override
    protected final void onPreExecute() {
        raiseOnBeforeExecute();
    }

    @Override
    protected final void onPostExecute(Result result) {
        raiseOnAfterExecute(result);
    }

    @Override
    protected final void onCancelled(Result result) {
        raiseOnCancelExecute(result);
    }

    protected final void raiseOnBeforeExecute() {
        if (mAsyncHandler == null) return;
        mAsyncHandler.onBeforeExecute();
    }

    protected final void raiseOnAfterExecute(Result result) {
        if (mAsyncHandler == null) return;
        mAsyncHandler.onAfterExecute(result);
    }

    protected final void raiseOnCancelExecute(Result result) {
        if (mAsyncHandler == null) return;
        mAsyncHandler.onCancelExecute(result);
    }

    public interface IAsyncHandler<Result> {
        void onBeforeExecute();

        void onAfterExecute(Result result);

        void onCancelExecute(Result result);
    }

}
