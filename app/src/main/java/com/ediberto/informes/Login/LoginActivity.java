package com.ediberto.informes.Login;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.ediberto.informes.BasedeDatos.DatabaseHelper;
import com.ediberto.informes.Reportes.DailyReportActivity;
import com.ediberto.informes.R;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button goToMainButton;
    private ImageView togglePasswordVisibility; // Ícono para mostrar/ocultar contraseña
    private boolean isPasswordVisible = false; // Estado de visibilidad de la contraseña
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        goToMainButton = findViewById(R.id.goToMain);
        togglePasswordVisibility = findViewById(R.id.togglePassword); // Vincula el ImageView del layout
        dbHelper = new DatabaseHelper(this);

        // Manejar el clic del ícono de visibilidad
        togglePasswordVisibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPasswordVisible) {
                    // Cambia a texto oculto
                    passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    togglePasswordVisibility.setImageResource(R.drawable.ic_visibility_off); // Cambiar al ícono de ocultar
                } else {
                    // Cambia a texto visible
                    passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    togglePasswordVisibility.setImageResource(R.drawable.ic_visibility); // Cambiar al ícono de mostrar
                }
                // Mantener el cursor al final del texto
                passwordEditText.setSelection(passwordEditText.length());
                isPasswordVisible = !isPasswordVisible; // Alternar el estado
            }
        });

        // Manejar el clic en el botón de inicio de sesión
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (checkCredentials(name, password)) {
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

        // Manejar el clic en el botón para ir al registro
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
        return dbHelper.checkUser(name, password); // Verifica las credenciales en la base de datos
    }
}