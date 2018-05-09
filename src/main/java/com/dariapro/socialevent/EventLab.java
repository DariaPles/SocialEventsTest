package com.dariapro.socialevent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import com.dariapro.socialevent.EventDBSchema.EventTable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Lenovo on 24.01.2018.
 */

public class EventLab {
    public static EventLab sEventLab;

    //private List<Event> mEvents = new ArrayList<Event>();

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public void addEvent(Event event){
        if(!event.getTitle().trim().equals("")) {
            ContentValues values = getContentValues(event);
            mDatabase.insert(EventTable.NAME, null, values);
        }
    }

    public void deleteEvent(Event event){
        String uuidString = event.getId().toString();
        mDatabase.delete(EventTable.NAME,
                EventTable.Cols.UUID + " = ?",
                new String[] { uuidString });
    }

    private static ContentValues getContentValues(Event event){
        ContentValues values = new ContentValues();
        values.put(EventTable.Cols.UUID, event.getId().toString());
        values.put(EventTable.Cols.DATE, event.getDate().getTime());
        values.put(EventTable.Cols.SOLVED, event.isSolved() ? 1: 0);
        values.put(EventTable.Cols.TIME, (String) null);
        values.put(EventTable.Cols.TITLE, event.getTitle());
        return values;
    }

    public List<Event> getEvents(){
        List<Event> events = new ArrayList<Event>();

        EventCursorWrapper cursorWrapper = queryEvents(null,null);

        try{
            cursorWrapper.moveToFirst();
            while(!cursorWrapper.isAfterLast()){
                events.add(cursorWrapper.getEvent());
                cursorWrapper.moveToNext();
            }
        } finally {
            cursorWrapper.close();
        }
        return events;
    }

    public Event getEvent(UUID uuid){
        EventCursorWrapper cursor = queryEvents(
                EventTable.Cols.UUID + " = ?",
                new String[] { uuid.toString() }    );
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getEvent();
        } finally {
            cursor.close();
        }

    }

    public void updateEvent(Event event) {
        String uuidString = event.getId().toString();
        ContentValues values = getContentValues(event);

        mDatabase.update(EventTable.NAME, values,
                EventTable.Cols.UUID + " = ?",
                new String[] { uuidString });
    }

    private EventCursorWrapper queryEvents(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                EventTable.NAME,
                null, // Columns - null выбирает все столбцы
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null // orderBy
        );
        return new EventCursorWrapper(cursor);
    }

    public static EventLab get(Context context){
        if(sEventLab == null){
            sEventLab = new EventLab(context);
        }
        return sEventLab;
    }

    private EventLab(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new EventBaseHelper(mContext)
                .getWritableDatabase();
        //mEvents = new ArrayList<Event>();
//        for(int i = 0; i < 10; i++){
//            Event event = new Event();
//            event.setTitle("Event #"+i);
//            event.setSolved(i%2 == 0);
//            mEvents.add(event);
//        }
    }

    public File getPhotoFile(Event event) {
        File externalFilesDir = mContext
                .getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (externalFilesDir == null) {
            return null;
        }
        return new File(externalFilesDir, event.getPhotoFilename());
    }
}
