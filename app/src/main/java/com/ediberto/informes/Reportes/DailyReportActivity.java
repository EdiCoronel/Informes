package com.ediberto.informes.Reportes;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ediberto.informes.BasedeDatos.DailyReportDatabaseHelper;
import com.ediberto.informes.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DailyReportActivity extends AppCompatActivity {

    private TextView dateTextView;
    private EditText locationEditText;
    private EditText descriptionEditText;
    private EditText observationsEditText;
    private EditText startTimeEditText;
    private EditText endTimeEditText;
    private Button submitButton;
    private Button viewReportButton;
    private DailyReportDatabaseHelper dbHelper; // Nueva instancia de la base de datos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_report);

        // Configurar la Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Habilitar el botón de retroceso
        getSupportActionBar().setTitle("Cargar Reportes"); // Establecer título de la Toolbar

        dateTextView = findViewById(R.id.dateTextView);
        locationEditText = findViewById(R.id.locationEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        observationsEditText = findViewById(R.id.observationsEditText);
        startTimeEditText = findViewById(R.id.startTimeEditText);
        endTimeEditText = findViewById(R.id.endTimeEditText);
        submitButton = findViewById(R.id.submitButton);
        viewReportButton = findViewById(R.id.viewReportButton);
        dbHelper = new DailyReportDatabaseHelper(this);

        // Establecer la fecha actual
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        dateTextView.setText(currentDate);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String location = locationEditText.getText().toString().trim();
                String description = descriptionEditText.getText().toString().trim();
                String observations = observationsEditText.getText().toString().trim();
                String startTime = startTimeEditText.getText().toString().trim();
                String endTime = endTimeEditText.getText().toString().trim();

                // Agregar el informe diario a la base de datos
                dbHelper.addDailyReport(currentDate, location, description, observations, startTime, endTime);
                Toast.makeText(DailyReportActivity.this, "Informe diario guardado", Toast.LENGTH_SHORT).show();

                // Limpiar los campos después de guardar
                locationEditText.setText("");
                descriptionEditText.setText("");
                observationsEditText.setText("");
                startTimeEditText.setText("");
                endTimeEditText.setText("");
            }
        });

        viewReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Asume que tienes el ID del informe que deseas ver
                int reportId = 1; // Cambia esto según la lógica de tu aplicación
                Intent intent = new Intent(DailyReportActivity.this, ViewReportActivity.class);
                intent.putExtra("REPORT_ID", reportId); // Pasar el ID del informe
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Manejar el clic del botón de retroceso
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // Volver a la actividad anterior
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}