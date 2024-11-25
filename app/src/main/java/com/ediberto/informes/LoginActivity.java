package com.ediberto.informes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button goToMainButton; // Nuevo botón
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.username); // Cambia el nombre a algo más descriptivo, como nameEditText
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        goToMainButton = findViewById(R.id.goToMain);
        dbHelper = new DatabaseHelper(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = usernameEditText.getText().toString().trim(); // Cambia 'username' a 'name'
                String password = passwordEditText.getText().toString().trim();

                if (checkCredentials(name, password)) { // Cambia 'username' a 'name'
                    Toast.makeText(getApplicationContext(), "Inicio de sesión exitoso", Toast.LENGTH_LONG).show();
                    Log.d("LoginActivity", "Credenciales correctas");

                    Intent intent = new Intent(LoginActivity.this, DailyReportActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                    Log.d("LoginActivity", "Credenciales incorrectas");
                }
            }
        });

        goToMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private boolean checkCredentials(String name, String password) {
        return dbHelper.checkUser (name, password); // Verifica las credenciales en la base de datos
    }
}