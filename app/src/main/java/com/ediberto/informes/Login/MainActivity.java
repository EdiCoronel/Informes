package com.ediberto.informes.Login;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ediberto.informes.BasedeDatos.DatabaseHelper;
import com.ediberto.informes.R;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private EditText editTextName, editTextEmail, editTextPassword;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Agregar Usuarios"); // Establecer título de la Toolbar

        editTextName = findViewById(R.id.editTextName);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextEmail = findViewById(R.id.editTextEmail);
        Button buttonAdd = findViewById(R.id.buttonAdd);
        Button buttonView = findViewById(R.id.buttonView);
        dbHelper = new DatabaseHelper(this);

        buttonAdd.setOnClickListener(v -> {
            String name = editTextName.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            String email = editTextEmail.getText().toString().trim();
            String defaultAccessLevel = "Basico";

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(MainActivity.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(MainActivity.this, "Por favor, introduce un correo electrónico válido", Toast.LENGTH_SHORT).show();
                return;
            }

            dbHelper.addUser (new User(0, name, email, password, defaultAccessLevel));
            clearFields();
            Toast.makeText(MainActivity.this, "Usuario agregado con éxito", Toast.LENGTH_SHORT).show();
        });

        buttonView.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, UserListActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) { // Este es el ID del botón de navegación hacia arriba
            // Regresar a LoginActivity
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // Opcional: finalizar la actividad actual
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void clearFields() {
        editTextName.setText("");
        editTextEmail.setText("");
        editTextPassword.setText("");
    }
}