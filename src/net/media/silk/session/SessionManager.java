package net.media.silk.session;

import java.util.HashMap;

import net.media.silk.Login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SessionManager {

	SharedPreferences pref;
	Editor editor;
	Context _context;
	int PRIVATE_MODE = 0;

	private static final String PREF_NAME = "scmPref";
	private static final String IS_LOGIN = "IsLoggedIn";
	public static final String KEY_ID = "uid";
	public static final String KEY_NAME = "name";
	public static final String KEY_PASSWORD = "password";

	@SuppressLint("CommitPrefEdits")
	public SessionManager(Context context) {
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}

	public void createLoginSession(String name, String password,int uid) {

		editor.putBoolean(IS_LOGIN, true);
		editor.putString(KEY_ID,Integer.toString(uid));
		editor.putString(KEY_NAME, name);
		editor.putString(KEY_PASSWORD, password);
		editor.commit();
	}

	public void checkLogin() {

		if (!this.isLoggedIn()) {

			Intent i = new Intent(_context, Login.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			_context.startActivity(i);
		}

	}

	public HashMap<String, String> getUserDetails() {
		HashMap<String, String> user = new HashMap<String, String>();
		user.put(KEY_ID, pref.getString(KEY_ID, null));
		user.put(KEY_NAME, pref.getString(KEY_NAME, null));
		user.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, null));
		return user;
	}

	public void logoutUser() {

		editor.clear();
		editor.commit();
	}

	public boolean isLoggedIn() {
		return pref.getBoolean(IS_LOGIN, false);
	}
}
