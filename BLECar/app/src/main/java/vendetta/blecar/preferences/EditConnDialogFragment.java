package vendetta.blecar.preferences;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import vendetta.blecar.R;
import vendetta.blecar.connection.ConnectionConfig;

public class EditConnDialogFragment extends DialogFragment {

    private IConnEditable mListener;
    private EditText connectionNameET, connectionValueET;
    private TextView connectionIdEditTV;

    public interface IConnEditable {
        void onSaveButtonPress(ConnectionConfig connectionConfig);

        ConnectionConfig getActiveSelection();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (IConnEditable) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(getActivity().toString() + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_connection_edit, null);
        connectionNameET = v.findViewById(R.id.connectionNameET);
        connectionValueET = v.findViewById(R.id.connectionValueET);
        connectionIdEditTV = v.findViewById(R.id.connectionIdEditTV);
        connectionNameET.setText(mListener.getActiveSelection().getName());
        connectionValueET.setText(mListener.getActiveSelection().getIdentifier());
        connectionIdEditTV.setText(mListener.getActiveSelection().getConnType().getSpecific());
        builder.setView(v)
//                .setMessage()
                .setTitle("Editing "+mListener.getActiveSelection().getName())
                .setIcon(android.R.drawable.ic_menu_edit)
                .setPositiveButton("Save", (dialog, id) -> {
                    mListener.onSaveButtonPress(new ConnectionConfig(connectionNameET.getText().toString(), mListener.getActiveSelection().getConnType(), connectionValueET.getText().toString()));
                })
                .setNegativeButton("cancel", (dialog, id) -> {
                    // User cancelled the dialog
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
