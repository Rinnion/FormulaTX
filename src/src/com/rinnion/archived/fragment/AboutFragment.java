package com.rinnion.archived.fragment;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.TextView;
import com.rinnion.archived.R;

/**
 * Created with IntelliJ IDEA.
 * User: Lenovo
 * Date: 15.02.14
 * Time: 22:46
 * To change this template use File | Settings | File Templates.                                                              np:\\.\pipe\LOCALDB#C9D6BA74\tsql\query
 */
public class AboutFragment extends Fragment  {

    public static final String TYPE = "TYPE";
    public static final String ENTITY = "ENTITY";
    public static final String TYPE_TOURNAMENT_MAIN = "TOURNMENT_MAIN";
    public static final String TYPE_TOURNAMENT_OTHER = "TOURNMENT_OTHER";
    public static final String TYPE_COMPANY = "COMPANY";
    private String TAG = getClass().getCanonicalName();
    private TextView mTextViewAbout;

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
        View view = inflater.inflate(R.layout.about_layout, container, false);
        mTextViewAbout = (TextView)view.findViewById(R.id.tv_about);

        ActionBar ab = getActivity().getActionBar();
        ab.setTitle("О компании");
        ab.setIcon(R.drawable.ic_action_previous_item);

        Bundle args = getArguments();
        String type = args.getString(TYPE);
        String entity = args.getString(ENTITY);

        type = type == null ? "type:null" : type;
        entity = entity == null ? "entity:null" : entity;

        mTextViewAbout.setText("Описание " + type + " " + entity);

        return view;
    }


}

