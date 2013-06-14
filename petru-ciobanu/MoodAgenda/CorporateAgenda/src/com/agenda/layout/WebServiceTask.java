package com.agenda.layout;

import android.app.ProgressDialog;
import android.os.AsyncTask;


class WebServiceTask extends AsyncTask<Void, Void, Void> {
	/**
	 * 
	 */
	private final LoginActivity loginWebTask;

	/**
	 * @param loginActivity
	 */
	WebServiceTask(LoginActivity loginActivity) {
		loginWebTask = loginActivity;
	}

	private ProgressDialog dialog;

	protected void onPreExecute() {
		dialog = new ProgressDialog(loginWebTask);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setCancelable(true);
		dialog.setMessage("Vérifier le compte");
		dialog.show();
	}

	protected Void doInBackground(Void... params) {
		try {
			if (loginWebTask.txtUsername.getText().toString().length() < 5 || loginWebTask.txtPassword.getText().toString().length() < 3) {
				new ToastMessageTask(loginWebTask).execute("S'il vous plaît remplir tous les champs!");
			} else {
				loginWebTask.doLogin(loginWebTask.txtUsername.getText().toString(), loginWebTask.txtPassword.getText().toString());
			}
		} catch (Exception e) {
			dialog.cancel();
			new ToastMessageTask(loginWebTask).execute("S'il vous plaît réessayer plus tard!" + e);
		}
		return null;
	}

	protected void onPostExecute(Void dResult) {
		dialog.cancel();
	}

}