package com.s5884.youremindyoudb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * �f�[�^�x�[�X�����N���X
 */
public class DatabaseOpenHelperItem extends SQLiteOpenHelper {

	/* DataBase���� */
	private static final String DB_NAME_ITEM = "YOUREMINDYOU_ITEM";

	/**
	 * �R���X�g���N�^
	 */
	public DatabaseOpenHelperItem(Context context) {
		super(context, DB_NAME_ITEM, null, 1);
	}

	/**
	 * �f�[�^�x�[�X����
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.beginTransaction();

		try {
			StringBuilder createSql = new StringBuilder();
			createSql.append("create table " + RecordItem.TABLE_NAME_ITEM
					+ " (");
			createSql.append(RecordItem.COLUMN_ID
					+ " integer primary key autoincrement not null, ");
			createSql.append(RecordItem.COLUMN_ITEMTITLE + " text, ");
			createSql.append(RecordItem.COLUMN_ITEMALARMTIME + " integer");
			createSql.append(")");

			db.execSQL(createSql.toString());
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
	}

	/**
	 * �f�[�^�x�[�X�̍X�V
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
}
