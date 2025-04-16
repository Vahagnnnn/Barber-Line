package com.vahagn.barber_line.Classes;

public class TimeRange {
    private String open;
    private String close;


public  TimeRange()
{
    this.open = "Closed";
    this.close = "Closed";
}
    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getClose() {
        return close;
    }

    public void setClose(String close) {
        this.close = close;
    }
}

