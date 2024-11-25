package com.ediberto.informes;

public class Report {
    private int id;
    private String date;
    private String location;

    public Report(int id, String date, String location) {
        this.id = id;
        this.date = date;
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }
}
