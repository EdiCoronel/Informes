package com.ediberto.informes.BasedeDatos;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DailyReportDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "daily_reports.db";
    private static final int DATABASE_VERSION = 2;
    private static final String TABLE_REPORTS = "daily_reports";

    // Columnas de la tabla
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_LOCATION = "location";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_OBSERVATIONS = "observations";
    private static final String COLUMN_START_TIME = "start_time";
    private static final String COLUMN_END_TIME = "end_time";
    private static final String COLUMN_IMAGE = "imageBytes";

    public DailyReportDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_REPORTS_TABLE = "CREATE TABLE " + TABLE_REPORTS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_DATE + " TEXT,"
                + COLUMN_LOCATION + " TEXT,"
                + COLUMN_DESCRIPTION + " TEXT,"
                + COLUMN_OBSERVATIONS + " TEXT,"
                + COLUMN_START_TIME + " TEXT,"
                + COLUMN_END_TIME + " TEXT,"
                + COLUMN_IMAGE + " BLOB" + ")";
        db.execSQL(CREATE_REPORTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REPORTS);
        onCreate(db);
    }

    public void deleteReport(int reportId) {
        // Abre la base de datos en modo escritura
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            // Elimina el informe con el ID especificado
            db.delete(TABLE_REPORTS, COLUMN_ID + " = ?", new String[]{String.valueOf(reportId)});
        }
        catch (Exception e) {
            Log.e("DBError", "Error al eliminar el informe: " + e.getMessage());
            throw e; // Vuelve a lanzar la excepción si es necesario
        }
        // Asegúrate de cerrar la base de datos
    }

    // Método para agregar un informe diario
    public void addDailyReport(String date, String location, String description, String observations, String startTime, String endTime, byte[] imageBytes) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_LOCATION, location);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_OBSERVATIONS, observations);
        values.put(COLUMN_START_TIME, startTime);
        values.put(COLUMN_END_TIME, endTime);
        values.put(COLUMN_IMAGE, imageBytes);
        db.insert(TABLE_REPORTS, null, values);

        db.close();
    }

    // Método para obtener todos los informes diarios (si es necesario)
    public Cursor getAllReports() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_REPORTS, null, null, null, null, null, null);
    }

    // Método para obtener un informe diario por ID
    public Cursor getReportById(int reportId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_REPORTS + " WHERE " + COLUMN_ID + " = ?";
        return db.rawQuery(query, new String[]{String.valueOf(reportId)});
    }

    // Método para actualizar un informe diario
    public void updateDailyReport(int reportId, String location, String description,
                                  String observations, String startTime, String endTime, byte[] imageBytes) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_LOCATION, location);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_OBSERVATIONS, observations);
        values.put(COLUMN_START_TIME, startTime);
        values.put(COLUMN_END_TIME, endTime);
        values.put(COLUMN_IMAGE, imageBytes);

        // Actualizar el informe en la base de datos
        db.update(TABLE_REPORTS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(reportId)});
        db.close();
    }

    @SuppressLint("Range")
    public byte[] getImageByReportId(int reportId) {
        SQLiteDatabase db = this.getReadableDatabase();
        byte[] imageBytes = null;

        String query = "SELECT imageBytes FROM " + TABLE_REPORTS + " WHERE id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(reportId)});

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                if (!cursor.isNull(cursor.getColumnIndex("imageBytes"))) {
                    imageBytes = cursor.getBlob(cursor.getColumnIndex("imageBytes"));
                } else {
                    Log.d("getImageByReportId", "La imagen es nula");
                }
            } else {
                Log.d("getImageByReportId", "No se encontró ningún registro");
            }
            cursor.close();
        } else {
            Log.d("getImageByReportId", "Error al ejecutar la consulta");
        }
        return imageBytes;
    }

}