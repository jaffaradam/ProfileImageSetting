package com.alltime.profile.profileimagesetting.webapi;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class Util {


    public static long getCurrentTime() {
        DateTime dt = new DateTime(DateTimeZone.UTC);
        return dt.getMillis();
    }

    public static String getTAG(Class className) {
        String simpleClasssName = className.getSimpleName();
        if (simpleClasssName.length() > 22)
            return simpleClasssName.substring(0, 22);
        else
            return simpleClasssName;
    }


}
