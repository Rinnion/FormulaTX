package com.rinnion.archived.fragment.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
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
public class MessageAdapter extends SimpleCursorAdapter {
    private static String[] fromSpinner = {
            MessageHelper.COLUMN_CONTENT,
            MessageHelper.COLUMN_LIKES,
            MessageHelper._ID,
            MessageHelper.COLUMN_TAGS,
            MessageHelper.COLUMN_COMMENTS
    };
    private static int[] toSpinner = {
            R.id.mil_tv_content,
            R.id.mil_tv_like_count,
            R.id.mil_tv_identity,
            R.id.mil_tv_tags,
            R.id.mil_tv_comment_count
    };

    private IMessageClickListener mShareListener;

    public MessageAdapter(Context context, MessageCursor mc, IMessageClickListener shareListener) {
        super(context, R.layout.message_item_layout, mc, fromSpinner, toSpinner, 0);
        mShareListener = shareListener;
    }

    @Override
    public void bindView(View view, Context context, final Cursor cursor) {
        super.bindView(view, context, cursor);

        final Message msg = ((MessageCursor) cursor).getItem();

        view.findViewById(R.id.mil_ll_content).setBackgroundResource(getRandomBackgroundResource());

        final TextView txtLikes = (TextView) view.findViewById(R.id.mil_tv_like_count);
        final ImageView btnShare = (ImageView) view.findViewById(R.id.mil_iv_share);
        final ImageView btnLike = (ImageView) view.findViewById(R.id.mil_iv_like);
        btnLike.setImageResource(msg.like
                ? R.drawable.ic_action_important
                : R.drawable.ic_action_not_important);

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
                txtLikes.setText(String.valueOf(msg.likes));
                btnLike.setImageResource(msg.like ? R.drawable.ic_action_important : R.drawable.ic_action_not_important);
            }
        });

    }

    private int getRandomBackgroundResource() {
        int r = (int) Math.floor(Math.random() * 8);
        switch (r) {
            case 0:
                return R.drawable.bkg_black;
            case 1:
                return R.drawable.bkg_red;
            case 2:
                return R.drawable.bkg_yellow;
            case 3:
                return R.drawable.bkg_green;
            case 4:
                return R.drawable.bkg_cyan;
            case 5:
                return R.drawable.bkg_blue;
            case 6:
                return R.drawable.bkg_purple;
            case 7:
                return R.drawable.bkg_white;
            default:
                return R.drawable.bkg_black;
        }
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
