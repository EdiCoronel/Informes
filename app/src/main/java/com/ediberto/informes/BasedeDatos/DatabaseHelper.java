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
    private static final int DATABASE_VERSION = 2; // Aumenta la versión si es necesario
    private static final String TABLE_USERS = "users";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, email TEXT, password TEXT, access_level TEXT)";
        db.execSQL(CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN password TEXT"); // Agrega la columna de contraseña
        }
    }

    public boolean checkUser (String name, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        try (Cursor cursor = db.query(TABLE_USERS, new String[]{"id"}, "name=? AND password=?", new String[]{name, password}, null, null, null)) {
            return cursor != null && cursor.getCount() > 0;
        }
    }

    // CRUD Operations
    public void addUser  (User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", user.getName());
        values.put("email", user.getEmail());
        values.put("password", user.getPassword()); // Agregar la contraseña a la base de datos
        values.put("access_level", user.getAccessLevel()); // Agregar el nivel de acceso como String
        db.insert(TABLE_USERS, null, values);
        db.close();
    }

    public User getUser  (int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{"id", "name", "email", "password", "access_level"}, "id=?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            User user = new User(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4)); // Obtener la contraseña y el nivel de acceso
            cursor.close();
            return user;
        }
        return null;
    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_USERS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                User user = new User(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4)); // Obtener la contraseña y el nivel de acceso
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return userList;
    }

    public void updateUser  (User  user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", user.getName());
        values.put("email", user.getEmail());
        values.put("password", user.getPassword()); // Agregar la contraseña a la base de datos
        values.put("access_level", user.getAccessLevel()); // Agregar el nivel de acceso como String
        db.update(TABLE_USERS, values, "id=?", new String[]{String.valueOf(user.getId())});
        db.close();
    }

    public void deleteUser  (int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USERS, "id=?", new String[]{String.valueOf(id)});
        db.close();
    }
}