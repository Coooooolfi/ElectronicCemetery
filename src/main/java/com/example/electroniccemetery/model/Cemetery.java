package com.example.electroniccemetery.model;

public class Cemetery {
    private final int id;
    private final String name;
    private final String address;

    public Cemetery(int id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getAddress() { return address; }
}
