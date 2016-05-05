package tieorange.com.campuswarsaw;

import android.util.Log;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by tieorange on 04/05/16.
 */
public class DateTimeParser {

    private static final String TAG = DateTimeParser.class.getCanonicalName();

    public static List<Date> Parse(String time, String date) {
//        String eventDateTime = "Wednesday, 4 May 6:00 – 8:00 PM";
        List<Date> parsedDates = new ArrayList<>();

        int indexOfSeparator = time.indexOf("–");
        String startTime = time.substring(0, indexOfSeparator);
        String pmAMend = time.substring(time.length() - 2);
        String pmAMstart = pmAMend;
        String endTime = time.substring(indexOfSeparator + 1, time.length() - 3);

        // check PM_AM:
        int indexOfTwoDots = time.indexOf(":");
        int indexOfTwoDotsLast = time.lastIndexOf(":");

        int startHour = Integer.parseInt(time.substring(0, indexOfTwoDots));
        int endHour = Integer.parseInt(time.substring(indexOfSeparator + 1, indexOfTwoDotsLast));
        if (startHour >= endHour)
            pmAMstart = "AM";
        //


        String eventTimeEdited = MessageFormat.format(date + " {0} {1} - {2} {3}", startTime, pmAMstart, endTime, pmAMend);
        String startDate = MessageFormat.format(date + " {0} {1}", startTime, pmAMstart);
        String endDate = MessageFormat.format(date + " {0} {1}", endTime, pmAMend);

        DateFormat format = new SimpleDateFormat("EEE, d MMM h:mm a");

        try {
            Date parsedStartDate = format.parse(startDate);
            Date parsedEndDate = format.parse(endDate);
            parsedDates.add(parsedStartDate);
            parsedDates.add(parsedEndDate);

            setCurrentYearToDate(parsedStartDate);
            setCurrentYearToDate(parsedEndDate);

            Log.d(TAG, "Parse: parsedStartDate = " + parsedStartDate.toString());
            Log.d(TAG, "Parse: parsedEndDate = " + parsedEndDate.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }


//        List<Date> dates = new PrettyTimeParser().parse(eventTimeEdited);
        return parsedDates;
    }

    public static int getCurrentYear() {
        Date date = new Date();
        int result = 2016;
        if (date != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            result = cal.get(Calendar.YEAR);
        }
        return result;
    }

    private static void setCurrentYearToDate(Date date) {
        date.setYear(getCurrentYear() - 1900);

//        Calendar c = Calendar.getInstance();
//        c.setTime(date);
//        c.set(Calendar.YEAR, 2016 + 1900);
//        date = c.getTime();
    }
}
