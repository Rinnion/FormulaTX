package com.formulatx.archived.database.helper;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.text.TextUtils;
import com.formulatx.archived.database.DatabaseOpenHelper;
import com.formulatx.archived.database.cursor.ProductCursor;
import com.formulatx.archived.database.model.ApiObjects.Product;
import com.formulatx.archived.database.model.ApiObject;
import com.formulatx.archived.utils.Log;

/**
 * Helper for working with News repository
 */
public class ProductHelper implements BaseColumns {

    private static final String TAG = "ProductHelper";

    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_TOP = "top";
    public static final String COLUMN_FAVORITE = "favorite";
    public static final String COLUMN_THUMB = "ao_" + ApiObjectHelper.COLUMN_THUMB;
    public static final String COLUMN_TITLE = "ao_" + ApiObjectHelper.COLUMN_TITLE;
    public static final String COLUMN_CONTENT = "ao_" + ApiObjectHelper.COLUMN_CONTENT;

    public static String DATABASE_TABLE_ADDITINAL = "products";
    private final ApiObjectHelper aoh;
    private DatabaseOpenHelper doh;

    public ProductHelper(DatabaseOpenHelper doh) {
        this.doh = doh;
        aoh = new ApiObjectHelper(doh);
    }

    public static String[] COLS_ADDITIONAL;
    public static String ALL_COLUMNS_ADDITINAL;

    static {
        COLS_ADDITIONAL = new String[]{
                _ID,
                COLUMN_PRICE,
                COLUMN_TOP,
                COLUMN_FAVORITE
        };
        ALL_COLUMNS_ADDITINAL = TextUtils.join(",", COLS_ADDITIONAL);
    }

    public boolean merge(Product product) {
        Log.d(TAG, "merge(" + product.toString() + ")");

        //FIXME: Нужно переделать на update/merge
        ApiObject apiObject = aoh.get(product.id);

        delete(product.id);
        apiObject.thumb = product.thumb;
        aoh.add(apiObject);

        ContentValues map;
        map = new ContentValues();
        map.put(_ID, product.id);
        map.put(COLUMN_PRICE, product.price);
        map.put(COLUMN_TOP, product.top);
        map.put(COLUMN_FAVORITE,product.favorite?1:0);
        try {
            SQLiteDatabase db = doh.getWritableDatabase();
            return (db.insert(DATABASE_TABLE_ADDITINAL, null, map)!=-1);

        } catch (SQLException e) {
            Log.e(TAG, "Error writing location", e);
            return false;
        }
    }

    public void delete(long id) {
        Log.d(TAG, "delete (" + id + ")");
        try {
            Log.d(TAG, "Delete self location: " + id);
            SQLiteDatabase db = doh.getWritableDatabase();
            String[] args = {Long.toString(id)};
            db.delete(DATABASE_TABLE_ADDITINAL, _ID + "=?", args);
            aoh.delete(id);
        } catch (SQLException ex) {
            Log.e(TAG, "Error delete self location", ex);
        }
    }

    public Product getProduct(long id) {
        Log.v(TAG, "getProduct (" + id + ")");

        String sql = "SELECT g." + ALL_COLUMNS_ADDITINAL +
                ", ao." + ApiObjectHelper.COLUMN_THUMB + " AS " + COLUMN_THUMB +
                ", ao." + ApiObjectHelper.COLUMN_TITLE + " AS " + COLUMN_TITLE +
                ", ao." + ApiObjectHelper.COLUMN_CONTENT + " AS " + COLUMN_CONTENT +
                " FROM " + DATABASE_TABLE_ADDITINAL + " AS g " +
                " LEFT JOIN " + ApiObjectHelper.DATABASE_TABLE + " AS ao ON ao._id=g._id " +
                " WHERE " + ApiObjectHelper.COLUMN_DISPLAY_METHOD +"=? AND g._id=?";
        SQLiteDatabase d = doh.getReadableDatabase();
        ProductCursor c = (ProductCursor) d.rawQueryWithFactory(
                new ProductCursor.Factory(),
                sql,
                new String[]{String.valueOf(ApiObject.PRODUCT), String.valueOf(id)},
                null);
        if (c.getCount() == 0) return null;
        c.moveToFirst();
        return c.getItem();
    }

