package net.media.silk;

import java.util.HashMap;

import net.media.silk.dbHelper.DatabaseHelper;
import net.media.silk.dialog.Dialog;
import net.media.silk.model.User;
import net.media.silk.session.SessionManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class ChangePasswordFragment extends Fragment {

	public ChangePasswordFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.changepassword, container,
				false);

		final EditText oldpwd = (EditText) rootView.findViewById(R.id.oldpwd);
		final EditText newpwd = (EditText) rootView.findViewById(R.id.newpwd);
		final TextView submit = (TextView) rootView.findViewById(R.id.submit);

		submit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String oldpwdValue = oldpwd.getText().toString();
				String newpwdValue = newpwd.getText().toString();
				SessionManager manager = new SessionManager(getActivity());

				HashMap<String, String> userHashMap = manager.getUserDetails();
				if (userHashMap.get(SessionManager.KEY_PASSWORD).equals(
						oldpwdValue)
						&& newpwdValue.length() > 0) {
					User user = new User();
					user.setUid(Integer.parseInt(userHashMap
							.get(SessionManager.KEY_ID)));
					user.setPassword(newpwdValue);
					DatabaseHelper helper = new DatabaseHelper(getActivity());
					helper.changePassword(user);
					oldpwd.setText("");
					newpwd.setText("");
					Dialog.correctDialog(getActivity(), "Password",
							"Password Updated!");
				} else {
					Dialog.wrongDialog(getActivity(), "Password",
							"Please Enter Password!");
				}
			}
		});
		return rootView;
	}
}