package org.leadingsoft.golf.api.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.springframework.stereotype.Component;

@Component
public class DateLogicUtils {
  private static final String DATE_TIME_FORMAT = "yyyyMMddHHmmssSSS";
  private static final String DATE_FORMAT = "yyyyMMdd";
  private static final String DATE_FORMAT_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

  public Calendar getCalendar(long date) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(new Date(date));
    return cal;
  }

  public Calendar getCalendar(String date) {
    Long longDate = Long.parseLong(date);
    return getCalendar(longDate);
  }

  public Calendar getCurrent() {
    return Calendar.getInstance(Locale.JAPAN);
  }

  public String getCurrentTimeString() {
    return getTimeString(getCurrent());
  }

  public String getTimeString(Calendar calendar) {
    SimpleDateFormat sf = new SimpleDateFormat(DATE_TIME_FORMAT, Locale.JAPAN); // not thread safe
    return sf.format(calendar.getTime());
  }

  public String getPstTimeString(Calendar calendar) {
    SimpleDateFormat sf = new SimpleDateFormat(DATE_TIME_FORMAT, Locale.JAPAN); // not thread safe
    sf.setTimeZone(TimeZone.getTimeZone("PST"));
    return sf.format(calendar.getTime());
  }

  public Calendar fromTimeString(String timeString) {
    SimpleDateFormat sf = new SimpleDateFormat(DATE_TIME_FORMAT, Locale.JAPAN); // not thread safe
    sf.setLenient(false);
    Calendar cal = Calendar.getInstance();
    try {
      cal.setTime(sf.parse(timeString));
    } catch (ParseException e) {
      throw new IllegalArgumentException(e);
    }
    return cal;
  }

  /**
   * addDays分加算した日付を返却
   *
   * @param calendar
   * @return yyyyMMdd形式で加算された日付を返却
   */
  public String getAddDateString(Calendar calendar, String addDays) {
    Calendar workCal = (Calendar) calendar.clone();
    workCal.add(Calendar.DAY_OF_MONTH, Integer.valueOf(addDays));
    return getDateString(workCal);
  }

  public String getDateString(Calendar calendar) {
    SimpleDateFormat sf = new SimpleDateFormat(DATE_FORMAT, Locale.JAPAN); // not
                                                                           // thread
                                                                           // safe
    return sf.format(calendar.getTime());
  }

  /**
   * システム時間取得:
   * 
   * @param calendar
   * @return 年月日時分秒
   */
  public String getCurrentTimeString(Calendar calendar) {
    SimpleDateFormat sf = new SimpleDateFormat(DATE_FORMAT_YYYYMMDDHHMMSS, Locale.JAPAN);
    return sf.format(calendar.getTime());
  }

}
