package com.ediberto.informes.Login;

import androidx.annotation.NonNull;

public class User {
    private final int id;
    private final String name;
    private final String email;
    private final String password;
    private final String accessLevel;

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

    public String getAccessLevel() {
        return accessLevel;
    }

    @NonNull
    @Override
    public String toString() {
        return name + " (" + email + ", Nivel de acceso: " + accessLevel + ")";
    }
}