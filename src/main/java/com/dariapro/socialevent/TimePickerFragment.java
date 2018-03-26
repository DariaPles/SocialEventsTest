package com.dariapro.socialevent;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TimePickerFragment extends DialogFragment {

    private static final String DIALOG_TIME = "DialogTime";
    public static final String EXTRA_TIME = "time";

    @SuppressLint("NewApi")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final EventTime eventTime = (EventTime) getArguments().getSerializable(DIALOG_TIME);

        int hour = eventTime.getHour();
        int minute = eventTime.getMinute();

        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_time,null);

        final TimePicker timePicker = v.findViewById(R.id.dialog_time);
        timePicker.setHour(hour);
        timePicker.setMinute(minute);


        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.date_picker_title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int hour = timePicker.getHour();
                        int minute = timePicker.getMinute();

                        //Date date = new Date();
                        EventTime newEventTime = new EventTime();
                        newEventTime.setHour(hour);
                        newEventTime.setMinute(minute);
                        sendResult(Activity.RESULT_OK, newEventTime);
                    }
                })
                .create();
    }

    private void sendResult(int resultCode, EventTime newEventTime){
        if(getTargetFragment() == null){
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_TIME, (Serializable) newEventTime);

        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

    public static TimePickerFragment newInstance(EventTime newEventTime){
        Bundle args = new Bundle();
        args.putSerializable(DIALOG_TIME, (Serializable) newEventTime);

        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
