package com.dariapro.socialevent;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class EventFragment extends Fragment{

    private static final String ARG_EVENT_ID = "com.dariapro.socialevent.event_id";
    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_DATE = 0;
    private static final String DIALOG_TIME = "DialogTime";
    private static final int REQUEST_TIME = 1;
    private static final int REQUEST_PHOTO= 2;
    private static final int DOWNLOAD_PHOTO= 3;

    private Event mEvent;
    private File mPhotoFile;
    private EditText mTitleField;
    private Button mDateButton;
    private Button mTimeButton;
    private Button mSaveButton;
    private CheckBox mSolvedCheckBox;
    private ImageButton mCameraButton;
    private ImageButton mDownloadPicButton;
    private ImageView mEventPicture;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID eventId = (UUID) getArguments().getSerializable(EventPagerActivity.EXTRA_EVENT_ID);
        if(eventId != null) {
            mEvent = EventLab.get(getActivity()).getEvent(eventId);
            mPhotoFile = EventLab.get(getActivity()).getPhotoFile(mEvent);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu,menuInflater);
        menuInflater.inflate(R.menu.fragment_event, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_delete_event:
                EventLab.get(getActivity()).deleteEvent(mEvent);
                Intent intent = new Intent(getActivity(), EventListActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override public void onPause() {
        super.onPause();
        //EventLab.get(getActivity()).updateEvent(mEvent);
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

            mSaveButton = v.findViewById(R.id.event_save);
            mSaveButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    if(EventLab.get(getActivity()).getEvent(mEvent.getId()) != null){
                        EventLab.get(getActivity()).updateEvent(mEvent);
                    }
                    else {
                        EventLab.get(getActivity()).addEvent(mEvent);
                    }
                }
            });

        mSolvedCheckBox = v.findViewById(R.id.event_solved);
        mSolvedCheckBox.setChecked(mEvent.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mEvent.setSolved(isChecked);
            }
        });

        PackageManager packageManager = getActivity().getPackageManager();

        mCameraButton = v.findViewById(R.id.take_event_picture_button);
        mEventPicture = v.findViewById(R.id.event_picture);
        updatePhotoView();
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        boolean canTakePhoto = mPhotoFile != null &&
                    captureImage.resolveActivity(packageManager) != null;
        mCameraButton.setEnabled(canTakePhoto);
        if (canTakePhoto) {
            Uri uri = Uri.fromFile(mPhotoFile);
            captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }
        mCameraButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivityForResult(captureImage, REQUEST_PHOTO);
                }
        });

        mDownloadPicButton = v.findViewById(R.id.download_event_picture_button);
        mDownloadPicButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, DOWNLOAD_PHOTO);
                }
        });

        return v;
    }

    private void updatePhotoView() {
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mEventPicture.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(
                    mPhotoFile.getPath(), getActivity());
            mEventPicture.setImageBitmap(bitmap);
        }
    }

    private void uploadPhoto(Intent data) {
        Uri chosenImageUri = data.getData();
        String imagePath = getRealPathFromURI_API19(getActivity(), chosenImageUri);
        InputStream ims = getClass().getResourceAsStream(imagePath);
        Bitmap bitmap = PictureUtils.getScaledBitmap(
                imagePath, getActivity());
//        Bitmap bitmap = BitmapFactory.decodeStream(ims);
        mEventPicture.setImageBitmap(bitmap);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getRealPathFromURI_API19(Context context, Uri uri){

        String filePath = "";
        String wholeID = DocumentsContract.getDocumentId(uri);

        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = { MediaStore.Images.Media.DATA };

        // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, new String[]{ id }, null);

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }

    public static EventFragment newInstance(UUID eventID){
        Bundle args = new Bundle();
        args.putSerializable(ARG_EVENT_ID, eventID);

        EventFragment eventFragment = new EventFragment();

        if(eventID == null){
            Event newEvent = new Event();
            eventFragment.mEvent = newEvent;
        }

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
        else if (requestCode == REQUEST_PHOTO) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_GRANTED) {
                updatePhotoView();
            }
            else{
                ActivityCompat.requestPermissions(getActivity(), new String[]
                        {Manifest.permission.CAMERA}, 0);
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) ==
                        PackageManager.PERMISSION_GRANTED) {
                    updatePhotoView();
                }
            }
        }
        else if (requestCode == DOWNLOAD_PHOTO) {

            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_GRANTED) {
                uploadPhoto(data);
            }
            else {
                ActivityCompat.requestPermissions(getActivity(), new String[]
                        {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) ==
                        PackageManager.PERMISSION_GRANTED) {
                    uploadPhoto(data);
                }
            }
        }
    }

    private void updateDate() {
        mDateButton.setText(mEvent.getDate().toString());
    }

    public void returnResult(){
        getActivity().setResult(Activity.RESULT_OK,null);
    }

}
