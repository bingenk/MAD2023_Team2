package sg.edu.np.mad.mad2023_team2.ui.LoginSignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

    //Create Variables
    TextInputEditText loginUsername, loginPassword;
    Button loginButton;
    TextView signupRedirectText, forgetPassword;
    CheckBox rememberMe;
    SharedPreferences sharedPreferences;
    PreferenceManager PreferenceManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginUsername = findViewById(R.id.login_username);
        loginPassword = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.Loginbutton);
        signupRedirectText = findViewById(R.id.RedirectSignup);
        rememberMe = findViewById(R.id.rememberMe);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this); // obtain an instance of the SharedPreferences interface.
        forgetPassword = findViewById(R.id.forgetpassword);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateUsername() || !validatePassword()){

                } else {
                    checkUser();
                }
            }
        });

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, ForgetPassword.class);
                startActivity(intent);
            }
        });

        signupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Signup.class);
                startActivity(intent);
            }
        });
    }

    public Boolean validateUsername() {
        String val = loginUsername.getText().toString();
        if (val.isEmpty()){
            loginUsername.setError("Username cannot be empty");
            return false;
        }else {
            loginUsername.setError(null);
            return true;
        }
    }

    public Boolean validatePassword() {
        String val = loginPassword.getText().toString();
        if (val.isEmpty()){
            loginPassword.setError("Password cannot be empty");
            return false;
        }else {
            loginPassword.setError(null);
            return true;
        }
    }

    public void checkUser() {
        String userUsername = loginUsername.getText().toString().trim();
        String userPassword = loginPassword.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("username").equalTo(userUsername);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    loginUsername.setError(null);
                    String passwordFromDB = snapshot.child(userUsername).child("password").getValue(String.class);

                    if (!Objects.equals(passwordFromDB, userPassword)){
                        loginUsername.setError(null);
                        loginPassword.setError("Invalid Credentials!");
                        loginPassword.requestFocus();
                    }else {
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
                }else {
                    loginUsername.setError("User does not exist");
                    loginUsername.requestFocus();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
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