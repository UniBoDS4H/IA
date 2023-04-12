package com.ds4h.model.util;

import java.time.LocalDate;
import java.time.LocalTime;
public class DateTime {
    private final LocalDate today;
    private final LocalTime now;
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
        return  today.getMonthValue() <= 9 ?  "0" + String.valueOf(today.getMonthValue()) : String.valueOf(today.getMonthValue());
    }

    /**
     * Returns the current day.
     * @return the current day.
     */
    public String day(){
        return  today.getDayOfMonth() <= 9 ?  "0" + String.valueOf(today.getDayOfMonth()) : String.valueOf(today.getDayOfMonth());
    }

    /**
     * Returns the current hour.
     * @return the current hour.
     */
    public String hour(){
        return this.now.getHour() <= 9 ?  "0" + String.valueOf(this.now.getHour()) : String.valueOf(this.now.getHour());
    }

    /**
     * Returns the current minute.
     * @return the current minute.
     */
    public String minute(){
         return this.now.getMinute() <= 9 ?  "0" + String.valueOf(this.now.getMinute()) : String.valueOf(this.now.getMinute());
    }

    /**
     * Returns the current seconds.
     * @return the current seconds.
     */
    public String seconds(){
        return this.now.getSecond() <= 9 ? "0" + String.valueOf(this.now.getSecond()) : String.valueOf(this.now.getSecond());
    }
}
