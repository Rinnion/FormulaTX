package com.rinnion.archived.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.rinnion.archived.R;
import com.rinnion.archived.network.tasks.AsyncActivityTask;
import com.rinnion.archived.network.tasks.SendMessageTask;

/**
 * Created with IntelliJ IDEA.
 * User: Lenovo
 * Date: 15.02.14
 * Time: 22:46
 * To change this template use File | Settings | File Templates.                                                              np:\\.\pipe\LOCALDB#C9D6BA74\tsql\query
 */
public class FeedbackFragment extends Fragment {

    private String TAG = getClass().getCanonicalName();
    private Button mSend;
    private EditText mContent;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.feedback_layout, container, false);
        mContent = (EditText) view.findViewById(R.id.nml_ed_content);
        mSend = (Button) view.findViewById(R.id.nml_bt_send);
        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = mContent.getText().toString();
                SendMessageTask sct = new SendMessageTask(message, new SendMessageAsyncHandler());
                sct.execute();
            }
        });
        return view;
    }

    private class SendMessageAsyncHandler implements AsyncActivityTask.IAsyncHandler<Void> {
        @Override
        public void onBeforeExecute() {
            mSend.setEnabled(false);
        }

        @Override
        public void onAfterExecute(Void aVoid) {
            mSend.setEnabled(true);
            //TODO roll back to previos fragment
            getFragmentManager().popBackStack();
        }

        @Override
        public void onCancelExecute(Void aVoid) {
            mSend.setEnabled(true);
        }
    }
}
