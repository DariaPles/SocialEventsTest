package com.dariapro.socialevent;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Date;
import java.util.UUID;

public class EventCursorWrapper extends CursorWrapper {
    public EventCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Event getEvent() {
        String uuidString = getString(getColumnIndex(EventDBSchema.EventTable.Cols.UUID));
        String title = getString(getColumnIndex(EventDBSchema.EventTable.Cols.TITLE));
        long date = getLong(getColumnIndex(EventDBSchema.EventTable.Cols.DATE));
        int isSolved = getInt(getColumnIndex(EventDBSchema.EventTable.Cols.SOLVED));

        Event event = new Event(UUID.fromString(uuidString));
        event.setTitle(title);
        event.setDate(new Date(date));
        event.setSolved(isSolved != 0);

        return event;
//        return null;
    }
}
