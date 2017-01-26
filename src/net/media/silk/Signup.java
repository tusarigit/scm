package net.media.silk;

import net.media.silk.dbHelper.DatabaseHelper;
import net.media.silk.dialog.Dialog;
import net.media.silk.model.User;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

public class Signup extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// Hides the titlebar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.signup);

		final EditText name = (EditText) findViewById(R.id.name);
		final EditText pwd = (EditText) findViewById(R.id.pwd);
		final EditText cnfrmpwd = (EditText) findViewById(R.id.cnfrmpwd);
		final TextView signup = (TextView) findViewById(R.id.signup);
		
		signup.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				String nameValue = name.getText().toString();
				String pwdValue = pwd.getText().toString();
				String cnfrmpwdValue = cnfrmpwd.getText().toString();
				if (pwdValue.equals(cnfrmpwdValue) && nameValue != "") {
					User user = new User();					
					user.setName(nameValue);
					user.setPassword(pwdValue);
					DatabaseHelper helper = new DatabaseHelper(
							getApplicationContext());						
					if (helper.insertUser(user)== -1) {
						Dialog.wrongDialog(Signup.this, "Signup",
								"Please Try Again!");
					} else {
						startActivity(new Intent(Signup.this, Login.class)
						.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
								| Intent.FLAG_ACTIVITY_NEW_TASK));
						finish();
					}

				} else {
					Dialog.wrongDialog(Signup.this, "Signup",
							"Please check Credentials!");
				}
			}
		});
	}
}
