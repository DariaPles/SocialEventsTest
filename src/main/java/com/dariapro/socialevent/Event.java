package com.dariapro.socialevent;

import java.util.Date;
import java.util.UUID;

public class Event {
    private UUID mId;
    private String mTitle;
    private Date mDate;
    private EventTime mEventTime;
    private boolean mSolved;

    public Event(){
        mId = UUID.randomUUID();
        mDate = new Date();
        mEventTime = new EventTime();
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        this.mDate = date;
    }

    public EventTime getEventTime() {
        return mEventTime;
    }

    public void setEventTime(EventTime mEventTime) {
        this.mEventTime = mEventTime;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        this.mSolved = solved;
    }
}
