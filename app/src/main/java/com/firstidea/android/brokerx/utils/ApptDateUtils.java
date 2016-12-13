/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.firstidea.android.brokerx.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Govind
 */
public class ApptDateUtils {

    public static final int SECONDS_IN_A_DAY = 86400;

    public static final String dateFormat_yyyy_MM_dd = "yyyy-MM-dd";
    public static final String dateFormat_yyyy_MMM= "yyyy MMMM";
    public static final String dateFormat_MMM= "MMMM";
    public static final String dateFormat_dd_mmm_yyyy = "dd-MMM-yyyy";
    //    public static final String dateFormat_yyyy_MM_dd_hh_mm_ss = "yyyy-MM-dd HH:mm:ss";
    public static final String dateFormat_dd_MMM_yyyy_hh_mm_a = "dd-MMM-yyyy h:mm a";

    public static final String javascript_date_time_format = "MMMM dd, yyyy HH:mm:ss";
    public static final String javascript_date_format = "MMMM dd, yyyy";

    public static final String dateFormat_hh_mm_a = "h:mm a";
    public static final String dateFormat_hh_mma = "h:mma";
    public static final String dateFormat_hh_mm = "H:mm";
    public static final String timeFormat_hh_mm_ss = "HH:mm ss";
    public static final String timeFormat_hh_mmss = "HH:mm:ss";
    public static final String dateFormat_yyyyMM = "yyyyMM";
    public static final String dateFormat_yyyy_MM_DD_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";

    static final String YEAR = "yrs";
    static final String MONTH = "m";

    public static Date getCurrentDate() {
        //Calendar now = Calendar.getInstance();
        return getTimeZoneDate("Asia/Kolkata");//now.getTime();
    }

