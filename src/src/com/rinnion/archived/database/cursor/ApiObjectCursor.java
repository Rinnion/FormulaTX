package com.rinnion.archived.database.cursor;

import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import com.rinnion.archived.database.helper.ApiObjectHelper;
import com.rinnion.archived.database.model.ApiObject;

/**
 * Created with IntelliJ IDEA.
 * User: tretyakov
 * Date: 25.10.13
 * Time: 14:31
 * To change this template use File | Settings | File Templates.
 */
public class ApiObjectCursor extends SQLiteCursor {

    public ApiObjectCursor(SQLiteDatabase db, SQLiteCursorDriver driver, String editTable, SQLiteQuery query) {
        super(db, driver, editTable, query);
    }

    public ApiObject getItem() {
        long id = getColId();
        int objType=getColType();
        ApiObject apiObject = new ApiObject(id,objType);
        apiObject.comment_status = "comment_status";
        apiObject.comment_status = "user";
        apiObject.comment_status = "date";
        apiObject.comment_status = "modified";
        apiObject.comment_status = "content";
        apiObject.comment_status = "title";
        apiObject.comment_status = "status";
        apiObject.comment_status = "comment_status";
        apiObject.comment_status = "password";
        apiObject.comment_status = "post_name";
        apiObject.comment_status = "link";
        apiObject.comment_status = "type";
        apiObject.comment_status = "parent";
        apiObject.comment_status = "meta_title";
        apiObject.comment_status = "meta_description";
        apiObject.comment_status = "meta_keywords";
        apiObject.comment_status = "display_method";
        apiObject.comment_status = "rss";
        apiObject.comment_status = "files";
        apiObject.comment_status = "thumb";
        apiObject.comment_status = "lang";
        apiObject.comment_status = "lang_id";
        apiObject.comment_status = "references_include";
        apiObject.comment_status = "gallery_include";
        apiObject.comment_status = "tables";
        apiObject.comment_status = "parsers_include";
        apiObject.comment_status = "login";
        apiObject.update_time = getColUpdateTime();

        return apiObject;
    }

    private long getColId() {
        return getLong(getColumnIndexOrThrow(ApiObjectHelper._ID));
    }

    private int getColType() {
        return getInt(getColumnIndexOrThrow(ApiObjectHelper.COLUMN_OBJ_TYPE));
    }

    private long getColUpdateTime() {
        return getLong(getColumnIndexOrThrow(ApiObjectHelper.COLUMN_UPDATE_TIME));
    }

    public static class Factory implements SQLiteDatabase.CursorFactory {

        @Override
        public Cursor newCursor(SQLiteDatabase sqLiteDatabase, SQLiteCursorDriver sqLiteCursorDriver, String s, SQLiteQuery sqLiteQuery) {
            ApiObjectCursor c = new ApiObjectCursor(sqLiteDatabase, sqLiteCursorDriver, s, sqLiteQuery);
            return c;
        }
    }
}
