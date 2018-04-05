package com.dariapro.socialevent;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class EventFragment extends Fragment {

    private static final String ARG_EVENT_ID = "com.dariapro.socialevent.event_id";
    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_DATE = 0;
    private static final String DIALOG_TIME = "DialogTime";
    private static final int REQUEST_TIME = 0;

    private Event mEvent;
    private EditText mTitleField;
    private Button mDateButton;
    private Button mTimeButton;
    private CheckBox mSolvedCheckBox;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mEvent = new Event();

        UUID eventId = (UUID) getArguments().getSerializable(EventPagerActivity.EXTRA_EVENT_ID);
        mEvent = EventLab.get(getActivity()).getEvent(eventId);

    }

    @Override public void onPause() {
        super.onPause();
        EventLab.get(getActivity()).updateEvent(mEvent);
    }
        @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_event, container, false);

        mTitleField = v.findViewById(R.id.event_title);
        mTitleField.setText(mEvent.getTitle());

        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence c, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence c, int start, int before, int count) {
                mEvent.setTitle(c.toString());
            }

            @Override
            public void afterTextChanged(Editable c) {

            }
        });


        mDateButton = v.findViewById(R.id.event_date);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                DatePickerFragment datePickerFragment = DatePickerFragment
                        .newInstance(mEvent.getDate());
                datePickerFragment.setTargetFragment(EventFragment.this, REQUEST_DATE);
                datePickerFragment.show(fragmentManager, DIALOG_DATE);
            }
        });

//        mTimeButton = v.findViewById(R.id.event_time);
//        //DateFormat df = DateFormat.getDateInstance(DateFormat.LONG, Locale.ENGLISH);
//        updateDate();
//        mTimeButton.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                FragmentManager fragmentManager = getFragmentManager();
//                TimePickerFragment timePickerFragment = TimePickerFragment
//                        .newInstance(mEvent.getEventTime());
//                timePickerFragment.setTargetFragment(EventFragment.this, REQUEST_TIME);
//                timePickerFragment.show(fragmentManager, DIALOG_TIME);
//            }
//        });

        mSolvedCheckBox = v.findViewById(R.id.event_solved);
        mSolvedCheckBox.setChecked(mEvent.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mEvent.setSolved(isChecked);
            }
        });

        return v;
    }

    public static EventFragment newInstance(UUID eventID){
        Bundle args = new Bundle();
        args.putSerializable(ARG_EVENT_ID, eventID);

        EventFragment eventFragment = new EventFragment();
        eventFragment.setArguments(args);
        return eventFragment;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != Activity.RESULT_OK){
            return;
        }

        if(requestCode == REQUEST_DATE){
            Date date = (Date) data
                    .getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mEvent.setDate(date);
            updateDate();
        }
    }

    private void updateDate() {
        mDateButton.setText(mEvent.getDate().toString());
    }

    public void returnResult(){
        getActivity().setResult(Activity.RESULT_OK,null);
    }
}
