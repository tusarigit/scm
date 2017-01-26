package net.media.silk;

import java.util.List;

import net.media.silk.dbHelper.DatabaseHelper;
import net.media.silk.dialog.Dialog;
import net.media.silk.model.Accessno;
import net.media.silk.model.Conference;
import net.media.silk.model.Passcode;
import net.media.silk.parameters.Parameters;
import net.media.silk.util.Date;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class AddPasscodeFragment extends Fragment {
	public AddPasscodeFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater
				.inflate(R.layout.addpasscode, container, false);

		final EditText confnameEditText = (EditText) rootView
				.findViewById(R.id.confname);
		final EditText accessnoEditText = (EditText) rootView
				.findViewById(R.id.accessno);
		final EditText passcodeEditText = (EditText) rootView
				.findViewById(R.id.passcode);
		TextView submit = (TextView) rootView.findViewById(R.id.submit);
		submit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String confnameValue = confnameEditText.getText().toString();
				String accessnoValue = accessnoEditText.getText().toString();
				String passcodeValue = passcodeEditText.getText().toString();
				DatabaseHelper helper = new DatabaseHelper(getActivity());

				if (confnameValue.length() == 0 || accessnoValue.length() == 0
						|| passcodeValue.length() == 0) {
					Dialog.wrongDialog(getActivity(), "Edit Passcode",
							"Please enter all felds!");
				} else {
					if (helper.isConference(confnameValue)) {
						Dialog.wrongDialog(getActivity(), "Edit Passcode",
								"Conference name already exist!");
					} else {
						Conference conference = new Conference();
						Accessno accessno = new Accessno();
						Passcode passcode = new Passcode();

						conference.setConfname(confnameValue);
						accessno.setAccessno(accessnoValue);
						accessno.setActive(true);
						passcode.setPasscode(passcodeValue);

						int passcodecount = helper.getPasscodeCount();
						boolean isPasscode = helper.isPasscode(passcodeValue);
						boolean isAccessno = helper.isAccessno(accessnoValue);
						if (passcodecount >= Parameters.PASSCODECOUNT
								&& !isPasscode) {
							Dialog.wrongDialog(getActivity(), "Edit Passcode",
									"Only " + Parameters.PASSCODECOUNT
											+ " passcode you can add!");
						} else {
							int passcodeID = -1;
							int accessnoID = -1;
							if (passcodecount < 2) {
								accessnoID = (int) helper
										.insertAccessno(accessno);
								passcode.setActive(true);
								passcodeID = (int) helper
										.insertPasscode(passcode);
								conference.setActive(true);
							} else {
								if (isAccessno) {
									accessno = helper
											.getAccessno(accessnoValue);
									accessnoID = accessno.getAid();
								} else {
									accessnoID = (int) helper
											.insertAccessno(accessno);
								}
								if (isPasscode) {
									passcode = helper
											.getPasscode(passcodeValue);
									passcodeID = passcode.getPid();
									conference.setActive(passcode.isActive());
								} else {
									List<Passcode> passcodesList = helper
											.getPasscode();
									boolean flag = true;
									for (int i = 0; i < passcodesList.size(); i++) {
										Passcode passcode1 = passcodesList
												.get(i);
										if (Date.getCurrentDate()
												.compareTo(
														Date.getNHourAfterDate(
																passcode1
																		.getUpdated(),
																Parameters.PASSCODEACTIVATIONDURATION)) < 0) {
											flag = false;
											break;
										}
									}
									conference.setActive(flag);
									passcode.setActive(flag);
									passcodeID = (int) helper
											.insertPasscode(passcode);
								}
							}

							if (accessnoID != -1) {
								if (passcodeID != -1) {
									conference.setAid(accessnoID);
									conference.setPid(passcodeID);

									int confID = (int) helper
											.insertConference(conference);
									if (confID != -1) {
										Dialog.correctDialog(getActivity(),
												"Edit Passcode", "Data saved!");
									} else {
										helper.deleteAccessno(accessnoID);
										helper.deletePasscode(passcodeID);
										Dialog.wrongDialog(getActivity(),
												"Edit Passcode",
												"Please try again!");
									}
								} else {
									helper.deleteAccessno(accessnoID);
									Dialog.wrongDialog(getActivity(),
											"Edit Passcode",
											"Please try again!");
								}
							} else {
								Dialog.wrongDialog(getActivity(),
										"Edit Passcode", "Please try again!");
							}
						}
					}
				}
				accessnoEditText.setText("");
				passcodeEditText.setText("");
				confnameEditText.setText("");
			}
		});
		return rootView;
	}
}