    public ProductCursor getAllByParent(long parent) {
        Log.v(TAG, "getAllByParent ("+parent+")");

        String sql = "SELECT g." + ALL_COLUMNS_ADDITINAL +
                ", ao." + ApiObjectHelper.COLUMN_THUMB + " AS " + COLUMN_THUMB +
                ", ao." + ApiObjectHelper.COLUMN_TITLE + " AS " + COLUMN_TITLE +
                ", ao." + ApiObjectHelper.COLUMN_CONTENT + " AS " + COLUMN_CONTENT +
                " FROM " + DATABASE_TABLE_ADDITINAL + " AS g " +
                " LEFT JOIN " + ApiObjectHelper.DATABASE_TABLE + " AS ao ON ao._id=g._id " +
                " LEFT JOIN " + ApiObjectHelper.DATABASE_TABLE + " AS p ON ao.parent = p.post_name " +
                " WHERE ao." + ApiObjectHelper.COLUMN_DISPLAY_METHOD + "=? AND p." + _ID + "=? ";

        SQLiteDatabase d = doh.getReadableDatabase();
        ProductCursor c = (ProductCursor) d.rawQueryWithFactory(
                new ProductCursor.Factory(),
                sql,
                new String[]{String.valueOf(ApiObject.PRODUCT), String.valueOf(parent)},
                null);
        c.moveToFirst();
        return c;
    }

    public ProductCursor getAll() {
        Log.v(TAG, "getAll ()");

        String sql = "SELECT g." + ALL_COLUMNS_ADDITINAL +
                ", ao." + ApiObjectHelper.COLUMN_THUMB + " AS " + COLUMN_THUMB +
                ", ao." + ApiObjectHelper.COLUMN_TITLE + " AS " + COLUMN_TITLE +
                ", ao." + ApiObjectHelper.COLUMN_CONTENT + " AS " + COLUMN_CONTENT +
                " FROM " + DATABASE_TABLE_ADDITINAL + " AS g " +
                " LEFT JOIN " + ApiObjectHelper.DATABASE_TABLE + " AS ao ON ao._id=g._id " +
                " WHERE ao." + ApiObjectHelper.COLUMN_DISPLAY_METHOD + "=?";

        SQLiteDatabase d = doh.getReadableDatabase();
        ProductCursor c = (ProductCursor) d.rawQueryWithFactory(
                new ProductCursor.Factory(),
                sql,
                new String[]{String.valueOf(ApiObject.PRODUCT)},
                null);
        c.moveToFirst();
        return c;
    }

    public ProductCursor getFavorites() {
        Log.v(TAG, "getAll ()");

        String sql = "SELECT g." + ALL_COLUMNS_ADDITINAL +
                ", ao." + ApiObjectHelper.COLUMN_THUMB + " AS " + COLUMN_THUMB +
                ", ao." + ApiObjectHelper.COLUMN_TITLE + " AS " + COLUMN_TITLE +
                ", ao." + ApiObjectHelper.COLUMN_CONTENT + " AS " + COLUMN_CONTENT +
                " FROM " + DATABASE_TABLE_ADDITINAL + " AS g " +
                " LEFT JOIN " + ApiObjectHelper.DATABASE_TABLE + " AS ao ON ao._id=g._id " +
                " WHERE ao." + ApiObjectHelper.COLUMN_DISPLAY_METHOD + "=? and g." + COLUMN_FAVORITE + "=1" ;

        SQLiteDatabase d = doh.getReadableDatabase();
        ProductCursor c = (ProductCursor) d.rawQueryWithFactory(
                new ProductCursor.Factory(),
                sql,
                new String[]{String.valueOf(ApiObject.PRODUCT)},
                null);
        c.moveToFirst();
        return c;
    }
}
