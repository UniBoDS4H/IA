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
    public String year(){
        return String.valueOf(today.getYear());
    }
    public String month(){
        return  today.getMonthValue() <= 9 ?  "0" + String.valueOf(today.getMonthValue()) : String.valueOf(today.getMonthValue());
    }
    public String day(){
        return  today.getDayOfMonth() <= 9 ?  "0" + String.valueOf(today.getDayOfMonth()) : String.valueOf(today.getDayOfMonth());
    }
    public String hour(){
        return this.now.getHour() <= 9 ?  "0" + String.valueOf(this.now.getHour()) : String.valueOf(this.now.getHour());
    }
    public String minute(){
         return this.now.getMinute() <= 9 ?  "0" + String.valueOf(this.now.getMinute()) : String.valueOf(this.now.getMinute());
    }
    public String seconds(){
        return this.now.getSecond() <= 9 ? "0" + String.valueOf(this.now.getSecond()) : String.valueOf(this.now.getSecond());
    }
}
