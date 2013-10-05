package me.arthurtucker.jarvisjr;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by Arthur on 7/24/13.
 * Prove HER wrong.
 */
public class ResetDialog extends DialogFragment {
    // Use this instance of the interface to deliver action events
    private ResetDialogListener mListener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        String mException = "ERROR"; //getResources().getString(R.string.unlock_dialog_exception);

        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (ResetDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " " + mException);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TheActivity mActivity = new TheActivity();

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);

        // Strings

        builder.setMessage("You'll loose all your changes!")
                .setTitle("Reset?");
        builder.setPositiveButton("Erase", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                mListener.onDialogPositiveClick(ResetDialog.this);
            }
        });
        builder.setNegativeButton("Cancel", null);
        AlertDialog dialog = builder.create();
        dialog.show();
        return dialog;
    }

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface ResetDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);

        //public void onDialogNegativeClick(DialogFragment dialog);
    }

}
