package com.coeus.pdfreader.ormlite;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.artifex.mupdflib.R;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	private static final String DATABASE_NAME = "pdfbookmarks.db";
	private static final int DATABASE_VERSION = 1;

	private Dao<BookmarksORM, Integer> bookmarksDao = null;

	/************************ DatabaseHelper CTOR ***************************************/
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION,
				R.raw.ormlite_config);
	}

	/************************ onCreate ***************************************/
	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
			TableUtils.createTable(connectionSource, BookmarksORM.class);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	/***************************** onUpgrade ************************************/
	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
			int oldVersion, int newVersion) {
		try {
			TableUtils.dropTable(connectionSource, BookmarksORM.class, true);
			// after we drop the old databases, we create the new ones
			onCreate(db, connectionSource);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**************************************************************/
	public Dao<BookmarksORM, Integer> getBookmarkDao() throws SQLException {
		if (bookmarksDao == null) {
			bookmarksDao = getDao(BookmarksORM.class);
		}
		return bookmarksDao;
	}


	/**************************************************************/

}
