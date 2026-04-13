package com.example.electroniccemetery.model;

public class Grave {
    private final int id;
    private final int plotId;
    private final int number;
    private final String photoUrl;

    public Grave(int id, int plotId, int number, String photoUrl) {
        this.id = id;
        this.plotId = plotId;
        this.number = number;
        this.photoUrl = photoUrl;
    }

    public int getId() { return id; }
    public int getPlotId() { return plotId; }
    public int getNumber() { return number; }
    public String getPhotoUrl() { return photoUrl; }
}
