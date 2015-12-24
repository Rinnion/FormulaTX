package com.rinnion.archived.fragment;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;
import com.rinnion.archived.R;
import com.rinnion.archived.database.cursor.MessageCursor;
import com.rinnion.archived.database.model.Message;
import com.rinnion.archived.fragment.adapter.MessageAdapter;
import com.rinnion.archived.network.loaders.MessageAsyncLoader;

/**
 * Created with IntelliJ IDEA.
 * User: Lenovo
 * Date: 15.02.14
 * Time: 22:46
 * To change this template use File | Settings | File Templates.                                                              np:\\.\pipe\LOCALDB#C9D6BA74\tsql\query
 */
public class PhotoGalleryFragment extends Fragment implements LoaderManager.LoaderCallbacks<MessageCursor> {

    private String TAG = getClass().getCanonicalName();
    private ResourceCursorAdapter mAdapter;
    private ListView mListView;
    private TextView mTextView;
    private AdapterView.OnItemClickListener mListener;
    private View mEmpty;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, final MenuInflater inflater) {
        Log.d(TAG, "onCreateOptionsMenu");
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_message, menu);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        mAdapter = new MessageAdapter(getActivity(), null, new MessageAdapter.IMessageClickListener() {
            @Override
            public void Share(Message message) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, message.content);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });

        getLoaderManager().initLoader(R.id.message_loader, Bundle.EMPTY, this);

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.message_list_layout, container, false);
        mListView = (ListView) view.findViewById(R.id.ml_lv_list);
        mEmpty = view.findViewById(R.id.ml_tv_empty);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mListener != null) mListener.onItemClick(parent, view, position, id);
            }
        });

        mListView.setEmptyView(mEmpty);


        mListView.setAdapter(mAdapter);

        return view;
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        mListener = listener;
    }

    @Override
    public Loader<MessageCursor> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader");
        return new MessageAsyncLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<MessageCursor> loader, MessageCursor data) {
        Log.d(TAG, "onLoadFinished");
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<MessageCursor> loader) {
        Log.d(TAG, "onLoaderReset");
    }


}

