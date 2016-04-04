package com.trurating.utilTime;

/**
 * Created by Paul on 11/03/2016.
 */
public class TDateTime {

    public static String getCurrentDateTime() {
        return new TDate() + " " + new TTime();
    }
}
