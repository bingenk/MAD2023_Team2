package sg.edu.np.mad.mad2023_team2.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;


import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import sg.edu.np.mad.mad2023_team2.R;
import sg.edu.np.mad.mad2023_team2.databinding.ActivityMainBinding;
import sg.edu.np.mad.mad2023_team2.ui.LoginSignup.Login;
import sg.edu.np.mad.mad2023_team2.ui.cart_sqllite_database.DataBaseHelper;
import sg.edu.np.mad.mad2023_team2.ui.cart_sqllite_database.DatabaseManager;
import sg.edu.np.mad.mad2023_team2.ui.checkout.Checkout;
import sg.edu.np.mad.mad2023_team2.ui.home.PrivacyPolicyActivity;
import sg.edu.np.mad.mad2023_team2.ui.home.SettingsActivity;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private TextView profileUsername;
    private TextView profileEmail;
    private FirebaseAuth mAuth;
    DataBaseHelper dataBaseHelper;


    // For user to logout upon clicking logout button and return to login page3
    private void logoutUser() {
        mAuth.signOut();
        Intent intent = new Intent(MainActivity.this, Login.class);
        startActivity(intent);

        // Delete cart data upon user logout
       dataBaseHelper = DatabaseManager.getDataBaseHelper(MainActivity .this);

        int variable = dataBaseHelper.deleteAllData();
        if (variable > 0) {
            Toast.makeText(MainActivity.this,"Successfully logout, cart details is cleared.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this,"Successfully logout.", Toast.LENGTH_SHORT).show();
        }
        finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            // Open the desired activity here
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_privacy) {
            // Open the desired activity here
            Intent intent = new Intent(MainActivity.this, PrivacyPolicyActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        // Show user profile upon when user login
        // Get references to the profileUsername and profileEmail TextViews
        View headerView = navigationView.getHeaderView(0);
        profileUsername = headerView.findViewById(R.id.profileUsername);
        profileEmail = headerView.findViewById(R.id.profileEmail);

        // Retrieve the user profile information from the intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("username") && intent.hasExtra("email")) {
            String username = intent.getStringExtra("username");
            String email = intent.getStringExtra("email");

            // Set the user profile information to the TextViews
            profileUsername.setText(username);
            profileEmail.setText(email);
        }

        mAuth = FirebaseAuth.getInstance();

        // User logout function
        Menu menu = navigationView.getMenu();
        MenuItem logoutItem = menu.findItem(R.id.nav_logout);

        // set onClick listener when user click logout button and perform logout action
        logoutItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                logoutUser();
                return true;
            }
        });


        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_contact, R.id.nav_bookAttract, R.id.nav_translate)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
