package com.agenda.layout;

import android.os.AsyncTask;
import android.widget.Toast;

class ToastMessageTask extends AsyncTask<String, String, String> {
	/**
	 * 
	 */
	private final LoginActivity loginActivity;

	/**
	 * @param loginActivity
	 */
	ToastMessageTask(LoginActivity loginActivity) {
		this.loginActivity = loginActivity;
	}

	String toastMessage;

	@Override
	protected String doInBackground(String... params) {
		toastMessage = params[0];
		return toastMessage;
	}

	protected void onPostExecute(String result) {
		Toast toast = Toast.makeText(this.loginActivity.getApplicationContext(), result, Toast.LENGTH_LONG);
		toast.show();
	}
}