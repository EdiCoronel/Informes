package com.ediberto.informes.Reportes;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.provider.MediaStore;
import com.ediberto.informes.BasedeDatos.DailyReportDatabaseHelper;
import com.ediberto.informes.R;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DailyReportActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int PICK_IMAGE = 2;

    private TextView dateTextView;
    private EditText locationEditText;
    private EditText descriptionEditText;
    private EditText observationsEditText;
    private EditText startTimeEditText;
    private EditText endTimeEditText;
    private Button submitButton;
    private Button viewReportButton;
    private Button buttonSelectPhoto;
    private ImageView imageView;
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
        buttonSelectPhoto = findViewById(R.id.buttonSelectPhoto);
        imageView = findViewById(R.id.imageView);

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
                imageView.setImageDrawable(null);
            }
        });

        viewReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Asume que tienes el ID del informe que deseas ver
                int reportId = 1;
                Intent intent = new Intent(DailyReportActivity.this, ViewReportActivity.class);
                intent.putExtra("REPORT_ID", reportId); // Pasar el ID del informe
                startActivity(intent);
            }
        });

        buttonSelectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhotoIntent, PICK_IMAGE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                imageView.setImageBitmap(imageBitmap);
            } else if (requestCode == PICK_IMAGE) {
                Uri selectedImageUri = data.getData();
                imageView.setImageURI(selectedImageUri);
            }
        }
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