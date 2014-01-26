package org.zhydevelop.andnerd.db;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.zhydevelop.andnerd.R;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Helper class to create & upgrade database cache tables
 */
public class InternalDBHelper extends SQLiteOpenHelper {
	/**
	 * Version constant to increment when the database should be rebuilt
	 */
	private static final int DB_VERSION = 0;
	private Context context;

	/**
	 * Name of database file
	 */
	private static final String DB_NAME = "internal.db";

	/**
	 * @param context
	 */
	public InternalDBHelper(final Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(final SQLiteDatabase db) {
		db.execSQL("CREATE TABLE history ([keyword] VARCHAR(20) NOT NULL ON CONFLICT REPLACE, [timestamp] TIME);");
	}

	@Override
	public void onUpgrade(final SQLiteDatabase db, final int oldVersion,
			final int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS history");
		onCreate(db);
	}

	public synchronized SQLiteDatabase getReadableDatabase() {
		try {
			//解压文件
			FileOutputStream fos;
			InputStream fis = context.getResources().openRawResource(R.raw.internal);
			fos = context.openFileOutput(
					InternalDBHelper.DB_NAME, Context.MODE_PRIVATE);

			byte[] buffer = new byte[fis.available()];
			int len = 0;
			len = fis.read(buffer);
			fos.write(buffer, 0, len);

			fis.close();
			fos.close();
		} catch (IOException e) {
			return null;
		}

		SQLiteDatabase db = null;
		String path = context.getFileStreamPath(
				InternalDBHelper.DB_NAME).getAbsolutePath();
		try {
			db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
			//update
			if(db.getVersion() < DB_VERSION) {
				db.close();
				//reopen
				db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
			}
		} catch(SQLException ex) {

		}
		return db;
	}
}