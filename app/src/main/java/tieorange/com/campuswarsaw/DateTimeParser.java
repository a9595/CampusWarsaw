package tieorange.com.campuswarsaw;

import org.ocpsoft.prettytime.nlp.PrettyTimeParser;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by tieorange on 04/05/16.
 */
public class DateTimeParser {

    public static List<Date> Parse(String time, String date) {
//        String eventDateTime = "Wednesday, 4 May 6:00 – 8:00 PM";
        String startTime = time.substring(0, 4);
        String pmAM = time.substring(10);
        String endTime = time.substring(5, 9);

        String eventTimeEdited = MessageFormat.format(date + " {0} {1} - {2} {3}", startTime, pmAM, endTime, pmAM);
        List<Date> dates = new PrettyTimeParser().parse(eventTimeEdited);
        return dates;
    }
}
