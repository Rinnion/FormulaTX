package com.formulatx.archived.database.cursor;

import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import com.formulatx.archived.database.helper.ApiObjectHelper;
import com.formulatx.archived.database.model.ApiObject;

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
        ApiObject apiObject = new ApiObject(id);
        fillApiObject(apiObject);
        return apiObject;
    }

    protected void fillApiObject(ApiObject apiObject) {
        apiObject.comment_status = getColByName(ApiObjectHelper.COLUMN_COMMENT_STATUS);
        apiObject.user = getColByName(ApiObjectHelper.COLUMN_USER);
        apiObject.date = getColByName(ApiObjectHelper.COLUMN_DATE);
        apiObject.modified = getColByName(ApiObjectHelper.COLUMN_MODIFIED);
        apiObject.content = getColByName(ApiObjectHelper.COLUMN_CONTENT);
        apiObject.title = getColByName(ApiObjectHelper.COLUMN_TITLE);
        apiObject.status =getColByName(ApiObjectHelper.COLUMN_STATUS);
        apiObject.password = getColByName(ApiObjectHelper.COLUMN_PASSWORD);
        apiObject.post_name = getColByName(ApiObjectHelper.COLUMN_POST_NAME);
        apiObject.link = getColByName(ApiObjectHelper.COLUMN_LINK);
        apiObject.type =getColByName(ApiObjectHelper.COLUMN_TYPE);
        apiObject.parent = getColByName(ApiObjectHelper.COLUMN_PARENT);
        apiObject.meta_title = getColByName(ApiObjectHelper.COLUMN_META_TITLE);
        apiObject.meta_description = getColByName(ApiObjectHelper.COLUMN_META_DESCRIPTION);
        apiObject.meta_keywords = getColByName(ApiObjectHelper.COLUMN_META_KEYWORDS);
        apiObject.display_method = getColByName(ApiObjectHelper.COLUMN_DISPLAY_METHOD);
        apiObject.rss = getColByName(ApiObjectHelper.COLUMN_RSS);
        apiObject.files = getColByName(ApiObjectHelper.COLUMN_FILES);
        apiObject.thumb = getColByName(ApiObjectHelper.COLUMN_THUMB);
        apiObject.lang = getColByName(ApiObjectHelper.COLUMN_LANG);
        apiObject.lang_id = getColByName(ApiObjectHelper.COLUMN_LANG_ID);
        apiObject.references_include = getColByName(ApiObjectHelper.COLUMN_REFERENCES_INCLUDE);
        apiObject.gallery_include = getColByName(ApiObjectHelper.COLUMN_GALLERY_INCLUDE);
        apiObject.tables = getColByName(ApiObjectHelper.COLUMN_TABLES);
        apiObject.parsers_include = getColByName(ApiObjectHelper.COLUMN_PARSERS_INCLUDE);
        apiObject.login = getColByName(ApiObjectHelper.COLUMN_LOGIN);
        apiObject.update_time = getColUpdateTime();
    }

    private String getColByName(String name) {
        return getString(getColumnIndexOrThrow(name));
    }

    public long getColId() {
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
