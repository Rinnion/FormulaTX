package com.rinnion.archived.fragment;

import android.app.*;
import android.content.*;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import com.rinnion.archived.ArchivedApplication;
import com.rinnion.archived.Utils;
import com.rinnion.archived.database.helper.ApiObjectHelper;
import com.rinnion.archived.database.helper.TournamentHelper;
import com.rinnion.archived.database.model.ApiObjects.Tournament;
import com.rinnion.archived.utils.*;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.rinnion.archived.R;
import org.json.JSONArray;
import org.json.JSONObject;
import org.lorecraft.phparser.SerializedPhpParser;

import java.util.Map;

import java.io.File;
import java.net.URI;

/**
 * Created with IntelliJ IDEA.
 * User: Lenovo
 * Date: 15.02.14
 * Time: 22:46
 * To change this template use File | Settings | File Templates.                                                              np:\\.\pipe\LOCALDB#C9D6BA74\tsql\query
 */
public class OtherTournamentFragment  extends Fragment implements AlertDialogDownloadInterface {

    public static final String TOURNAMENT_POST_NAME = ApiObjectHelper.COLUMN_POST_NAME;
    public static final String ABOUT = "ABOUT";
    public static final String SCHEDULE = "SCHEDULE";
    public static final String GRIDS = "GRIDS";
    public static final String LIVESCORE = "LIVESCORE";
    public static final String VIDEO = "VIDEO";
    public static final String FINDWAY = "FINDWAY";

    private String TAG = getClass().getCanonicalName();
    private AlertDialogDownload alertDialogDownload;
    private String fileName;
    ProgressDialog pd;
    long downloadId;
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
        View view = inflater.inflate(R.layout.main_tournament_list_menu_layout, container, false);

        ActionBar ab = getActivity().getActionBar();
        if (ab != null) {
            ab.setIcon(R.drawable.ic_action_previous_item);
            ab.setHomeButtonEnabled(true);

            String post_name = getArguments().getString(OtherTournamentFragment.TOURNAMENT_POST_NAME);
            TournamentHelper th = new TournamentHelper(ArchivedApplication.getDatabaseOpenHelper());
            Tournament t = th.getByPostName(post_name);
            ab.setTitle(t.title);
        }

        alertDialogDownload=new AlertDialogDownload(getActivity(),"Загрузка данных","Хотите загрузить данные повторно?","Да, загрузить","Нет, открыть существуюший");
        alertDialogDownload.SetListener(this);


