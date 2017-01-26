package net.media.silk.dialog;

import net.media.silk.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class Dialog {
	public static void correctDialog(Context context, String title,
			String message) {
		AlertDialog dialog = new AlertDialog.Builder(context).create();
		dialog.setTitle(title);

		// Setting Dialog Message
		dialog.setMessage(message);

		// Setting Icon to Dialog
		dialog.setIcon(R.drawable.correct);

		// Setting OK Button
		dialog.setButton("OK", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		// Showing Alert Message
		dialog.show();
	}

	public static void wrongDialog(Context context, String title, String message) {
		AlertDialog dialog = new AlertDialog.Builder(context).create();
		dialog.setTitle(title);

		// Setting Dialog Message
		dialog.setMessage(message);

		// Setting Icon to Dialog
		dialog.setIcon(R.drawable.wrong);

		// Setting OK Button
		dialog.setButton("OK", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		// Showing Alert Message
		dialog.show();
	}
}
