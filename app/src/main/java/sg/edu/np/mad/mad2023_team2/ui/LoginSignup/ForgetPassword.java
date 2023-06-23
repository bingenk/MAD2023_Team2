package sg.edu.np.mad.mad2023_team2.ui.LoginSignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.ktx.Firebase;

import sg.edu.np.mad.mad2023_team2.R;

public class ForgetPassword extends AppCompatActivity {

    private EditText passEmail;

    private Button forgot;

    private String email;

    TextView passRedirectLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        passEmail = findViewById(R.id.passemail);
        forgot = findViewById(R.id.passforgot);
        passRedirectLogin = findViewById(R.id.PassRedirectLogin);


        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = passEmail.getText().toString();

                if (email.isEmpty()){
                    Toast.makeText(ForgetPassword.this,"Please provide your email", Toast.LENGTH_SHORT).show();
                } else {
                    forgetPassword();
                }
            }
        });

        // Direct user to login if the user wish to go back
        passRedirectLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ForgetPassword.this, Login.class);
                startActivity(intent);
            }
        });


    }

    private void forgetPassword() {

        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth. sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(ForgetPassword.this, "Check your Email", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ForgetPassword.this, Login.class));
                            finish();
                        }else {
                            Toast.makeText(ForgetPassword.this, "Error : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}