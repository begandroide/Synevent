package com.rilixtech.agendacalendarview.utils;

import com.rilixtech.agendacalendarview.CalendarManager;
import com.rilixtech.agendacalendarview.R;
import com.rilixtech.agendacalendarview.models.IWeekItem;
import android.content.Context;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Class containing helper functions for dates
 */
public class DateHelper {

  private static Calendar mCalendar = null;

  /**
   * Check if two Calendar instances have the same time (by month, year and day of month)
   *
   * @param cal The first Calendar instance.
   * @param selectedDate The second Calendar instance.
   * @return True if both instances have the same time.
   */
  public static boolean sameDate(Calendar cal, Calendar selectedDate) {
    return cal.get(Calendar.MONTH) == selectedDate.get(Calendar.MONTH) && cal.get(Calendar.YEAR) == selectedDate.get(
        Calendar.YEAR) && cal.get(Calendar.DAY_OF_MONTH) == selectedDate.get(Calendar.DAY_OF_MONTH);
  }

  /**
   * Check if a Date instance and a Calendar instance have the same time (by month, year and day
   * of month)
   *
   * @param cal The Calendar instance.
   * @param selectedDate The Date instance.
   * @return True if both have the same time.
   */
  public static boolean sameDate(Calendar cal, Date selectedDate) {
    if(mCalendar == null) mCalendar = Calendar.getInstance();
    mCalendar.setTime(selectedDate);
    return cal.get(Calendar.MONTH) == mCalendar.get(Calendar.MONTH) && cal.get(Calendar.YEAR) == mCalendar.get(
        Calendar.YEAR) && cal.get(Calendar.DAY_OF_MONTH) == mCalendar.get(Calendar.DAY_OF_MONTH);
  }

  /**
   * Check if a Date instance is between two Calendar instances' dates (inclusively) in time.
   *
   * @param selectedDate The date to verify.
   * @param startCal The start time.
   * @param endCal The end time.
   * @return True if the verified date is between the two specified dates.
   */
  public static boolean isBetweenInclusive(Date selectedDate, Calendar startCal, Calendar endCal) {
    if(mCalendar == null) mCalendar = Calendar.getInstance();
    mCalendar.setTime(selectedDate);
    // Check if we deal with the same day regarding startCal and endCal
    return sameDate(mCalendar, startCal) || mCalendar.after(startCal) && mCalendar.before(endCal);
  }

  public enum DatePosition {
    BEFORE, AFTER, SAME, UNKNOWN
  }

  public static DatePosition getPositionOfDate(Calendar cal, Calendar againstCal) {
    if(sameDate(cal, againstCal)) return DatePosition.SAME;
    if(cal.after(againstCal)) return DatePosition.AFTER;
    if(cal.before(againstCal)) return DatePosition.BEFORE;
    // This won't be happen on normal circumstance
    return DatePosition.UNKNOWN;
  }

  /**
   * Check if Calendar instance's date is in the same week, as the WeekItem instance.
   *
   * @param cal The Calendar instance to verify.
   * @param week The WeekItem instance to compare to.
   * @return True if both instances are in the same week.
   */
  public static boolean sameWeek(Calendar cal, IWeekItem week) {
    return (cal.get(Calendar.WEEK_OF_YEAR) == week.getWeekInYear() && cal.get(Calendar.YEAR) == week.getYear());
  }

  /**
   * Convert a millisecond duration to a string format
   *
   * @param millis A duration to convert to a string form
   * @return A string of the form "Xd" or either "XhXm".
   */
  public static String getDuration(Context context, long millis) {
    if (millis < 0) {
      throw new IllegalArgumentException("Duration must be greater than zero!");
    }

    long days = TimeUnit.MILLISECONDS.toDays(millis);
    millis -= TimeUnit.DAYS.toMillis(days);
    long hours = TimeUnit.MILLISECONDS.toHours(millis);
    millis -= TimeUnit.HOURS.toMillis(hours);
    long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);

    StringBuilder sb = new StringBuilder(64);
    if (days > 0) {
      sb.append(days);
      sb.append(context.getResources().getString(R.string.agenda_event_day_duration));
      return (sb.toString());
    } else {
      if (hours > 0) {
        sb.append(hours);
        sb.append("h");
      }
      if (minutes > 0) {
        sb.append(minutes);
        sb.append("m");
      }
    }

    return (sb.toString());
  }

  /**
   * Used for displaying the date in any section of the agenda view.
   *
   * @param calendar The date of the section.
   * @param locale The locale used by the Sunrise calendar.
   * @return The formatted date without the year included.
   */
  public static String getYearLessLocalizedDate(Calendar calendar, Locale locale) {
    SimpleDateFormat sdf =
        (SimpleDateFormat) SimpleDateFormat.getDateInstance(DateFormat.FULL, CalendarManager.getInstance().getLocale());
    String pattern = sdf.toPattern();

    String yearLessPattern = pattern.replaceAll("\\W?[Yy]+\\W?", "");
    SimpleDateFormat yearLessSDF = new SimpleDateFormat(yearLessPattern, locale);
    String yearLessDate = yearLessSDF.format(calendar.getTime()).toUpperCase();
    if (yearLessDate.endsWith(",")) {
      yearLessDate = yearLessDate.substring(0, yearLessDate.length() - 1);
    }
    return yearLessDate;
  }
}
