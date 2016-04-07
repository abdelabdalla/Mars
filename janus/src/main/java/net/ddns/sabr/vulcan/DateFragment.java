package net.ddns.sabr.vulcan;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;

/*
*Copyright 2016 Abdel-Rahim Abdalla
*/
public class DateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    public DateFragment() {}

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        String date = dayOfMonth + "/" + monthOfYear + "/" + year;

        ((MainActivity)getActivity()).session.date = date;
        ((MainActivity)getActivity()).dateButton.setText(date);
        //dismiss();
    }

}
