package com.ediberto.informes.Login;

public class User {
    private int id;
    private String name;
    private String email;
    private String password;
    private String accessLevel;

    public User(int id, String name, String email, String password, String accessLevel) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.accessLevel = accessLevel;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() { // Getter para la contraseña
        return password;
    }

    public void setPassword(String password) { // Setter para la contraseña
        this.password = password;
    }

    public String getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(String accessLevel) {
        this.accessLevel = accessLevel;
    }

    @Override
    public String toString() {
        return name + " (" + email + ", Nivel de acceso: " + accessLevel + ")";
    }
}