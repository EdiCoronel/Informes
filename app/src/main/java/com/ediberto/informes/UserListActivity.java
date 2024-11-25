package com.ediberto.informes;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;
import androidx.appcompat.widget.Toolbar;

public class UserListActivity extends AppCompatActivity {
    private ListView listViewUsers;
    private EditText editTextName, editTextEmail;
    private Button buttonUpdate, buttonDelete;
    private DatabaseHelper dbHelper;
    private List<User> userList;
    private int selectedUserId = -1; // Para almacenar el ID del usuario seleccionado
    private Spinner spinnerAccessLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        listViewUsers = findViewById(R.id.listViewUsers);
        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        spinnerAccessLevel = findViewById(R.id.spinnerAccessLevel);
        buttonUpdate = findViewById(R.id.buttonUpdate);
        buttonDelete = findViewById(R.id.buttonDelete);
        dbHelper = new DatabaseHelper(this);

        loadUsers();

        // Configura el Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.accesslevels, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAccessLevel.setAdapter(adapter);

        listViewUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User user = userList.get(position);
                selectedUserId = user.getId(); // Guardar el ID del usuario seleccionado
                editTextName.setText(user.getName()); // Cargar el nombre en el EditText
                editTextEmail.setText(user.getEmail()); // Cargar el correo en el EditText

                // Establecer el nivel de acceso en el Spinner
                String accessLevel = user.getAccessLevel();
                int spinnerPosition = adapter.getPosition(accessLevel);
                spinnerAccessLevel.setSelection(spinnerPosition); // Establecer el nivel de acceso en el Spinner
            }
        });

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedUserId != -1) { // Asegurarse de que un usuario esté seleccionado
                    String name = editTextName.getText().toString().trim(); // Obtener el nombre del EditText
                    String email = editTextEmail.getText().toString().trim(); // Obtener el correo del EditText
                    String accessLevel = spinnerAccessLevel.getSelectedItem().toString(); // Obtener el nivel de acceso seleccionado

                    // Validar que el nivel de acceso no sea el primer elemento (no válido)
                    if (accessLevel.equals("Seleccione un nivel de acceso")) {
                        Toast.makeText(UserListActivity.this, "Por favor, selecciona un nivel de acceso válido", Toast.LENGTH_SHORT).show();
                        return; // Salir del método si el nivel de acceso no es válido
                    }

                    // Obtener la contraseña del usuario seleccionado (si es necesario)
                    User currentUser  = dbHelper.getUser (selectedUserId);
                    String password = currentUser .getPassword(); // Obtener la contraseña del usuario actual

                    // Crear un nuevo objeto User con el nivel de acceso
                    User updatedUser  = new User(selectedUserId, name, email, password, accessLevel); // Usar el ID del usuario seleccionado

                    try {
                        dbHelper.updateUser (updatedUser ); // Actualizar el usuario en la base de datos
                        Toast.makeText(UserListActivity.this, "Usuario actualizado con éxito", Toast.LENGTH_SHORT).show(); // Mensaje de confirmación
                    } catch (Exception e) {
                        Toast.makeText(UserListActivity.this, "Error al actualizar el usuario", Toast.LENGTH_SHORT).show(); // Manejo de errores
                    }

                    loadUsers(); // Recargar la lista de usuarios
                    clearFields(); // Limpiar los campos
                } else {
                    Toast.makeText(UserListActivity.this, "Selecciona un usuario para actualizar", Toast.LENGTH_SHORT).show(); // Mensaje si no hay usuario seleccionado
                }
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedUserId != -1) { // Asegurarse de que un usuario esté seleccionado
                    dbHelper.deleteUser (selectedUserId); // Eliminar el usuario
                    loadUsers(); // Recargar la lista de usuarios
                    clearFields(); // Limpiar los campos
                    Toast.makeText(UserListActivity.this, "Usuario eliminado", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UserListActivity.this, "Selecciona un usuario para eliminar", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish(); // Cierra la actividad actual y vuelve a la anterior
        return true;
    }

    private void loadUsers() {
        userList = dbHelper.getAllUsers();
        ArrayAdapter<User> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userList);
        listViewUsers.setAdapter(adapter);
    }

    private void clearFields() {
        editTextName.setText("");
        editTextEmail.setText("");
        spinnerAccessLevel.setSelection(0); // Restablecer el Spinner a la primera opción
        selectedUserId = -1; // Reiniciar el ID seleccionado
    }
}