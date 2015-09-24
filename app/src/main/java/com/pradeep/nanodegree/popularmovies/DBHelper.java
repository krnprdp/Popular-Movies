package com.pradeep.nanodegree.popularmovies;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Pradeep on 9/23/15.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final String ID_FIELD = "id";
    public static final String TITLE_FIELD = "title";
    public static final String POSTER_FIELD = "poster";
    public static final String SYNOPSIS_FIELD = "synopsis";
    public static final String RATING_FIELD = "rating";
    public static final String RELEASE_FIELD = "release";

    private static final String DATABASE_NAME = "database";
    private static final String TABLE_NAME = "FavouriteMovies";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                        ID_FIELD + " TEXT PRIMARY KEY, " +
                        TITLE_FIELD + " TEXT," +
                        POSTER_FIELD + " Text," +
                        SYNOPSIS_FIELD + " Text," +
                        RATING_FIELD + " Text," +
                        RELEASE_FIELD + " Text);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
