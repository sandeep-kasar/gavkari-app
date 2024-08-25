package com.gavkariapp.helper;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

import com.gavkariapp.R;

import java.util.Calendar;

public class DatePicker extends DialogFragment {

    DatePickerDialog.OnDateSetListener ondateSet;
    private int year, month, day;

    public void setCallBack(DatePickerDialog.OnDateSetListener ondate) {
        ondateSet = ondate;
    }

    @SuppressLint("NewApi")
    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        year = args.getInt("year");
        month = args.getInt("month");
        day = args.getInt("day");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //create date picker instance
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), R.style.DatePickerDialogTheme, ondateSet, year, month, day);

        //hide past date
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());


        // Create a new instance of DatePickerDialog and return it
        return datePickerDialog;

    }


}
