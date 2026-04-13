package com.example.electroniccemetery.model;

public class Section {
    private final int id;
    private final int cemeteryId;
    private final int number;

    public Section(int id, int cemeteryId, int number) {
        this.id = id;
        this.cemeteryId = cemeteryId;
        this.number = number;
    }

    public int getId() { return id; }
    public int getCemeteryId() { return cemeteryId; }
    public int getNumber() { return number; }
}
