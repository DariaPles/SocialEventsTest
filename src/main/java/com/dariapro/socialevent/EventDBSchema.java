package com.dariapro.socialevent;

public class EventDBSchema {
    public static final class EventTable{
        public static final String NAME = "events";
        public static final class Cols{
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String TIME = "time";
            public static final String SOLVED = "solved";
        }
    }
}
