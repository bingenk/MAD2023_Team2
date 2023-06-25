package sg.edu.np.mad.mad2023_team2.ui.LoginSignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.Objects;
import sg.edu.np.mad.mad2023_team2.ui.MainActivity;
import sg.edu.np.mad.mad2023_team2.R;

public class Login extends AppCompatActivity {

    // Create Variables
    TextInputEditText loginUsername, loginPassword;
    Button loginButton;
    TextView signupRedirectText, forgetPassword;
    CheckBox rememberMe;
    SharedPreferences sharedPreferences; // Used for storing user preferences
    PreferenceManager PreferenceManager; // Manages preferences


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize variables by finding views in the layout
        loginUsername = findViewById(R.id.login_username);
        loginPassword = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.Loginbutton);
        signupRedirectText = findViewById(R.id.RedirectSignup);
        rememberMe = findViewById(R.id.rememberMe);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this); // Obtain an instance of the SharedPreferences interface
        forgetPassword = findViewById(R.id.forgetpassword);


        // Set click listener for the login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateUsername() || !validatePassword()) {
                    // Username or password validation failed
                } else {
                    checkUser();
                }
            }
        });

        // Set click listener for the forget password text
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Redirect to the ForgetPassword activity
                Intent intent = new Intent(Login.this, ForgetPassword.class);
                startActivity(intent);
            }
        });

        // Set click listener for the signup redirect text
        signupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Redirect to the Signup activity
                Intent intent = new Intent(Login.this, Signup.class);
                startActivity(intent);
            }
        });
    }

    // Validate the username field
    public Boolean validateUsername() {
        String val = loginUsername.getText().toString();
        if (val.isEmpty()) {
            loginUsername.setError("Username cannot be empty");
            return false;
        } else {
            loginUsername.setError(null);
            return true;
        }
    }

    // Validate the password field
    public Boolean validatePassword() {
        String val = loginPassword.getText().toString();
        if (val.isEmpty()) {
            loginPassword.setError("Password cannot be empty");
            return false;
        } else {
            loginPassword.setError(null);
            return true;
        }
    }

    // Check the user's credentials in the Firebase database
    public void checkUser() {
        String userUsername = loginUsername.getText().toString().trim();
        String userPassword = loginPassword.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("username").equalTo(userUsername);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // Check Internet Connection
                if (!isConnected(Login.this)) {
                    showCustomDialog();
                }

                if (snapshot.exists()) {
                    loginUsername.setError(null);
                    String passwordFromDB = snapshot.child(userUsername).child("password").getValue(String.class);

                    if (!Objects.equals(passwordFromDB, userPassword)) {
                        loginUsername.setError(null);
                        loginPassword.setError("Invalid Credentials!");
                        loginPassword.requestFocus();
                    } else {
                        // Login successful, show a toast message
                        Toast.makeText(Login.this, "Successfully logged in", Toast.LENGTH_SHORT).show();

                        // For showing user profile
                        rememberUser(userUsername, userPassword);
                        String userEmailFromDB = snapshot.child(userUsername).child("email").getValue(String.class);

                        // Pass the user profile information to MainActivity
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        intent.putExtra("username", userUsername);
                        intent.putExtra("email", userEmailFromDB);
                        startActivity(intent);
                    }
                } else {
                    loginUsername.setError("User does not exist");
                    loginUsername.requestFocus();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // Method for showing a custom dialog for internet connection
    private void showCustomDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
        builder.setMessage("Please check your internet connection.")
                .setCancelable(false)
                .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(getApplicationContext(), Login.class)); // Redirect user back to Login
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    // Method for checking internet connection
    private boolean isConnected(Login login) {
        ConnectivityManager connectivityManager = (ConnectivityManager) login.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if ((wifiConn != null && wifiConn.isConnected()) || (mobileConn != null && mobileConn.isConnected())) {
            return true;
        } else {
            return false;
        }
    }

    // Remember the user's credentials based on the "remember me" checkbox
    private void rememberUser(String username, String password) {
        boolean shouldRemember = rememberMe.isChecked();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("remembered", shouldRemember);
        if (shouldRemember) {
            editor.putString("username", username);
            editor.putString("password", password);
        } else {
            editor.remove("username");
            editor.remove("password");
        }
        editor.apply();
    }
}
