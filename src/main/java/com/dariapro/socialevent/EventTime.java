package com.dariapro.socialevent;


public class EventTime {
    int hour = 0;
    int minute = 0;

    EventTime(){

    }

    public int hourFrom24To12(){
        return hour - 12;
    }

    public int hourFrom12To24(){
        return hour + 12;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }
}
