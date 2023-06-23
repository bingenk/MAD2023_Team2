package sg.edu.np.mad.mad2023_team2.ui.LoginSignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import sg.edu.np.mad.mad2023_team2.R;
import android.widget.EditText;


public class Signup extends AppCompatActivity {

    //Create class/private instance variables
    private EditText emailEditText, usernameEditText, passwordEditText;
    private Button signupButton;
    private DatabaseReference databaseReference;
    TextView loginRedirectText;
    private FirebaseAuth firebaseAuth;

    //Validation for email format
    private boolean isValidEmail(String email) {
        // Email validation regex pattern
        String emailPattern = "^[a-z0-9._-]+@[a-z]+\\.[a-z]+$";

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

    //Validation for existing email in database upon user signup
    private void isEmailExists(final String email, final EmailExistsCallback callback) {
        Query query = databaseReference.orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean emailExists = dataSnapshot.exists();
                callback.onCallback(emailExists);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onCallback(false);
            }
        });
    }

    // Define a callback interface for handling the email existing result
    private interface EmailExistsCallback {
        void onCallback(boolean emailExists);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        // Initialize Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance();

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

                // Validation message for checking existing registered email
                isEmailExists(email, new EmailExistsCallback() {
                    @Override
                    public void onCallback(boolean emailExists) {
                        if (emailExists) {
                            emailEditText.setError("Email has already registered!");
                            emailEditText.requestFocus();
                        } else {
                            // Check if there are unfilled columns
                            if (email.isEmpty() || username.isEmpty() || password.isEmpty()) {
                                Toast.makeText(Signup.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            // Validation message for email format
                            if (!isValidEmail(email)) {
                                emailEditText.setError("Invalid email format\n" +
                                        "**Note** Only small case letters allowed.");
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

                            // Create a new HelperClass instance with the user input
                            HelperClass user = new HelperClass(email, username, password);

                            // Create a user in Firebase Authentication
                            firebaseAuth.createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        //Storing user data in both Firebase Realtime Database and Firebase Authentication
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                // Get the unique user ID generated by Firebase Authentication
                                                String userId = task.getResult().getUser().getUid();

                                                // Store the user information in Firebase Realtime Database
                                                databaseReference.child(username).setValue(user);

                                                // Display successfully registered message
                                                Toast.makeText(Signup.this, "You have signed up successfully!", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(Signup.this, Login.class);
                                                startActivity(intent);

                                                // Clear the EditText fields
                                                emailEditText.setText("");
                                                usernameEditText.setText("");
                                                passwordEditText.setText("");
                                            } else {
                                                // Registration failed
                                                Toast.makeText(Signup.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                    }
                });
            }
        });

        // Direct user to login if the user already has an account
        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Signup.this, Login.class);
                startActivity(intent);
            }
        });
    }
}






