package Common.Helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeHelper {

    public static String getDate(String dateTimeString) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            // Parse the datetime string into a Date object
            Date date = inputFormat.parse(dateTimeString);

            // Format the Date object to a date-only string
            return outputFormat.format(date);
        } catch (ParseException e) {
            return "";
        }
    }

    public static String getTime(String dateTimeString) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm:ss");
        try {
            // Parse the datetime string into a Date object
            Date date = inputFormat.parse(dateTimeString);

            // Format the Date object to a date-only string
            return outputFormat.format(date);
        } catch (ParseException e) {
            return "";
        }
    }
}
