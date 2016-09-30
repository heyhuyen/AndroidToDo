package com.huyentran.todo.util;

import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Helper methods for dealing with dates.
 */
public final class DateUtils {
    public static final int TODAY = 0;
    public static final int YESTERDAY = -1;
    public static final int TOMORROW = 1;
    public static final int PAST = -2;
    public static final int FUTURE = 2;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    private DateUtils() {}

    /**
     * Get a {@link} Calendar from the given date string. If the given date string is null or empty
     * or otherwise cannot be parsed, the current date is returned by default.
     * @param dateStr yyyy-mm-dd formatted string
     * @return a calendar date
     */
    public static Calendar getDateFromString(String dateStr) {
        Calendar dueDate = new GregorianCalendar();
        try {
            dueDate.setTime(DATE_FORMAT.parse(dateStr));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dueDate;
    }

    /**
     * Get the the string representation for the date picker's date.
     * @param datePicker
     * @return a date string
     */
    public static String getDateStringFromPicker(DatePicker datePicker) {
        int day  = datePicker.getDayOfMonth();
        int month= datePicker.getMonth();
        int year = datePicker.getYear();
        Calendar c = Calendar.getInstance(Locale.getDefault());
        c.set(year, month, day);

        return DATE_FORMAT.format(c.getTime());
    }

    /**
     * Get the given date's position relative to today.
     * @param calendar the date to check
     * @return an integer denoting the date position [-2, -1, 0, 1, 2]
     */
    public static int compareToday(Calendar calendar) {
        Calendar today = dateAtStartOfDay(new GregorianCalendar());
        Calendar otherDate = dateAtStartOfDay(calendar);
        if (otherDate.equals(today)) {
            return TODAY;
        } else if (otherDate.before(today)) {
            Calendar yesterday = new GregorianCalendar();
            yesterday.setTime(today.getTime());
            yesterday.add(Calendar.DAY_OF_MONTH, -1);
            if (otherDate.equals(yesterday)) {
                return YESTERDAY;
            }
            return PAST;
        } else {
            Calendar tomorrow = new GregorianCalendar();
            tomorrow.setTime(today.getTime());
            tomorrow.add(Calendar.DAY_OF_MONTH, 1);
            if (otherDate.equals(tomorrow)) {
                return TOMORROW;
            }
            return FUTURE;
        }
    }

    private static Calendar dateAtStartOfDay(Calendar calendar) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.set(year, month, day, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }
}
