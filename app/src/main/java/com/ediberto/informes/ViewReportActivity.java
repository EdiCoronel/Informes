package com.ediberto.informes;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ViewReportActivity extends AppCompatActivity {

    private TextView dateTextView;
    private EditText locationEditText;
    private EditText descriptionEditText;
    private EditText observationsEditText;
    private EditText startTimeEditText;
    private EditText endTimeEditText;
    private Button updateButton;
    private Button downloadButton;
    private RecyclerView recyclerView;

    private DailyReportDatabaseHelper dbHelper;
    private ArrayList<Report> reportList;
    private ReportAdapter reportAdapter;
    private int reportId; // Para identificar el informe

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_report);

        // Inicializa los elementos de la interfaz
        dateTextView = findViewById(R.id.dateTextView);
        locationEditText = findViewById(R.id.locationEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        observationsEditText = findViewById(R.id.observationsEditText);
        startTimeEditText = findViewById(R.id.startTimeEditText);
        endTimeEditText = findViewById(R.id.endTimeEditText);
        updateButton = findViewById(R.id.updateButton);
        downloadButton = findViewById(R.id.downloadButton);
        recyclerView = findViewById(R.id.recyclerView);

        dbHelper = new DailyReportDatabaseHelper(this);
        reportList = new ArrayList<>();
        reportAdapter = new ReportAdapter(reportList, new ReportAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Report report) {
                reportId = report.getId(); // Guarda el ID del reporte seleccionado
                loadReport(reportId); // Carga los detalles del reporte
            }
        });

        recyclerView.setAdapter(reportAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateReport();
            }
        });

        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadReportAsPDF();
            }
        });

        // Cargar todos los reportes al iniciar la actividad (si es necesario)
        loadAllReports();
    }

    private void loadAllReports() {
        Cursor cursor = dbHelper.getAllReports(); // Asegúrate de que este método esté implementado en tu DailyReportDatabaseHelper
        reportList.clear(); // Limpiar la lista antes de cargar nuevos datos
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String reportDate = cursor.getString(cursor.getColumnIndex("date"));
                String location = cursor.getString(cursor.getColumnIndex("location"));
                // Agregar otros campos según sea necesario
                reportList.add(new Report(id, reportDate, location)); // Asegúrate de que el constructor de Report tenga los parámetros correctos
            } while (cursor.moveToNext());
            cursor.close();
        } else {
            Toast.makeText(this, "No se encontraron reportes", Toast.LENGTH_SHORT).show();
        }
        reportAdapter.notifyDataSetChanged(); // Notificar al adaptador que los datos han cambiado
    }

    private void loadReport(int reportId) {
        Cursor cursor = dbHelper.getReportById(reportId);
        if (cursor != null && cursor.moveToFirst()) {
            dateTextView.setText(cursor.getString(cursor.getColumnIndex("date")));
            locationEditText.setText(cursor.getString(cursor.getColumnIndex("location")));
            descriptionEditText.setText(cursor.getString(cursor.getColumnIndex("description")));
            observationsEditText.setText(cursor.getString(cursor.getColumnIndex("observations")));
            startTimeEditText.setText(cursor.getString(cursor.getColumnIndex("start_time")));
            endTimeEditText.setText(cursor.getString(cursor.getColumnIndex("end_time")));
            cursor.close();
        } else {
            Toast.makeText(this, "Informe no encontrado", Toast.LENGTH_SHORT).show();
            finish(); // Cierra la actividad si no se encuentra el informe
        }
    }

    private void updateReport() {
        String location = locationEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String observations = observationsEditText.getText().toString().trim();
        String startTime = startTimeEditText.getText().toString().trim();
        String endTime = endTimeEditText.getText().toString().trim();

        // Actualiza el informe en la base de datos
        dbHelper.updateDailyReport(reportId, location, description, observations, startTime, endTime);
        Toast.makeText(this, "Informe actualizado", Toast.LENGTH_SHORT).show();

        // Carga de nuevo todos los reportes para reflejar los cambios
        loadAllReports();

        // Limpia los campos de entrada
        clearInputFields();
    }

    private void clearInputFields() {
        locationEditText.setText("");
        descriptionEditText.setText("");
        observationsEditText.setText("");
        startTimeEditText.setText("");
        endTimeEditText.setText("");
    }

    private void downloadReportAsPDF() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            return;
        }

        String date = dateTextView.getText().toString().trim();
        String location = locationEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String observations = observationsEditText.getText().toString().trim();
        String startTime = startTimeEditText.getText().toString().trim();
        String endTime = endTimeEditText.getText().toString().trim();

        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        paint.setTextSize(12);
        canvas.drawText("Fecha: " + date, 10, 25, paint);
        canvas.drawText("Ubicación: " + location, 10, 50, paint);
        canvas.drawText("Descripción: " + description, 10, 75, paint);
        canvas.drawText("Observaciones: " + observations, 10, 100, paint);
        canvas.drawText("Hora de Inicio: " + startTime, 10, 125, paint);
        canvas.drawText("Hora de Fin: " + endTime, 10, 150, paint);

        pdfDocument.finishPage(page);

        String directoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getPath();
        File filePath = new File(directoryPath, "Reporte_" + reportId + ".pdf");

        try {
            pdfDocument.writeTo(new FileOutputStream(filePath));
            Toast.makeText(this, "Reporte descargado: " + filePath.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.e("PDFDownload", "Error al guardar el PDF: " + e.getMessage());
            Toast.makeText(this, "Error al descargar el reporte", Toast.LENGTH_SHORT).show();
        } finally {
            pdfDocument.close();
        }
    }
}
