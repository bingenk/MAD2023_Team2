package sg.edu.np.mad.mad2023_team2.ui.LoginSignup;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import sg.edu.np.mad.mad2023_team2.R;
import android.widget.EditText;

public class Signup extends AppCompatActivity {

    private EditText emailEditText, usernameEditText, passwordEditText;
    private Button signupButton;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        // Get references to the EditText fields and signup button
        emailEditText = findViewById(R.id.signup_email);
        usernameEditText = findViewById(R.id.signup_username);
        passwordEditText = findViewById(R.id.signup_password);
        signupButton = findViewById(R.id.Signupbutton);

        // Set click listener for the signup button
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user input from EditText fields
                String email = emailEditText.getText().toString().trim();
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                // Create a new HelperClass instance with the user input (HelperClass.java)
                HelperClass user = new HelperClass(email, username, password);

                // Generate a new unique key for the user (Extra for other features use)
                String userId = databaseReference.push().getKey();

                // Store the user information in Firebase Realtime Database
                databaseReference.child(userId).setValue(user);

                // Display successfully registered message
                Toast.makeText(Signup.this, "User registered successfully!", Toast.LENGTH_SHORT).show();

                // Clear the EditText fields
                emailEditText.setText("");
                usernameEditText.setText("");
                passwordEditText.setText("");
            }
        });
    }
}






