package com.s5884.youremindyoudb;

import java.io.Serializable;

@SuppressWarnings("serial")
public class RecordItem implements Serializable{

	/* �e�[�u���� */
	public static final String TABLE_NAME_ITEM = "tbl_voice_alarms";

	/* �J������ */
    /* ���ۂ�DataBase�ɋL�^�����J������ */
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_ITEMTITLE = "title";
	public static final String COLUMN_ITEMALARMTIME = "alarmtime";

	/* �v���p�e�B */
    /* �ۑ��̏��⌟�����Ȃǂ�ێ�������̂ɂȂ�܂� */
	private int rowid = 0;
	private String itemtitle = null;
	private long itemalarmtime = 0;

    /* �v���p�e�B�𑀍삷�郁�\�b�h�B�ł� */
	public int getRowid() { return rowid; }
	public void setRowid(int rowid) { this.rowid = rowid; }

	public String getItemTitle() { return itemtitle; }
	public void setItemTitle(String paramTitle) { this.itemtitle = paramTitle; }
	
	public long getItemAlarmTime() { return itemalarmtime; }
	public void setItemAlarmTime(long paramAlarmTime) { this.itemalarmtime = paramAlarmTime; }
	

    /**
     * ListView�ŕ\�����܂�
     */
	@Override
	public String toString() {

		/* ���L�̖{�����擾 */
		String title = getItemTitle();
		/* ���s���폜 */
		title = title.replaceAll( "\n", "" );

		StringBuilder builder = new StringBuilder();
		builder.append(getItemAlarmTime());

		return builder.toString();
	}
}
