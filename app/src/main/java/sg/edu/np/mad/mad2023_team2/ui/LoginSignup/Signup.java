package sg.edu.np.mad.mad2023_team2.ui.LoginSignup;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import sg.edu.np.mad.mad2023_team2.R;
import android.widget.EditText;


public class Signup extends AppCompatActivity {

    private EditText emailEditText, usernameEditText, passwordEditText;
    private Button signupButton;
    private DatabaseReference databaseReference;
    TextView loginRedirectText;

    //Validation for email format
    private boolean isValidEmail(String email) {
        // Email validation regex pattern
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        return email.matches(emailPattern);
    }

    //Validation for password format (validate improper characters in password)
    private boolean isValidPassword(String password) {
        // Password validation regex pattern
        String passwordPattern = "^(?=.*[a-zA-Z]).+$";

        // Check for invalid characters
        if (password.contains(",") || password.contains(".") || password.contains("_")
                || password.contains("-") || password.contains("!") || password.contains("?")) {
            return false;
        }

        // Check password format using regex pattern
        return password.matches(passwordPattern);
    }

    //Validation for too long username
    private boolean isValidUsername(String username) {
        // Define the maximum length of the username
        int maxLength = 15;

        // Check if the username length is within the allowed limit
        return username.length() <= maxLength;
    }

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
        loginRedirectText = findViewById(R.id.RedirectLogin);

        // Set click listener for the signup button
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user input from EditText fields
                String email = emailEditText.getText().toString().trim();
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                // Validation message for email format
                if (!isValidEmail(email)) {
                    emailEditText.setError("Invalid email format");
                    emailEditText.requestFocus();
                    return;
                }

                // Validation message for password format
                if (!isValidPassword(password)) {
                    passwordEditText.setError("Invalid password format. Please avoid the following:\n" +
                            "- Using commas (,)\n" +
                            "- Using periods (.)\n" +
                            "- Using underscores (_)\n" +
                            "- Using dashes (-)\n" +
                            "- Using exclamation marks (!)\n" +
                            "- Using question marks (?)\n" +
                            "- Ensure your password contains at least one alphabet character.");
                    passwordEditText.requestFocus();
                    return;
                }

                // Validation message for username length
                if (!isValidUsername(username)) {
                    usernameEditText.setError("Username is too long!");
                    usernameEditText.requestFocus();
                    return;
                }


                if(email.isEmpty() || username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(Signup.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();

                } else {
                    // Create a new HelperClass instance with the user input (HelperClass.java)
                    HelperClass user = new HelperClass(email, username, password);

                    // Generate a new unique key for the user (Extra for other features use)
                    String userId = databaseReference.push().getKey();

                    // Store the user information in Firebase Realtime Database
                    databaseReference.child(userId).setValue(user);

                    // Display successfully registered message
                    Toast.makeText(Signup.this, "You have sign up successfully!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Signup.this, Login.class);
                    startActivity(intent);

                    // Clear the EditText fields
                    emailEditText.setText("");
                    usernameEditText.setText("");
                    passwordEditText.setText("");
                }
            }
        });

        //Direct user to login if user already have an account
        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Signup.this, Login.class);
                startActivity(intent);
            }
        });
    }
}






