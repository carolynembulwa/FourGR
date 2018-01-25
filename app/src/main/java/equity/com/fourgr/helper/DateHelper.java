package equity.com.fourgr.helper;

import android.content.Context;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateHelper {
    public static String getFormattedDate(Context context, String dateString){
        final String timeFormatString = "HH:mm";
        final String dateTimeFormatString = "yyyy-MM-dd HH:mm:ss";

        long timeinMilliseconds = convertTimeToMilliseconds(dateString, dateTimeFormatString);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeinMilliseconds);

        Calendar now = Calendar.getInstance();
        final long HOURS = 60 * 60 * 60;

        if (now.get(Calendar.DATE) == calendar.get(Calendar.DATE)){
            return FormatDateString(dateString, dateTimeFormatString, timeFormatString);
        }else if (now.get(Calendar.DATE) - calendar.get(Calendar.DATE) == 1){
            return "Yesterday";
        }else{
            return FormatDateString(dateString, dateTimeFormatString, "dd/MM/yyyy");
        }
    }

    public static String FormatDateString(String dateString, String inputFormat, String outputformat){
        String formatted_date = "";

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(inputFormat);
            Date d = dateFormat.parse(dateString);

            dateFormat = new SimpleDateFormat(outputformat);
            formatted_date = dateFormat.format(d);
        }catch(Exception e){
            Log.e("Submissions", e.getMessage());
        }

        return formatted_date;
    }

    public static long convertTimeToMilliseconds(String dateString, String format){
        long timeInMills = 0;
        SimpleDateFormat sdf = new SimpleDateFormat(format);

        try {
            Date mDate = sdf.parse(dateString);
            timeInMills = mDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeInMills;

    }
}
