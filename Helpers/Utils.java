package com.chefless.ela.photo_gallery.Helpers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ela on 18/10/2016.
 */

public class Utils {

    public static Date getUTCDateFromString(String s){

        if(s==null || s.isEmpty())
            return  null;

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss-SS:00"); // Quoted "Z" to indicate UTC, no timezone offset

        Date d = new Date(0);
        try {
            d = df.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }

    public static String getDateWithDefaultFormat(Date d) {
        if (d == null)
            return "N/A";
        DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm");
        return dateFormat.format(d);
    }
}
