package com.agenda.layout;

import java.io.InputStream;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;


import com.agenda.utils.SharedData;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class LoginActivity extends Activity {
	protected SharedData sharedPrefs;
	private static final String SERVICE_URL = "http://192.168.26.102:8080/Agenda/login";
	// connection timeout, in milliseconds (waiting to connect)
	private static final int CONN_TIMEOUT = 3000;
	// socket timeout, in milliseconds (waiting for data)
	private static final int SOCKET_TIMEOUT = 5000;
	String userId;
	String userEmail;
	EditText txtUsername;
	EditText txtPassword;
	ImageButton btnLoginUser;
	String value;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.dock_right_enter, R.anim.hold);
		setContentView(R.layout.agenda_login);
		sharedPrefs = new SharedData(getApplicationContext());
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		txtUsername = (EditText) findViewById(R.id.inputUsername);
		txtPassword = (EditText) findViewById(R.id.inputPassword);

		try {
			Bundle bundle = this.getIntent().getExtras();
			value = bundle.getString("EventId").toString();
			if (sharedPrefs.getUserName().length() > 3 && value.length() >= 4) {
				Intent notification = new Intent(LoginActivity.this, NotificationView.class);
				startActivityIfNeeded(notification, 0);
			}
		} catch (Exception e) {
			if (sharedPrefs.getUserName().length() > 3) {
				Intent signIn = new Intent(LoginActivity.this, UserActivity.class);
				signIn.putExtra("idUser", sharedPrefs.getUserName());
				startActivityIfNeeded(signIn, 0);
			}
		}

		// USER LOGIN
		btnLoginUser = (ImageButton) findViewById(R.id.btnLogin);
		btnLoginUser.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				new WebServiceTask(LoginActivity.this).execute();
			}
		});

	}

	private void startActivity(Intent notification, Class<Activity> class1) {
		// TODO Auto-generated method stub

	}

	private void hideKeyboard() {
		InputMethodManager inputManager = (InputMethodManager) LoginActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.hideSoftInputFromWindow(LoginActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}

	// Establish connection and socket (data retrieval) timeouts
	private HttpParams getHttpParams() {
		HttpParams htpp = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(htpp, CONN_TIMEOUT);
		HttpConnectionParams.setSoTimeout(htpp, SOCKET_TIMEOUT);
		return htpp;
	}

	public String doLogin(String username, String password) {
		try {
			HttpClient httpclient = new DefaultHttpClient(getHttpParams());
			HttpPost httppost = new HttpPost(SERVICE_URL);
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("username", username));
			nameValuePairs.add(new BasicNameValuePair("password", password));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			InputStream is = entity.getContent();
			int ch;
			StringBuffer b = new StringBuffer();
			while ((ch = is.read()) != -1) {
				b.append((char) ch);
			}
			String s = b.toString();
			is.close();
			// PARSE JSON RESPONSE
			JSONObject jso = new JSONObject(s);
			new ToastMessageTask(this).execute(jso.getString("id_utilisateur").toString());
			if (jso.getString("id_utilisateur").toString().contains("null")) {
				new ToastMessageTask(this).execute("Aucun utilisateur trouvé");
			} else {
				sharedPrefs.setUserName(jso.getString("utilisateur_prenom"));
				sharedPrefs.setUserId(jso.getString("id_utilisateur"));
				Intent signIn = new Intent(LoginActivity.this, UserActivity.class);
				startActivityForResult(signIn, 0);
			}
		} catch (Exception e) {
			new ToastMessageTask(this).execute("S'il vous plaît réessayer plus tard!" + "\n" + e.getMessage());
		}
		return null;
	}

}
