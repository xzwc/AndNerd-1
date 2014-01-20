package org.zhydevelop.andnerd.util;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Given a PersistableResource, this class will take support loading/storing
 * it's data or requesting fresh data, as appropriate.
 */
public class DBManager {
    /**
     * Get writable database
     *
     * @param helper
     * @return writable database or null if it failed to create/open
     */
    protected SQLiteDatabase getWritable(SQLiteOpenHelper helper) {
        try {
            return helper.getWritableDatabase();
        } catch (SQLiteException e) {
            return null;
        }
    }

    /**
     * Get readable database
     *
     * @param helper
     * @return readable database or null if it failed to create/open
     */
    protected SQLiteDatabase getReadable(SQLiteOpenHelper helper) {
        try {
            return helper.getReadableDatabase();
        } catch (SQLiteException e) {
            return null;            
        }
    }
    
}