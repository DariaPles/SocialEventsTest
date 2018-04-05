package com.dariapro.socialevent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dariapro.socialevent.EventDBSchema.EventTable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Lenovo on 24.01.2018.
 */

public class EventLab {
    public static EventLab sEventLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public void addEvent(Event event){
       ContentValues values = getContentValues(event);

       mDatabase.insert(EventTable.NAME, null, values);
    }

    private static ContentValues getContentValues(Event event){
        ContentValues values = new ContentValues();
        values.put(EventTable.Cols.UUID, event.getId().toString());
        values.put(EventTable.Cols.DATE, event.getDate().getTime());
        values.put(EventTable.Cols.SOLVED, event.isSolved() ? 1: 0);
        values.put(EventTable.Cols.TIME, event.getEventTime().toString());
        values.put(EventTable.Cols.TITLE, event.getTitle());
        return values;
    }

    public List<Event> getEvents(){
        return new ArrayList<>();
    }

    public Event getEvent(UUID uuid){

        return null;
    }

    public void updateEvent(Event event) {
        String uuidString = event.getId().toString();
        ContentValues values = getContentValues(event);

        mDatabase.update(EventTable.NAME, values,
                EventTable.Cols.UUID + " = ?",
                new String[] { uuidString });
    }

    private Cursor queryCrimes(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                EventTable.NAME,
                null, // Columns - null выбирает все столбцы
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null // orderBy
        );
        return cursor;
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


//        for(int i = 0; i < 100; i++){
//            Event event = new Event();
//            event.setTitle("Event #"+i);
//            event.setSolved(i%2 == 0);
//            mEvents.add(event);
//        }
    }
}
