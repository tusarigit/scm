package net.media.silk;

import net.media.silk.dbHelper.DatabaseHelper;
import net.media.silk.dialog.Dialog;
import net.media.silk.model.User;
import net.media.silk.session.SessionManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// Hides the titlebar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);

		final EditText name = (EditText) findViewById(R.id.name);
		final EditText pwd = (EditText) findViewById(R.id.pwd);
		final TextView login = (TextView) findViewById(R.id.login);

		login.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String nameValue = name.getText().toString();
				String pwdValue = pwd.getText().toString();
				DatabaseHelper helper = new DatabaseHelper(
						getApplicationContext());
				User user = helper.getUser(nameValue, pwdValue);
				if (user != null && user.getName().equals(nameValue)
						&& user.getPassword().equals(pwdValue)) {
					SessionManager manager = new SessionManager(
							getApplicationContext());
					manager.createLoginSession(nameValue, pwdValue,
							user.getUid());
					startActivity(new Intent(Login.this, Home.class)
							.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
									| Intent.FLAG_ACTIVITY_NEW_TASK));
					finish();
				} else {
					name.setText("");
					pwd.setText("");
					Dialog.wrongDialog(Login.this, "Login",
							"User Name & Password is Wrong!");
				}
			}
		});
	}
}