    public static Date getCurrentMonthStartDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date getCurrentDateAndTime() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = getTimeZoneDate("Asia/Kolkata");//Calendar.getInstance().getTime();
        String strDt = (String) dateFormat.format(date);
        try {
            date = dateFormat.parse(strDt);
        } catch (ParseException ex) {
            Logger.getLogger(ApptDateUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return date;
    }

    public static Date getCurrentFormatedDate() {

        DateFormat dateFormat = new SimpleDateFormat(dateFormat_yyyy_MM_dd);
        Date date = getTimeZoneDate("Asia/Kolkata");//Calendar.getInstance().getTime();
        String strDt = (String) dateFormat.format(date);
        try {
            date = dateFormat.parse(strDt);
        } catch (ParseException ex) {
            Logger.getLogger(ApptDateUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return date;
    }

    public static Date getCurrentJavascriptdDate() {

        DateFormat dateFormat = new SimpleDateFormat(javascript_date_format);
        Date date = getTimeZoneDate("Asia/Kolkata");//Calendar.getInstance().getTime();
        String strDt = (String) dateFormat.format(date);
        try {
            date = dateFormat.parse(strDt);
        } catch (ParseException ex) {
            Logger.getLogger(ApptDateUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return date;
    }

    public static Date getFormatedDateInYYYY_MM_DD(Date date) {

        DateFormat dateFormat = new SimpleDateFormat(dateFormat_yyyy_MM_dd);
        String strDt = (String) dateFormat.format(date);
        try {
            date = dateFormat.parse(strDt);
        } catch (ParseException ex) {
            Logger.getLogger(ApptDateUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return date;
    }

    public static String getFormatedDateInYYYY_MM_DDString(Date date) {

        DateFormat dateFormat = new SimpleDateFormat(dateFormat_yyyy_MM_dd);
        String strDt = (String) dateFormat.format(date);
        return strDt;
    }

    public static Date getFutureOrPastMonthsDate(int months) {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.MONTH, months);
        return now.getTime();
    }

    public static Date addMonthsToDate(Date inputDate, int months) {
        Calendar now = Calendar.getInstance();
        now.setTime(inputDate);
        now.add(Calendar.MONTH, months);
        return now.getTime();
    }

    public static String getDateIn24HrsFormat(Date date) {
        String formatDt = "";
        if (date != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm ss");
            formatDt = dateFormat.format(date);
        }
        return formatDt;
    }

    public static String getDateIn24HrsFormatForFireFox(Date date) {
        String formatDt = "";
        if (date != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
            formatDt = dateFormat.format(date);
        }
        return formatDt;
    }

    public static Date getFormatedDate(String inputDate) {

        DateFormat dateFormat = new SimpleDateFormat(dateFormat_yyyy_MM_dd);
        Date date = null;
        try {
            date = dateFormat.parse(inputDate);
        } catch (ParseException ex) {
            Logger.getLogger(ApptDateUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return date;
    }

    public static Date getDateFromYearmonth(String yearMonth) {
        DateFormat dateFormat = new SimpleDateFormat(dateFormat_yyyy_MMM);
        Date date = null;
        try {
            date = dateFormat.parse(yearMonth);
        } catch (ParseException ex) {
            Logger.getLogger(ApptDateUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return date;
    }

    public static String getMonthFromDate(Date yearMonth) {
        DateFormat dateFormat = new SimpleDateFormat(dateFormat_MMM);
        String month = null;
        try {
            month = dateFormat.format(yearMonth);
        } catch (Exception ex) {
            Logger.getLogger(ApptDateUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return month;
    }

    //    public static Date getFormatedDate_dd_mmm_yyyy(String inputDate) {
//
//        DateFormat dateFormat = new SimpleDateFormat(dateFormat_dd_mmm_yyyy);
//        Date date = null;
//        try {
//            date = dateFormat.parse(inputDate);
//        } catch (ParseException ex) {
//            Logger.getLogger(ApptDateUtils.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return date;
//    }
   /* public static String getDateDifference(Date dateOfBirth, Date currentDate) {

        DateTime startDt = new DateTime(dateOfBirth.getTime());
        DateTime endDt = new DateTime(currentDate.getTime());
        Period period = new Period(startDt, endDt, PeriodType.yearMonthDay());
        String age = "";
        if (period.getYears() > 0) {
            age = period.getYears() + "." + period.getMonths() + " yrs";
        }
//        if (period.getMonths() >= 3) {
//            age = age + period.getMonths() + "m";
//        }
        return age;
    }

    public static Integer getDateTimeDifferenceInMinutes(Date startDate, Date endDate) {

        DateTime startDt = new DateTime(startDate.getTime());
        DateTime endDt = new DateTime(endDate.getTime());
        Period period = new Period(startDt, endDt, PeriodType.yearMonthDay());

        Integer difference = null;
        difference = period.getMinutes();
        return difference;
    }
*/
    public static Date getFirstDayofMonthForInputDate(Date inputDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(inputDate);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return cal.getTime();
    }

    public static Date getLastDayofMonthForInputDate(Date inputDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(inputDate);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }

    //getting befor months date send negative value for xMonths (e.g: -1)
    public static Date getFirstDateBeforAfterXMonths(Date inputDate, int xMonths) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(inputDate);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.add(Calendar.MONTH, xMonths);
        return cal.getTime();
    }

    public static Date getFormatedDateAndTime(String inputDate) {
        DateFormat dateFormat = new SimpleDateFormat(dateFormat_yyyy_MM_DD_HH_mm_ss);
        Date date = null;
        try {
            date = dateFormat.parse(inputDate);
        } catch (ParseException ex) {
            Logger.getLogger(ApptDateUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return date;
    }

    public static Date getFormatedDateAndTime(long milis) {
        DateFormat dateFormat = new SimpleDateFormat(dateFormat_yyyy_MM_DD_HH_mm_ss);
        Date date = null;
        try {
            date = new Date(milis);
            String formattedDate = dateFormat.format(date);
            date = getFormatedDateAndTime(formattedDate);
        } catch (Exception ex) {
            Logger.getLogger(ApptDateUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return date;
    }

    public static String getFormatedDateAndTimeString(Date inputDate) {
        DateFormat dateFormat = new SimpleDateFormat(dateFormat_yyyy_MM_DD_HH_mm_ss);
        try {
            return dateFormat.format(inputDate);
        } catch (Exception ex) {
            Logger.getLogger(ApptDateUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public static Date getAppointmentDates(int initialDate) {

        Calendar cal = Calendar.getInstance();
        int no_of_diff_days = (initialDate / SECONDS_IN_A_DAY);
        cal.set(1970, 0, 1);
        cal.add(Calendar.DATE, no_of_diff_days);
        return cal.getTime();
    }

    public static Date getAppointmentStartDate(int initialDate) {

        Calendar cal = Calendar.getInstance();
        int no_of_diff_days = (initialDate / SECONDS_IN_A_DAY);
        cal.set(1970, 0, 1);
        cal.add(Calendar.DATE, no_of_diff_days);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }

    public static Date getAppointmentEndDate(int initialDate) {

        Calendar cal = Calendar.getInstance();
        int no_of_diff_days = (initialDate / SECONDS_IN_A_DAY);
        cal.set(1970, 0, 1);
        cal.add(Calendar.DATE, no_of_diff_days);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTime();
    }

    //    public static String getFormattedDateString(Date date) {
//        if (date != null) {
//            String formatDt = "";
//            SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormat_yyyy_MM_dd_hh_mm_ss);
//            formatDt = dateFormat.format(date);
//            return formatDt;
//        }
//        return null;
//    }
    public static String getFormattedDateString(Date date) {
        if (date != null) {
            String formatDt = "";
            SimpleDateFormat dateFormat = new SimpleDateFormat(javascript_date_time_format);
            formatDt = dateFormat.format(date);
            return formatDt;
        }
        return null;
    }

    public static String getFormattedDateString_yyyy_MM_dd(Date date) {
        if (date != null) {
            String formatDt = "";
            SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormat_yyyy_MM_dd);
            formatDt = dateFormat.format(date);
            return formatDt;
        }
        return null;
    }

    public static Date getDateTimeAt7_30AM() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        calendar.set(Calendar.MINUTE, 30);

        return calendar.getTime();
    }

    public static Date getDateTimeAt8_00AM() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 00);

        return calendar.getTime();
    }

    public static Date getDateTimeAt8_30AM() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 30);

        return calendar.getTime();
    }

    public static Date getMidnightDateTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);

        return calendar.getTime();
    }

    private static String getDaySuffix(int day) {
        switch (day) {
            case 1:
            case 21:
            case 31:
                return "st";

            case 2:
            case 22:
                return "nd";

            case 3:
            case 23:
                return "rd";

            default:
                return "th";
        }
    }

    public static String getFormattedDateStringdd_MMM(Date date) {

        Integer iDay = date.getDate();
        String daySuffix = getDaySuffix(iDay);
        SimpleDateFormat SDF = new SimpleDateFormat("d'" + daySuffix + "' MMM");
        String formatDt = "";
        formatDt = SDF.format(date);
        return formatDt;
    }

    public static String getFormattedDateStringdd_MMM_hh_mm(Date date) {

        Integer iDay = date.getDate();
        String daySuffix = getDaySuffix(iDay);
        SimpleDateFormat SDF = new SimpleDateFormat("d'" + daySuffix + "' MMM 'at' h:mma");
        String formatDt = "";
        formatDt = SDF.format(date);
        return formatDt;
    }

    public static String getFormattedDateStringdd_MMM_yyyy(Date date) {
        //SimpleDateFormat SDF = new SimpleDateFormat(dateFormat_dd_mmm_yyyy);
        SimpleDateFormat SDF = new SimpleDateFormat(javascript_date_format);
        String formatDt = SDF.format(date);
        return formatDt;
    }

    public static String getCaseSheetFormattedDateStringdd_MMM_yyyy(Date date) {
        SimpleDateFormat SDF = new SimpleDateFormat(dateFormat_dd_mmm_yyyy);
//        SimpleDateFormat SDF = new SimpleDateFormat(javascript_date_format);
        String formatDt = SDF.format(date);
        return formatDt;
    }

    public static String getTimeStringFor12HrsFromDateTime(Date dateTime) {
        SimpleDateFormat SDF = new SimpleDateFormat(dateFormat_hh_mm_a);
        String time = SDF.format(dateTime);
        return time;
    }

    public static String getTimeStringFor12HrsFromDateTimeForAppt(Date dateTime) {
        SimpleDateFormat SDF = new SimpleDateFormat(dateFormat_hh_mma);
        String time = SDF.format(dateTime);
        return time;
    }

    public static String getTimeStringFor24HrsFromDateTime(Date dateTime) {
        SimpleDateFormat SDF = new SimpleDateFormat(dateFormat_hh_mm);
        String time = SDF.format(dateTime);
        return time;
    }

    public static Date getTimeFromDateTime(Date dateTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTime);
        calendar.set(0, 0, 0);
        Date time = calendar.getTime();
        return time;
    }

    public static Date getTimeFromString(String dateTime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormat_hh_mm_a);
        Date date = null;
        try {
            date = dateFormat.parse(dateTime);
        } catch (ParseException ex) {
            Logger.getLogger(ApptDateUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return date;
    }

    public static Date getTimeZoneDate(String timeZone) {

        TimeZone tz = TimeZone.getTimeZone(timeZone);
        Calendar timeZoneDt = Calendar.getInstance(tz);
        Calendar calendar = Calendar.getInstance();
        calendar.set(timeZoneDt.get(Calendar.YEAR), timeZoneDt.get(Calendar.MONTH), timeZoneDt.get(Calendar.DATE),
                timeZoneDt.get(Calendar.HOUR_OF_DAY), timeZoneDt.get(Calendar.MINUTE), (timeZoneDt.get(Calendar.SECOND) + 90));

        return calendar.getTime();
    }

    public static Date getDateBeforeMinutes(Date inputDate, Integer minutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(inputDate);
        int hours = (int) (minutes / 60);
        int mins = (int) (minutes % 60);
        int prevHours = cal.get(Calendar.HOUR_OF_DAY);
        int prevMins = cal.get(Calendar.MINUTE);
        cal.set(Calendar.HOUR_OF_DAY, prevHours - hours);
        cal.set(Calendar.MINUTE, prevMins - mins);
        return cal.getTime();
    }

    public static Date getDateAfterMinutes(Date inputDate, Integer minutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(inputDate);
        int hours = (int) (minutes / 60);
        int mins = (int) (minutes % 60);
        int prevHours = cal.get(Calendar.HOUR_OF_DAY);
        int prevMins = cal.get(Calendar.MINUTE);
        cal.set(Calendar.HOUR_OF_DAY, prevHours + hours);
        cal.set(Calendar.MINUTE, prevMins + mins);
        return cal.getTime();
    }

    public static Date removeSecondsFromDate(Date inputDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(inputDate);
//        int mins = (int) (seconds / 60);
//        int sec = (int) (seconds % 60);
//        int prevHours = cal.get(Calendar.HOUR_OF_DAY);
//        int prevMins = cal.get(Calendar.MINUTE);
//        cal.set(Calendar.HOUR_OF_DAY, prevHours + hours);
//        cal.set(Calendar.MINUTE, prevMins + mins);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date getDateBeforeAfterXDays(Date inputDate, Integer days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(inputDate);
        cal.add(Calendar.DAY_OF_MONTH, days);
        return cal.getTime();
    }

    public static Date getDateBeforeAfterXMonths(Date inputDate, Integer months) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(inputDate);
        cal.add(Calendar.MONTH, months);
        return cal.getTime();
    }

    public static Date addTimeToDate(Date inputDate) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(inputDate);
        int hours = cal1.get(Calendar.HOUR_OF_DAY);
        int mins = cal1.get(Calendar.MINUTE);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hours);
        cal.set(Calendar.MINUTE, mins);
        return cal.getTime();
    }

    public static Date addTimeToDate(Date date, Date time) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(time);
        int hours = cal1.get(Calendar.HOUR_OF_DAY);
        int mins = cal1.get(Calendar.MINUTE);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, hours);
        cal.set(Calendar.MINUTE, mins);
        return cal.getTime();
    }

    public static Date getFormatedTimeFromString(String dateTime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(timeFormat_hh_mm_ss);
        Date date = null;
        try {
            date = dateFormat.parse(dateTime);
        } catch (ParseException ex) {
            Logger.getLogger(ApptDateUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return date;
    }
    public static Date getFormatedTimeFromStringWithoutSpace(String dateTime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(timeFormat_hh_mmss);
        Date date = null;
        try {
            date = dateFormat.parse(dateTime);
        } catch (ParseException ex) {
            Logger.getLogger(ApptDateUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return date;
    }


    public static String getFormattedDateString_yyyy_dd(Date date) {
        String formatDt = "";
        if (date != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormat_yyyyMM);
            formatDt = dateFormat.format(date);
        }
        return formatDt;
    }

    public static String getWeekDayOfDate(Date inputDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(inputDate);
        Integer dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        switch (dayOfWeek) {
            case Calendar.SUNDAY:
                return "Sunday";
            case Calendar.MONDAY:
                return "Monday";
            case Calendar.TUESDAY:
                return "Tuesday";
            case Calendar.WEDNESDAY:
                return "wednesday";
            case Calendar.THURSDAY:
                return "Thursday";
            case Calendar.FRIDAY:
                return "Friday";
            case Calendar.SATURDAY:
                return "Saturday";
        }
        return "";
    }

    public static String getDateFormat_dd_MMM_yyyy_hh_mm_a(Date inputDate) {
        SimpleDateFormat SDF = new SimpleDateFormat(dateFormat_dd_MMM_yyyy_hh_mm_a);
        String formatDt = SDF.format(inputDate);
        return formatDt;
    }

    public static Date getDateTimeFromUnixTimeStamp(int initialDate) {
        return new Date((long) initialDate * 1000);
    }

    public static Date getGreaterDate(Date date1, Date date2) {
        return date1.after(date2) ? date1 : date2;
    }

}
