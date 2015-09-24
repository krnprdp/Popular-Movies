package com.pradeep.nanodegree.popularmovies;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

/**
 * Created by Pradeep on 9/23/15.
 */
public class FavoritesProvider extends ContentProvider {

    DBHelper helper;
    private SQLiteDatabase sqlDB;

    public static String AUTHORITY = "com.pradeep.nanodegree.popularmovies.provider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    @Override
    public boolean onCreate() {
        helper = new DBHelper(getContext());
        sqlDB = helper.getWritableDatabase();
        Log.d("FavoritesProvider", "Content Provider is working - sort of? **************************\n\n**************************");
        if (sqlDB == null)
            return false;
        else
            return true;
    }

    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
