package com.ediberto.informes.Reportes;

public class Report {
    private int id;
    private String date;
    private String location;
    private String description;
    private String observations;
    private String startTime;
    private String endTime;
    private byte[] imageBytes;

    public Report(int id, String date, String location, String description, String observations, String startTime, String endTime, byte[] imageBytes) {
        this.id = id;
        this.date = date;
        this.location = location;
        this.description = description;
        this.observations = observations;
        this.startTime = startTime;
        this.endTime = endTime;
        this.imageBytes = imageBytes;
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
