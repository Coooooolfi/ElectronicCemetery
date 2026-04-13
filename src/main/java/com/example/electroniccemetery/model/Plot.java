package com.example.electroniccemetery.model;

public class Plot {
    private final int id;
    private final int sectionId;
    private final int number;

    public Plot(int id, int sectionId, int number) {
        this.id = id;
        this.sectionId = sectionId;
        this.number = number;
    }

    public int getId() { return id; }
    public int getSectionId() { return sectionId; }
    public int getNumber() { return number; }
}
