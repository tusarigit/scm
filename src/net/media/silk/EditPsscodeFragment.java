package net.media.silk;

import java.util.ArrayList;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class EditPsscodeFragment extends Fragment implements
		OnItemSelectedListener {	
	private Spinner conferenceSpinner;
	private EditText confnameEditText;
	private EditText accessnoEditText;
	private EditText passcodeEditText;

	private static ArrayAdapter<String> conferenceArrayAdapter;
	private List<Conference> conferencesList;
	private DatabaseHelper helper;
	private Conference conference;
	private Accessno accessno;
	private Passcode passcode;
	private static List<String> sConference;

	public EditPsscodeFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.editpasscode, container,
				false);
		confnameEditText = (EditText) rootView.findViewById(R.id.confname);
		accessnoEditText = (EditText) rootView.findViewById(R.id.accessno);
		passcodeEditText = (EditText) rootView.findViewById(R.id.passcode);

		TextView submit = (TextView) rootView.findViewById(R.id.submit);
		conferenceSpinner = (Spinner) rootView
				.findViewById(R.id.conferenceSpinner);
		sConference = new ArrayList<String>();
		helper = new DatabaseHelper(getActivity());
		update();
		conferenceArrayAdapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, sConference);

		conferenceSpinner.setAdapter(conferenceArrayAdapter);
		conferenceSpinner.setOnItemSelectedListener(this);

		submit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String confnameValue = confnameEditText.getText().toString();
				String accessnoValue = accessnoEditText.getText().toString();
				String passcodeValue = passcodeEditText.getText().toString();
				if (confnameValue.length() == 0 || accessnoValue.length() == 0
						|| passcodeValue.length() == 0) {
					Dialog.wrongDialog(getActivity(), "Edit Passcode",
							"Please Select Conference Name!");
				} else if (conference != null) {
					boolean flagConference = true;
					if (!confnameValue.equalsIgnoreCase(conference
							.getConfname())) {
						if (helper.isConference(confnameValue)) {
							flagConference = false;
						}
					}
					if (flagConference) {
						boolean flagPasscode = true;
						Passcode passcode1 = helper.getPasscode(conference
								.getPid());
						if (!passcodeValue.equalsIgnoreCase(passcode1
								.getPasscode())) {
							String upadteDate = passcode1.getUpdated();
							if (Date.getCurrentDate()
									.compareTo(
											Date.getNHourAfterDate(
													upadteDate,
													Parameters.PASSCODEACTIVATIONDURATION)) < 0) {
								flagPasscode = false;
							}
						}
						boolean isPasscode = helper.isPasscode(passcodeValue);
						boolean isAccessno = helper.isAccessno(accessnoValue);
						if (flagPasscode) {
							conference.setConfname(confnameValue);
							conference.setActive(true);
							if (isAccessno) {
								conference.setAid(accessno.getAid());
							} else {
								accessno.setAid(conference.getAid());
								accessno.setActive(true);
								accessno.setAccessno(accessnoValue);
								helper.updateAccessno(accessno);
							}
							if (isPasscode) {
								conference.setPid(passcode.getPid());
								conference.setActive(passcode.isActive());
							} else {
								passcode.setPid(conference.getPid());
								passcode.setActive(true);
								passcode.setPasscode(passcodeValue);
								helper.updatePasscode(passcode);
							}
							helper.updateConference(conference);

							update();
							conferenceArrayAdapter.notifyDataSetChanged();
							conferenceSpinner.setSelection(0, true);
							Dialog.correctDialog(getActivity(),
									"Edit Passcode", "Data Updated!");
						} else {
							Dialog.wrongDialog(getActivity(), "Edit Passcode",
									"You are not able to edit this conference!");
						}
					} else {
						Dialog.wrongDialog(getActivity(), "Edit Passcode",
								"Conference name already exist!");
					}
				}
			}
		});
		return rootView;
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		conference = null;
		if (arg2 > 0) {
			conference = conferencesList.get(arg2 - 1);
			accessno = helper.getAccessno(conference.getAid());
			passcode = helper.getPasscode(conference.getPid());
			confnameEditText.setText(conference.getConfname());
			accessnoEditText.setText(accessno.getAccessno());
			passcodeEditText.setText(passcode.getPasscode());
		} else {
			confnameEditText.setText("");
			accessnoEditText.setText("");
			passcodeEditText.setText("");
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
	}

	public void update() {
		sConference.clear();
		conferencesList = helper.getConference();
		sConference.add("Select Conference Name");
		for (int i = 0; i < conferencesList.size(); i++) {
			sConference.add(conferencesList.get(i).getConfname());
		}
	}
}
