package com.dariapro.socialevent;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dariapro.socialevent.EventDBSchema.EventTable;

public class EventBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "eventBase.db";


    public EventBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       db.execSQL("create table " + EventTable.NAME  + "(" +
               " _id integer primary key autoincrement, " +
               EventTable.Cols.UUID + ", " +
               EventTable.Cols.TITLE + ", " +
               EventTable.Cols.DATE + ", " +
               EventTable.Cols.SOLVED + ")"
       );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
