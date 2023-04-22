package com.ds4h.model.util.directoryManager;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * This class is used in order to obtain the Date and the Time that will be used for the creation of Directories.
 */
public class DateTime {
    private final LocalDate today;
    private final LocalTime now;

    /**
     * Constructor for the DateTime object.
     */
    public DateTime(){
        today = LocalDate.now();
        this.now = LocalTime.now();
    }

    /**
     * Returns the current year.
     * @return the current year.
     */
    public String year(){
        return String.valueOf(today.getYear());
    }

    /**
     * Returns the current month.
     * @return the current month.
     */
    public String month(){
        return  today.getMonthValue() <= 9 ?  "0" + today.getMonthValue() : String.valueOf(today.getMonthValue());
    }

    /**
     * Returns the current day.
     * @return the current day.
     */
    public String day(){
        return  today.getDayOfMonth() <= 9 ?  "0" + today.getDayOfMonth() : String.valueOf(today.getDayOfMonth());
    }

    /**
     * Returns the current hour.
     * @return the current hour.
     */
    public String hour(){
        return this.now.getHour() <= 9 ?  "0" + this.now.getHour() : String.valueOf(this.now.getHour());
    }

    /**
     * Returns the current minute.
     * @return the current minute.
     */
    public String minute(){
         return this.now.getMinute() <= 9 ?  "0" + this.now.getMinute() : String.valueOf(this.now.getMinute());
    }

    /**
     * Returns the current seconds.
     * @return the current seconds.
     */
    public String seconds(){
        return this.now.getSecond() <= 9 ? "0" + this.now.getSecond() : String.valueOf(this.now.getSecond());
    }
}
