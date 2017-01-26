package net.media.silk;

import java.util.List;

import net.media.silk.dbHelper.DatabaseHelper;
import net.media.silk.model.Passcode;
import net.media.silk.model.User;
import net.media.silk.parameters.Parameters;
import net.media.silk.session.SessionManager;
import net.media.silk.util.Date;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Window;

public class Splash extends Activity {
	// Database Helper
	DatabaseHelper db;

	private long ms = 0;
	private long splashTime = 1000;
	private boolean splashActive = true;
	private boolean paused = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		db = new DatabaseHelper(getApplicationContext());

		// Hides the titlebar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		if (Build.VERSION.SDK_INT <= 17) {
			Settings.System.putInt(getContentResolver(),
					Settings.System.AUTO_TIME, 1);
		}
		setContentView(R.layout.splace);
		Thread mythread = new Thread() {
			public void run() {
				try {
					while (splashActive && ms < splashTime) {
						if (!paused)
							ms = ms + 100;
						sleep(100);
					}
				} catch (Exception e) {
				} finally {
					List<User> users = db.getUser();
					Intent intent;
					if (users.isEmpty()) {
						intent = new Intent(Splash.this, Signup.class);
					} else {
						SessionManager manager = new SessionManager(
								getApplicationContext());
						DatabaseHelper helper = new DatabaseHelper(
								getApplicationContext());
						List<Passcode> passcodesList = helper.getPasscode();
						for (int i = 0; i < passcodesList.size(); i++) {
							Passcode passcode = passcodesList.get(i);
							if (Date.getCurrentDate()
									.compareTo(
											Date.getNHourAfterDate(
													passcode.getUpdated(),
													Parameters.PASSCODEACTIVATIONDURATION)) > 0) {
								if (!passcode.isActive()) {
									passcode.setActive(true);
									helper.updatePasscode(passcode);
									helper.updateConference(passcode.getPid());
								}
							}
						}
						if (manager.isLoggedIn()) {
							intent = new Intent(Splash.this, Home.class);
						} else {
							intent = new Intent(Splash.this, Login.class);
						}
					}
					startActivity(intent
							.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
									| Intent.FLAG_ACTIVITY_NEW_TASK));
					finish();
				}
			}
		};
		mythread.start();
	}

	@Override
	public void onBackPressed() {

	}

}