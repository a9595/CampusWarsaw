package tieorange.com.campuswarsaw;

import org.ocpsoft.prettytime.nlp.PrettyTimeParser;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by tieorange on 04/05/16.
 */
public class DateTimeParser {

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
        String startDate = MessageFormat.format(date + " {0} {1}", startTime, pmAMend);
        String endDate = MessageFormat.format(date + " {0} {1}", endTime, pmAMend);

        DateFormat format = new SimpleDateFormat("EEE, d MMM h:mm a", Locale.ENGLISH);

        try {
            Date parsedStartDate = format.parse(startDate);
            Date parsedEndDate = format.parse(endDate);
            parsedDates.add(parsedStartDate);
            parsedDates.add(parsedEndDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        List<Date> dates = new PrettyTimeParser().parse(eventTimeEdited);
        return dates;
    }
}
