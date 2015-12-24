package com.rinnion.archived.fragment.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import com.rinnion.archived.R;
import com.rinnion.archived.database.cursor.MessageCursor;
import com.rinnion.archived.database.helper.MessageHelper;
import com.rinnion.archived.database.model.Message;
import com.rinnion.archived.network.tasks.AsyncActivityTask;
import com.rinnion.archived.network.tasks.SendDislikeTask;
import com.rinnion.archived.network.tasks.SendLikeTask;

/**
 * Created by tretyakov on 18.08.2015.
 */
public class DraftAdapter extends SimpleCursorAdapter {
    private static String[] fromSpinner = {
            MessageHelper.COLUMN_CONTENT,
            MessageHelper._ID,
            MessageHelper.COLUMN_TAGS
    };
    private static int[] toSpinner = {
            R.id.dil_tv_content,
            R.id.dil_tv_identity,
            R.id.dil_tv_tags
    };

    private IMessageClickListener mShareListener;

    public DraftAdapter(Context context, MessageCursor mc, IMessageClickListener shareListener) {
        super(context, R.layout.draft_item_layout, mc, fromSpinner, toSpinner, 0);
        mShareListener = shareListener;
    }

    @Override
    public void bindView(View view, Context context, final Cursor cursor) {
        super.bindView(view, context, cursor);

        final Message msg = ((MessageCursor) cursor).getItem();

        final ImageView btnShare = (ImageView) view.findViewById(R.id.dil_iv_share);
        final ImageView btnLike = (ImageView) view.findViewById(R.id.dil_iv_like);
        btnLike.setImageResource(msg.like ? R.drawable.ic_action_important : R.drawable.ic_action_not_important);

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mShareListener == null) return;
                mShareListener.Share(msg);
            }
        });

        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncActivityTask<Void, Void, Void> slt = null;
                if (!msg.like) {
                    slt = new SendLikeTask(msg.id, new SendLikeAsyncHandler());
                    msg.like = true;
                    msg.likes++;
                } else {
                    slt = new SendDislikeTask(msg.id, new SendLikeAsyncHandler());
                    msg.like = false;
                    msg.likes--;
                }
                slt.execute();
                btnLike.setImageResource(msg.like ? R.drawable.ic_action_important : R.drawable.ic_action_not_important);
            }
        });

    }

    public interface IMessageClickListener {
        void Share(Message message);
    }

    private class SendLikeAsyncHandler implements AsyncActivityTask.IAsyncHandler<Void> {

        @Override
        public void onBeforeExecute() {
        }

        @Override
        public void onAfterExecute(Void aVoid) {
        }

        @Override
        public void onCancelExecute(Void aVoid) {
        }
    }
}
