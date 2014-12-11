package ch.ethz.inf.vs.android.glukas.project4;

import ch.ethz.inf.vs.android.glukas.project4.database.DatabaseAccess;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.Toast;

//Fragment layout for the registration dialog
public class RegistrationDialogFragment extends DialogFragment {

	public interface RegistrationDialogFragmentDelegate {
		void onUserRegistered(String username);
	}

	private RegistrationDialogFragmentDelegate delegate;

	RegistrationDialogFragment(RegistrationDialogFragmentDelegate delegate) {
		this.delegate = delegate;
		
	}
	
	@Override
	public AlertDialog onCreateDialog(Bundle savedInstanceState) {
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		// Get the layout inflater
		LayoutInflater inflater = getActivity().getLayoutInflater();

		// Inflate and set the layout for the dialog
		// Pass null as the parent view because its going in the dialog layout
		//	        builder.setView(inflater.inflate(R.layout.dialog_registration, null));
		final EditText input = new EditText(getActivity());
		builder.setView(input);

		// Set dialog title
		builder.setTitle(R.string.registration_title);

		// Set positive button
		builder.setPositiveButton(R.string.register_button, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// Retrieve data from textboxes, create new User and call putUser
				String username = input.getEditableText().toString(); // TODO: check for null
				Toast.makeText(getActivity(), username + " was succesfully registered", Toast.LENGTH_LONG).show();

				delegate.onUserRegistered(username);
			}
		});
		builder.setCancelable(false);
		// Set negative button
		/*builder.setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// TODO: User cancelled registration dialog, what should be done?
			}
		});*/

		// Create the AlertDialog object and return it
		return builder.create();
	}
}
