package me.arthurtucker.jarvisjr;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;

import me.arthurtucker.jarvisjr.utils.Global;
import me.arthurtucker.jarvisjr.utils.Prefs;

/**
 * Created by Arthur on 7/24/13.
 * Prove HER wrong.
 */
public class StartQuietDialog extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int hour;
        int minute;
        if (Global.isQTStartEnabled) {
            hour    = Global.startQTHour;
            minute  = Global.startQTMin;
        } else {
            // Use the current time as the default values for the picker
            final Calendar c    = Calendar.getInstance();
            hour                = c.get(Calendar.HOUR_OF_DAY);
            minute              = c.get(Calendar.MINUTE);
        }

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(TimePicker view, int hour, int minute) {
        Global.isQTStartEnabled = true;
        Global.startQTHour      = hour;
        Global.startQTMin       = minute;
        TheFragment.updateStartSummary(getActivity());
        Prefs.pushPrefs();
    }

}
