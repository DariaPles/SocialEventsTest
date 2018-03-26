package com.dariapro.socialevent;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Lenovo on 24.01.2018.
 */

public class EventLab {
    public static EventLab sEventLab;
    private List<Event> mEvents;

    public List<Event> getEvents(){
        return mEvents;
    }

    public Event getEvent(UUID uuid){
        for(Event event: mEvents){
            if(event.getId().equals(uuid)){
                return event;
            }
        }
        return null;
    }

    public static EventLab get(Context context){
        if(sEventLab == null){
            sEventLab = new EventLab(context);
        }
        return sEventLab;
    }

    private EventLab(Context context){
        mEvents = new ArrayList<Event>();
        for(int i = 0; i < 100; i++){
            Event event = new Event();
            event.setTitle("Event #"+i);
            event.setSolved(i%2 == 0);
            mEvents.add(event);
        }
    }
}
