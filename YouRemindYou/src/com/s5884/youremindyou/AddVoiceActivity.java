package com.s5884.youremindyou;

import java.util.Calendar;

import com.s5884.youremindyou.AddVoiceActivity;
import com.s5884.youremindyou.R;
import com.s5884.youremindyoudb.RecordDaoItem;
import com.s5884.youremindyoudb.RecordItem;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class AddVoiceActivity extends Activity implements OnClickListener {

	// ----------------------------------------------------------
	// ////////////////// In-class Variables ////////////////////
	// ----------------------------------------------------------
	private RecordItem Record = null;
	private EditText titleOfVoice = null;
	private int year, month, day, hour, minute;
	MediaRecorder recorder;

	// ----------------------------------------------------------
	// ////////////////////// onCreate() ////////////////////////
	// ----------------------------------------------------------
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_voice);

		// ======================= Action Bar =======================
		// set Navigation Mode
		getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		// show up-button on the Action Bar
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setTitle(R.string.menu_add);

		// ======================= EditText =======================
		titleOfVoice = (EditText)findViewById(R.id.editText1);

		// ======================= Image Button =======================
		// get an ImageButton for finish adding voice
		final ImageButton doneImgBtn = (ImageButton) findViewById(R.id.imageButton2);
		// set a OnClickListener to the ImageButton
		doneImgBtn.setOnClickListener(this);

		// get an ImageButton for recording voice
		final ImageButton recordImgBtn = (ImageButton) findViewById(R.id.imageButton3);
		// set a OnClickListener to the ImageButton
		recordImgBtn.setOnClickListener(this);		
		
		// =================== Calendar Dialog / Text ===================
		// get a TextView for showing date
		final TextView dateTextView = (TextView) findViewById(R.id.textView4);
		// set a OnClickListener to the TextView
		dateTextView.setOnClickListener(this);

		// get data for the TextView
		final Calendar calendar = Calendar.getInstance();
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);

		// set date text on the TextView
		dateTextView.setText(year + "/" + (month + 1) + "/" + day);

		// =================== Calendar Dialog / Text ===================
		// get a TextView for showing hours
		final TextView timeTextView = (TextView) findViewById(R.id.textView5);
		// set a OnClickListener to the TextView
		timeTextView.setOnClickListener(this);

		// get data for the TextView
		hour = calendar.get(Calendar.HOUR_OF_DAY);
		minute = calendar.get(Calendar.MINUTE);

		// set date text on the TextView
		timeTextView.setText(hour + ":" + minute);
	}

	// ----------------------------------------------------------
	// /////////////// onOptionsItemSelected() //////////////////
	// ----------------------------------------------------------
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) { // if�g���ƃG���[�iitem��Int�`���Ȃ��߁j
		case android.R.id.home: // �A�v���A�C�R���i�z�[���A�C�R���j�����������̏���
			Intent intent = new Intent(this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		}
		return true;
	}

	// ----------------------------------------------------------
	// /////////////////////// onClick() ////////////////////////
	// ----------------------------------------------------------
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.imageButton3:
			// recorder = new MediaRecorder();
			// recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			// recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
			// recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
			//
			// //�ۑ���
			// String filePath = Environment.getExternalStorageDirectory() +
			// "/audio.3gp";
			// recorder.setOutputFile(filePath);
			//
			// Log.d("test", "Hello");
			//
			// //�^���������^���J�n
			// try {
			// recorder.prepare();
			// } catch (Exception e) {
			// e.printStackTrace();
			// }
			// recorder.start(); //�^���J�n
			break;
		case R.id.imageButton2:
			
			Calendar temp_cal = Calendar.getInstance();
			temp_cal.set(year, month, day, hour, minute);
			long unixTime = temp_cal.getTimeInMillis() / 1000;
			//Log.d("UnixTime", "UT =" + unixTime);
			
			// Input Data into RecordItem Object
			Record = new RecordItem();
			Record.setItemTitle(titleOfVoice.getText().toString());
			Record.setItemAlarmTime(unixTime);
			
			Log.d("Record", "Title: " + titleOfVoice.getText().toString());
			Log.d("Record", "AlarmTime: " + unixTime);
			
			/* �f�[�^�擾�^�X�N�̎��s */
			DataLoadTask task = new DataLoadTask();
			task.execute();

			// // create an instance of Intent
			// Intent intent = new Intent(AddVoiceActivity.this,
			// MainActivity.class);
			// // go to next screen
			// startActivity(intent);
			break;
		case R.id.textView4:
			showDatePickerDialog();
			break;
		case R.id.textView5:
			showTimePickerDialog();
			break;
		}
	}

	// ----------------------------------------------------------
	// ///////////////// showDatePickerDialog() /////////////////
	// ----------------------------------------------------------
	private void showDatePickerDialog() {
		final TextView dateTextView = (TextView) findViewById(R.id.textView4);
		// generate a DatePickerDialog
		final DatePickerDialog dialog = new DatePickerDialog(
				AddVoiceActivity.this,
				new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker picker, int yearSet,
							int monthSet, int daySet) {
						Log.v("Date", yearSet + "/" + (monthSet + 1) + "/"
								+ daySet);
						dateTextView.setText(yearSet + "/" + (monthSet + 1)
								+ "/" + daySet);
						year = yearSet;
						month = monthSet;
						day = daySet;
					}
				}, year, month, day);
		dialog.show();
	}

	// ----------------------------------------------------------
	// ///////////////// showTimePickerDialog() /////////////////
	// ----------------------------------------------------------
	private void showTimePickerDialog() {
		final TextView timeTextView = (TextView) findViewById(R.id.textView5);
		// generate a TimePickerDialog
		final TimePickerDialog dialog = new TimePickerDialog(
				AddVoiceActivity.this,
				new TimePickerDialog.OnTimeSetListener() {
					@Override
					public void onTimeSet(TimePicker tPicker, int hourOfDaySet,
							int minuteSet) {
						Log.v("Hour", hourOfDaySet + ":" + (minuteSet));
						timeTextView.setText(hourOfDaySet + ":" + (minuteSet));
						hour = hourOfDaySet;
						minute = minuteSet;
					}
				}, hour, minute, true);
		dialog.show();
	}

	// ----------------------------------------------------------
	// ////////////////////// DataLoadTask //////////////////////
	// ----------------------------------------------------------
	public class DataLoadTask extends AsyncTask<Object, Integer, RecordItem> {
		/* �������_�C�A���O */
		private ProgressDialog progressDialog = null;

		@Override
		protected void onPreExecute() {
			/* �������_�C�A���O�\�� */
			progressDialog = new ProgressDialog(AddVoiceActivity.this);
			progressDialog.setMessage(getResources().getText(R.string.loading));
			progressDialog.setIndeterminate(true);
			progressDialog.show();
		}

		@Override
		protected RecordItem doInBackground(Object... params) {
			/* �ۑ������s */
			com.s5884.youremindyoudb.RecordDaoItem dao = new RecordDaoItem(
					AddVoiceActivity.this);

			return dao.save_item(Record);
		}

		protected void onPostExecute(RecordItem result) {
			/* �������_�C�A���O���N���[�Y */
			progressDialog.dismiss();

			//clearKeyboard();
			/* �ۑ������\�� */
			DispToast(null);
			/* �ǉ������I�� */
			finish();
		}
	}

	// ----------------------------------------------------------
	// ///////////////////////// Toast //////////////////////////
	// ----------------------------------------------------------
	/* �ۑ������\�� */
	public void DispToast(View v) {
		Toast.makeText(this, R.string.add_ok, Toast.LENGTH_SHORT).show();
	}
}