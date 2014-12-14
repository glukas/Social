package ch.ethz.inf.vs.android.glukas.project4;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

//Fragment layout for the registration dialog
public class RegistrationDialogFragment extends DialogFragment {
	
	private EditText input;
	
	public interface RegistrationDialogFragmentDelegate {
		void onUserRegistered(String username);
	}

	private RegistrationDialogFragmentDelegate delegate;

	RegistrationDialogFragment(RegistrationDialogFragmentDelegate delegate) {
		this.delegate = delegate;
		this.setCancelable(false);
	}
	
	@SuppressLint("InflateParams") 
	@Override
	public AlertDialog onCreateDialog(Bundle savedInstanceState) {
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		// Get the layout inflater
		LayoutInflater inflater = getActivity().getLayoutInflater();

		// Pass null as the parent view because its going in the dialog layout
		final View dialogView = inflater.inflate(R.layout.dialog_registration, null);
		
		builder.setView(dialogView);

		input = (EditText)dialogView.findViewById(R.id.dialog_username);
		
		// Set dialog title
		builder.setTitle(R.string.registration_title);

		// Set positive button
		builder.setPositiveButton(R.string.register_button, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int id) {
				// Retrieve data from textboxes, create new User and call putUser
				String username = input.getEditableText().toString();
				
				delegate.onUserRegistered(username);
			}
		});

		// Create the AlertDialog object and return it
		return builder.create();
	}
}
