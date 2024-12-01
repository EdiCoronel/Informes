package com.ediberto.informes.BasedeDatos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ediberto.informes.Login.User;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "users.db";
    private static final int DATABASE_VERSION = 3; // Aumenta la versión si es necesario
    private static final String TABLE_USERS = "users";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "email TEXT, " +
                "password TEXT, " +
                "access_level TEXT)";
        db.execSQL(CREATE_USERS_TABLE);

        // Agrega un usuario administrador predeterminado
        addDefaultAdminUser (db);
    }

@Override
public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    if (oldVersion < 2) {
        db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN password TEXT"); // Agrega la columna de contraseña
        // Verifica si hay un administrador y crea uno si no existe
        if (getAdminCount() == 0) {
            addDefaultAdminUser (db);
        }
    }
}

    // Método para agregar un usuario administrador predeterminado
    private void addDefaultAdminUser(SQLiteDatabase db) {
        // Verifica si ya existe un usuario con nivel de acceso 'Administrador'
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE access_level = ?",
                new String[]{"Administrador"});
        if (cursor.moveToFirst()) {
            cursor.close(); // Si existe, no hacer nada
            return;
        }
        cursor.close();

        // Insertar el usuario administrador si no existe
        ContentValues adminUser = new ContentValues();
        adminUser.put("name", "admin");
        adminUser.put("email", "admin@example.com");
        adminUser.put("password", "admin123"); // Usa un hash seguro en producción
        adminUser.put("access_level", "Administrador");
        db.insert(TABLE_USERS, null, adminUser);
    }

    public boolean checkUser(String name, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        try (Cursor cursor = db.query(TABLE_USERS, new String[]{"id"}, "name=? AND password=?", new String[]{name, password}, null, null, null)) {
            return cursor != null && cursor.getCount() > 0;
        }
    }

    // CRUD Operations
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", user.getName());
        values.put("email", user.getEmail());
        values.put("password", user.getPassword());
        values.put("access_level", user.getAccessLevel());
        db.insert(TABLE_USERS, null, values);
        db.close();
    }

    public User getUser(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{"id", "name", "email", "password", "access_level"}, "id=?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            User user = new User(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
            cursor.close();
            return user;
        }
        return null;
    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_USERS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                User user = new User(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return userList;
    }

    public void updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", user.getName());
        values.put("email", user.getEmail());
        values.put("password", user.getPassword());
        values.put("access_level", user.getAccessLevel());
        db.update(TABLE_USERS, values, "id=?", new String[]{String.valueOf(user.getId())});
        db.close();
    }

    public void deleteUser(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Verifica si el usuario es administrador
        User user = getUser(id);
        if (user != null && "Administrador".equals(user.getAccessLevel())) {
            // Verifica que haya al menos otro administrador en la base de datos
            int adminCount = getAdminCount();
            if (adminCount <= 1) {
                // No permite eliminar al último administrador
                throw new IllegalStateException("No puedes eliminar el único usuario administrador.");
            }
        }

        db.delete(TABLE_USERS, "id=?", new String[]{String.valueOf(id)});
        db.close();
    }

    // Obtener el número de administradores en la base de datos
    public int getAdminCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_USERS + " WHERE access_level = ?", new String[]{"Administrador"});
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }
}