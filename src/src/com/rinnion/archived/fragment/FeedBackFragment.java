package com.rinnion.archived.fragment;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.rinnion.archived.R;
import com.rinnion.archived.database.model.ApiObject;
import com.rinnion.archived.utils.Log;
import com.rinnion.archived.network.tasks.SendCommentTask;

/**
 * Created with IntelliJ IDEA.
 * User: Lenovo
 * Date: 15.02.14
 * Time: 22:46
 * To change this template use File | Settings | File Templates.                                                              np:\\.\pipe\LOCALDB#C9D6BA74\tsql\query
 */
public class FeedBackFragment extends Fragment  {

    public static final String TYPE = "TYPE";
    private String TAG = getClass().getCanonicalName();
    private ApiObject mApiObject;
    private View btnSend;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected");
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.d(TAG, "onOptionsItemSelected: 'home' selected");
                getActivity().getFragmentManager().popBackStack();
                return true;
            default:
                Log.d(TAG, "onOptionsItemSelected: default section");
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.feedback_layout, container, false);
        final TextView edName = (TextView) view.findViewById(R.id.fbl_ed_name);
        final TextView edEmail = (TextView) view.findViewById(R.id.fbl_ed_email);
        final TextView edFeedBack= (TextView) view.findViewById(R.id.fbl_ed_feedback);
        btnSend = view.findViewById(R.id.fdl_btn_send);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edName.getText().toString();
                String email = edEmail.getText().toString();
                String comment = edFeedBack.getText().toString();
                String phone = "+0(000)000-00-00";
                if (name.isEmpty() || email.isEmpty() || comment.isEmpty() || phone.isEmpty()) return;
                SendCommentTask sct = new SendCommentTask(name, comment, email, phone, new AsyncActivityTask.IAsyncHandler<Void>() {
                    @Override
                    public void onBeforeExecute() {
                        btnSend.setEnabled(false);
                    }

                    @Override
                    public void onAfterExecute(Void aVoid) {
                        edName.setText("");
                        edEmail.setText("");
                        edFeedBack.setText("");
                        btnSend.setEnabled(true);
                    }

                    @Override
                    public void onCancelExecute(Void aVoid) {
                        btnSend.setEnabled(true);
                    }
                });
                sct.execute();
            }
        });


        return view;
    }

    @Override
    public void onStart() {

        super.onStart();

        ActionBar ab = getActivity().getActionBar();
        if (ab != null) {
            ab.setTitle(R.string.string_contacts);
            ab.setIcon(R.drawable.ic_action_previous_item);
        }

    }
}

