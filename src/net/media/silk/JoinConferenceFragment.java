package net.media.silk;

import java.util.ArrayList;
import java.util.List;

import net.media.silk.dbHelper.DatabaseHelper;
import net.media.silk.dialog.Dialog;
import net.media.silk.model.Accessno;
import net.media.silk.model.Conference;
import net.media.silk.model.Passcode;
import net.media.silk.session.SessionManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class JoinConferenceFragment extends Fragment implements
		OnItemSelectedListener {
	private Spinner conferenceSpinner;
	private EditText accessnoEditText;
	private EditText passcodeEditText;

	private static ArrayAdapter<String> conferenceArrayAdapter;
	private List<Conference> conferencesList;
	private DatabaseHelper helper;
	private static List<String> sConference;

	public JoinConferenceFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.joinconference, container,
				false);
		TextView joinnow = (TextView) rootView.findViewById(R.id.joinnow);
		conferenceSpinner = (Spinner) rootView
				.findViewById(R.id.conferenceSpinner);
		accessnoEditText = (EditText) rootView.findViewById(R.id.accessno);
		passcodeEditText = (EditText) rootView.findViewById(R.id.passcode);
		helper = new DatabaseHelper(getActivity());
		conferencesList = helper.getConference();
		sConference = new ArrayList<String>();
		sConference.add("Select Conference Name");
		for (int i = 0; i < conferencesList.size(); i++) {
			sConference.add(conferencesList.get(i).getConfname());
		}

		conferenceArrayAdapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, sConference);

		conferenceSpinner.setAdapter(conferenceArrayAdapter);
		conferenceSpinner.setOnItemSelectedListener(this);

		joinnow.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String accessnoValue = accessnoEditText.getText().toString();
				String passcodeValue = passcodeEditText.getText().toString();
				helper = new DatabaseHelper(getActivity());

				if (accessnoValue.length() == 0 || passcodeValue.length() == 0) {
					Dialog.wrongDialog(getActivity(), "Conference",
							"Please Select Conference Name!");
				} else {
					SessionManager manager = new SessionManager(getActivity());
					manager.logoutUser();
					Intent callIntent = new Intent(Intent.ACTION_CALL)
							.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
									| Intent.FLAG_ACTIVITY_NEW_TASK);
					String phone = "tel:" + accessnoValue + ",,"
							+ passcodeValue;
					callIntent.setData(Uri.parse(phone));
					startActivity(callIntent);
					getActivity().finish();
				}
			}
		});

		return rootView;
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		if (arg2 > 0) {
			Conference conference = conferencesList.get(arg2 - 1);
			Accessno accessno = helper.getAccessno(conference.getAid());
			Passcode passcode = helper.getPasscode(conference.getPid());
			accessnoEditText.setText(accessno.getAccessno());
			passcodeEditText.setText(passcode.getPasscode());
		} else {
			accessnoEditText.setText("");
			passcodeEditText.setText("");
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}
}
