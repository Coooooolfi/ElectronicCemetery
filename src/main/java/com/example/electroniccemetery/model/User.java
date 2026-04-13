package com.example.electroniccemetery.model;

public class User {
    private final int id;
    private final int roleId;
    private final String lastName;
    private final String firstName;
    private final String othestvo;
    private final String login;
    private final String password;
    private final Integer cemeteryId;
    private final String roleName;

    public User(int id, int roleId, String lastName, String firstName, String othestvo,
                String login, String password, Integer cemeteryId, String roleName) {
        this.id = id;
        this.roleId = roleId;
        this.lastName = lastName;
        this.firstName = firstName;
        this.othestvo = othestvo;
        this.login = login;
        this.password = password;
        this.cemeteryId = cemeteryId;
        this.roleName = roleName;
    }

    public int getId() { return id; }
    public int getRoleId() { return roleId; }
    public String getLastName() { return lastName; }
    public String getFirstName() { return firstName; }
    public String getOthestvo() { return othestvo; }
    public String getLogin() { return login; }
    public String getPassword() { return password; }
    public Integer getCemeteryId() { return cemeteryId; }
    public String getRoleName() { return roleName; }

    public String getFullName() {
        return (lastName + " " + firstName + " " + (othestvo == null ? "" : othestvo)).trim();
    }
}