        int[] ints = new int[]{R.id.itml_image, R.id.itml_text};
        String[] names = new String[]{"resource", "text", "type"};
        String[] columns = new String[]{"_id", "resource", "text", "type"};
        MatrixCursor mc = new MatrixCursor(columns);
        mc.addRow(new Object[]{1, android.R.drawable.stat_sys_warning,  getResources().getString(R.string.string_tournament_about), ABOUT});
        mc.addRow(new Object[]{2, R.drawable.ic_action_person,  getResources().getString(R.string.string_tournament_schedule), SCHEDULE});
        mc.addRow(new Object[]{2, R.drawable.ic_action_person,  getResources().getString(R.string.string_tournament_grids), GRIDS});
        mc.addRow(new Object[]{2, R.drawable.ic_action_person,  getResources().getString(R.string.string_tournament_liveScore), LIVESCORE});
        mc.addRow(new Object[]{2, R.drawable.ic_action_person,  getResources().getString(R.string.string_photogallery), VIDEO});
        mc.addRow(new Object[]{2, R.drawable.ic_action_person,  getResources().getString(R.string.string_tournament_findway), FINDWAY});
        ListView listView = (ListView) view.findViewById(R.id.mtl_lv_menu);
        SimpleCursorAdapter sca = new SimpleCursorAdapter(getActivity(), R.layout.item_tournament_menu_layout, mc, names, ints, 0){
            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                final ImageView image = (ImageView) view.findViewById(R.id.itml_image);
                final TextView text = (TextView) view.findViewById(R.id.itml_text);

                int resource = cursor.getInt(1);
                String string = cursor.getString(2);
                String type = cursor.getString(3);
                image.setImageResource(resource);
                text.setText(string);
                view.setTag(type);
            }
        };
        listView.setAdapter(sca);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String tag = (String) view.getTag();
                if (tag.equals(ABOUT)) showAboutFragment();
                if (tag.equals(SCHEDULE)) showScheduleFragment();
                if (tag.equals(GRIDS)) showGridsFragment();
                if (tag.equals(LIVESCORE)) showEmptyFragment();
                if (tag.equals(VIDEO)) showGalleryFragment();
                if (tag.equals(FINDWAY)) showEmptyFragment();
            }
        });

        return view;
    }

    void openReader(String filepath)
    {
        File f=new File(filepath);

        if(!f.exists())
        {
            Log.d(TAG, "Full path: " + f.getAbsolutePath() + ", file not found");
            showNetworkNotAvailable();
            return;
        }


        Intent i = new Intent(Intent.ACTION_VIEW,  Uri.fromFile(f));
        //i.setType("application/pdf");
        Log.d(TAG, "Full path: " + f.getAbsolutePath());
        getActivity().startActivity(i);

    }


    private void prepareOpenAction()
    {



        new NetworkConnectionCheck(getActivity()){
            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                Log.d(TAG, "NetworkConnectionCheck, onPostExecute: " + String.valueOf(aBoolean));
                if(aBoolean)
                {

                    prepareDownloadAction();
                }
                else
                {

                    OnNegativeButton();

                }
            }
        }.execute();

    }

    private void prepareDownloadAction()
    {
        File f=new File(fileName);
        String path=getActivity().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath();

        File file=new File(path,f.getName());

        if(file.exists())
        {
            alertDialogDownload.Show();
        }
        else
        {
            OnPositiveButton();
        }
    }

    private void showGridsFragment() {
        String post_name = getArguments().getString(OtherTournamentFragment.TOURNAMENT_POST_NAME);
        TournamentHelper th = new TournamentHelper(ArchivedApplication.getDatabaseOpenHelper());
        Tournament t = th.getByPostName(post_name);
        if (t == null) {
            showNoValueMessage();
        }
        try {

            String string = String.valueOf(t.files);
            Log.d(TAG, "Handle: '" + string.substring(0, (string.length() > 25) ? 25 : string.length()) + ((string.length() > 25) ? "..." : "") + "'");
            JSONArray array = new JSONArray(string);
            String filename = Utils.fixUrlWithFullPath(array.getString(0));
            Log.d(TAG, "Handle file: '" + filename + "'");


            this.fileName=filename;

            prepareOpenAction();




        }catch(Exception ex){
            Log.e(TAG, "ERROR", ex);
            showNoValueMessage();
        }

    }

    private void showNoValueMessage() {
        Toast.makeText(getActivity(), "К сожалению ничего нет", Toast.LENGTH_LONG).show();
    }

    private void showNetworkDownloadError() {

        Toast.makeText(getActivity(),"Ошибка загрузки файла. Проверьте соединение с сетью.", Toast.LENGTH_LONG).show();
    }

    private void showNetworkNotAvailable() {

        Toast.makeText(getActivity(),"К сожалению сеть недоступна", Toast.LENGTH_LONG).show();
    }
    private void showUserCancel() {
        Toast.makeText(getActivity(), "Загрузка отменена пользователем", Toast.LENGTH_LONG).show();
    }


    private void showScheduleFragment() {
        String post_name = getArguments().getString(OtherTournamentFragment.TOURNAMENT_POST_NAME);
        TournamentHelper th = new TournamentHelper(ArchivedApplication.getDatabaseOpenHelper());
        Tournament t = th.getByPostName(post_name);
        if (t == null) {
            showNoValueMessage();
        }
        try {
            String string = String.valueOf(t.files);
            Log.d(TAG, "Handle: '" + string.substring(0, (string.length() > 25) ? 25 : string.length()) + ((string.length() > 25) ? "..." : "") + "'");
            JSONArray array = new JSONArray(string);


            String filename = Utils.fixUrlWithFullPath(array.getString(1));
            Log.d(TAG, "Handle file: '" + filename + "'");


            this.fileName=filename;

            prepareOpenAction();


        }catch(Exception ex){
            Log.e(TAG, "ERROR", ex);
            showNoValueMessage();
        }
    }

    public void showGalleryFragment() {
        GalleryFragment mlf = new GalleryFragment();
        Bundle bundle = new Bundle();
        bundle.putString(GalleryFragment.TOURNAMENT_POST_NAME, getArguments().getString(MainTournamentFragment.TYPE));
        mlf.setArguments(bundle);
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, mlf)
                .addToBackStack(null)
                .commit();
    }

    private void showAboutFragment() {
        AboutFragment mlf = new AboutFragment();
        Bundle bundle = new Bundle();
        bundle.putString(AboutFragment.TYPE, getArguments().getString(TOURNAMENT_POST_NAME));
        mlf.setArguments(bundle);
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, mlf)
                .addToBackStack(null)
                .commit();
    }

    public void showEmptyFragment() {
        EmptyFragment mlf = new EmptyFragment();
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, mlf)
                .addToBackStack(null)
                .commit();
    }
    private boolean validDownload(long downloadId) {

        Log.d(TAG,"Checking download status for id: " + downloadId);

        //Verify if download is a success
        DownloadManager dwm=(DownloadManager)getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        Cursor c= dwm.query(new DownloadManager.Query().setFilterById(downloadId));

        if(c.moveToFirst()){
            int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));

            if(status == DownloadManager.STATUS_SUCCESSFUL){
                return true; //Download is valid, celebrate
            }else{
                int reason = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_REASON));
                Log.d(TAG, "Download not correct, status [" + status + "] reason [" + reason + "]");
                return false;
            }
        }
        return false;
    }

    @Override
    public void OnPositiveButton() {

        Uri uri=Uri.parse(fileName);
        File f=new File(fileName);
        String path=getActivity().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath();

        File file=new File(path,f.getName());



        Log.d(TAG, "Environment.getExternalStoragePublicDirectory: " + path + ", f.getName(): " + f.getName());


        DownloadManager.Request rq=new DownloadManager.Request(uri)
                .setAllowedNetworkTypes((DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE))
                .setAllowedOverRoaming(false)
                .setTitle(f.getName())
                .setDestinationInExternalFilesDir(getActivity(), Environment.DIRECTORY_DOWNLOADS, f.getName())
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);

        //.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, f.getName())

        getActivity().registerReceiver(new MyDownloadBroadcastReceiver(file.getAbsolutePath()) {

                                           @Override
                                           public void onReceive(Context context, Intent intent) {
                                               getActivity().unregisterReceiver(this);
                                               String filePath = (String) this.getObject();


                                               if(validDownload(downloadId)) {

                                                   pd.dismiss();
                                                   openReader(filePath);
                                               }
                                                else
                                               {
                                                   pd.dismiss();
                                                   showNetworkDownloadError();
                                               }


                                           }
                                       },
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        );



        DownloadManager dwm=(DownloadManager)getActivity().getSystemService(Context.DOWNLOAD_SERVICE);


        pd=new ProgressDialog(getActivity());
        pd.setTitle("Загрузка файла...");
        pd.setMessage("Идет загрузка файла");

        pd.setButton(Dialog.BUTTON_NEGATIVE, "Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DownloadManager dwm = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);

                pd.dismiss();
                showUserCancel();
                dwm.remove(downloadId);
            }
        });


        pd.show();
        downloadId=dwm.enqueue(rq);
    }

    @Override
    public void OnNegativeButton() {
        File f=new File(fileName);
        String path=getActivity().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath();

        File file=new File(path,f.getName());
        openReader(file.getAbsolutePath());
    }

    @Override
    public void OnCancel() {

    }
}

