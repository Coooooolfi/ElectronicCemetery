package com.example.electroniccemetery.model;

import java.time.LocalDate;

public class Deceased {
    private final int id;
    private final int graveId;
    private final String lastName;
    private final String firstName;
    private final String othestvo;
    private final LocalDate birthDate;
    private final LocalDate deathDate;
    private final String description;
    private final String photoUrl;

    public Deceased(int id, int graveId, String lastName, String firstName, String othestvo,
                    LocalDate birthDate, LocalDate deathDate, String description, String photoUrl) {
        this.id = id;
        this.graveId = graveId;
        this.lastName = lastName;
        this.firstName = firstName;
        this.othestvo = othestvo;
        this.birthDate = birthDate;
        this.deathDate = deathDate;
        this.description = description;
        this.photoUrl = photoUrl;
    }

    public int getId() { return id; }
    public int getGraveId() { return graveId; }
    public String getLastName() { return lastName; }
    public String getFirstName() { return firstName; }
    public String getOthestvo() { return othestvo; }
    public LocalDate getBirthDate() { return birthDate; }
    public LocalDate getDeathDate() { return deathDate; }
    public String getDescription() { return description; }
    public String getPhotoUrl() { return photoUrl; }

    public String getFullName() {
        return (lastName + " " + firstName + " " + (othestvo == null ? "" : othestvo)).trim();
    }
}
