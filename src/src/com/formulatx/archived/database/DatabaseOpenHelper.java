package com.formulatx.archived.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.formulatx.archived.utils.Log;
import com.rinnion.archived.R;

/**
 * Created with IntelliJ IDEA.
 * User: tretyakov
 * Date: 25.10.13
 * Time: 14:17
 * To change this template use File | Settings | File Templates.
 */
public class DatabaseOpenHelper extends SQLiteOpenHelper {
    public static int DATABASE_VERSION = 11;
    private static String DATABASE_NAME = "FormulaTXDb";
    private final Context mContext;
    private final String TAG = "DatabaseOpenHelper";

    public DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        createDatabase(sqLiteDatabase);
    }

    private void createDatabase(SQLiteDatabase sqLiteDatabase) {
        Log.i(TAG, "createDatabase");
        sqLiteDatabase.beginTransaction();
        try {
            executeSqlStringArray(sqLiteDatabase, R.array.sqlsCreate);
            sqLiteDatabase.setTransactionSuccessful();
        } catch (SQLException e) {
            Log.e(TAG, "Error creating tables", e);
        } finally {
            sqLiteDatabase.endTransaction();
        }

    }

    private void executeSqlStringArray(SQLiteDatabase sqLiteDatabase, int id) {
        String[] sqlArray = this.mContext.getResources().getStringArray(id);
        for (String aSqlArray : sqlArray) {
            sqLiteDatabase.execSQL(aSqlArray);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        executeSqlStringArray(sqLiteDatabase, R.array.sqlsUpgrade);
        createDatabase(sqLiteDatabase);
    }
}
