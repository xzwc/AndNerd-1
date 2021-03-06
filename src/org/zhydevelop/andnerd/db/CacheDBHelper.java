package org.zhydevelop.andnerd.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Helper class to create & upgrade database cache tables
 */
public class CacheDBHelper extends SQLiteOpenHelper {
    /**
     * Version constant to increment when the database should be rebuilt
     */
    private static final int DB_VERSION = 0;

    /**
     * Name of database file
     */
    private static final String DB_NAME = "cache.db";

    /**
     * @param context
     */
    public CacheDBHelper(final Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
        //TODO
        db.execSQL("CREATE TABLE history ([keyword] VARCHAR(20) NOT NULL ON CONFLICT REPLACE, [timestamp] TIME);");
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion,
            final int newVersion) {
    	//TODO
        db.execSQL("DROP TABLE IF EXISTS history");
        onCreate(db);
    }
}