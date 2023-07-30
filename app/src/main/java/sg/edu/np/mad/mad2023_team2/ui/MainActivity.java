package sg.edu.np.mad.mad2023_team2.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Tasks;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import androidx.appcompat.app.AlertDialog;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import sg.edu.np.mad.mad2023_team2.R;
import sg.edu.np.mad.mad2023_team2.databinding.ActivityMainBinding;
import sg.edu.np.mad.mad2023_team2.databinding.FragmentAccomodationsBinding;
import sg.edu.np.mad.mad2023_team2.ui.BookingAttraction.BookAttractActivity;
import sg.edu.np.mad.mad2023_team2.ui.Cart.ApiManager;
import sg.edu.np.mad.mad2023_team2.ui.Cart.Cart;
import sg.edu.np.mad.mad2023_team2.ui.Cart.CartDataCallback;
import sg.edu.np.mad.mad2023_team2.ui.Cart.DeleteAllManager;
import sg.edu.np.mad.mad2023_team2.ui.Currency_Converter.SetCurrencyDetailsManager;
import sg.edu.np.mad.mad2023_team2.ui.LoginSignup.Login;
import sg.edu.np.mad.mad2023_team2.ui.accomodations.AccommodationsFragment;
import sg.edu.np.mad.mad2023_team2.ui.cart_sqllite_database.DataBaseHelper;
import sg.edu.np.mad.mad2023_team2.ui.cart_sqllite_database.DatabaseManager;
import sg.edu.np.mad.mad2023_team2.ui.checkout_cart_sqllite.Cart_item;
import sg.edu.np.mad.mad2023_team2.ui.checkout_cart_sqllite.checkout_cart_details;
import sg.edu.np.mad.mad2023_team2.ui.contact.ContactFragment;
import sg.edu.np.mad.mad2023_team2.ui.home.PrivacyPolicyActivity;
import sg.edu.np.mad.mad2023_team2.ui.home.SettingsActivity;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private TextView profileUsername, profileEmail, profileUsername_Home;
    private FirebaseAuth mAuth;
    DataBaseHelper dataBaseHelper;


    // For user to logout upon clicking logout button and return to login page
    private void logoutUser() {
        new AlertDialog.Builder(this)
                .setTitle("Logout Confirmation")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mAuth.signOut();
                        Intent intent = new Intent(MainActivity.this, Login.class);
                        startActivity(intent);
                        // Delete cart data upon user logout
                        dataBaseHelper = DatabaseManager.getDataBaseHelper(MainActivity.this);
                        // get username from shared prefs to use in firebase
                        SharedPreferences sharedPreferences = getSharedPreferences("CartFb", MODE_PRIVATE);
                        String username = sharedPreferences.getString("username", ""); // The second argument is the default value if the key is not found
                        Log.d("wassup", "ShowCustomersOnListView: firebase cart mfker");
                        Log.d("wassup", username);

                        checkout_cart_details details = dataBaseHelper.getEveryone();
                        addCartToUser(username, details.getAllcartitems());

                        SetCurrencyDetailsManager.getInstance(getApplicationContext()).resetApiCalled();
                        DeleteAllManager.getInstance(getApplicationContext()).makeDeleteCall();
                        ApiManager.getInstance(getApplicationContext()).resetApiCalled();
                        DeleteAllManager.getInstance(getApplicationContext()).resetApiCalled();// Reset apiCalled variable

//                    if (variable > 0) {
//                        Toast.makeText(MainActivity.this,"Successfully logout, cart details is cleared.", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(MainActivity.this,"Successfully logout.", Toast.LENGTH_SHORT).show();
//                    }
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_contact)
        {
            NavController navController = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.nav_contact);
            return true;
        }
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
        //delete all the data in the memory
        DeleteAllManager.getInstance(getApplicationContext()).makeDeleteCall();

        //set the currency details
        SetCurrencyDetailsManager.getInstance(getApplicationContext()).makeApiCall("SGD",1);

        //make the api call to get the cart from the api
        ApiManager.getInstance(getApplicationContext()).makeApiCall();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        // Show user profile upon when user login
        // Get references to the profileUsername and profileEmail TextViews
        View headerView = navigationView.getHeaderView(0);
        TextView myTextView = (TextView) findViewById(R.id.home_username);
        profileUsername = headerView.findViewById(R.id.profileUsername);
        profileEmail = headerView.findViewById(R.id.profileEmail);
        profileUsername_Home = myTextView;

        // Retrieve the user profile information from the intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("username") && intent.hasExtra("email")) {
            String username = intent.getStringExtra("username");
            String email = intent.getStringExtra("email");

            // Set the user profile information to the TextViews
            profileUsername.setText(username);
            profileUsername_Home.setText(username);
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

        //Home Page Navigation
        CardView hotelBookingCard = findViewById(R.id.card_hotel_booking);
        CardView attractionCard = findViewById(R.id.card_Attraction);
        CardView googleMapsDirectionCard = findViewById(R.id.card_GoogleMapsDirection);
        CardView translateCard = findViewById(R.id.card_Translate);
        CardView logoutCard = findViewById(R.id.card_logout);
        hotelBookingCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Accomodation
                NavController navController = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment_content_main);
                navController.navigate(R.id.nav_accommodations);
            }
        });

        attractionCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the Accomodation Activity
                Intent intent = new Intent(MainActivity.this, BookAttractActivity.class);
                startActivity(intent);
            }
        });

        googleMapsDirectionCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to GoogleMapsDirection
                NavController navController = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment_content_main);
                navController.navigate(R.id.nav_direction);
            }
        });

        translateCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Translate
                NavController navController = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment_content_main);
                navController.navigate(R.id.nav_translate);
            }
        });

        logoutCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
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

    /// PRAVEEN CODE
    public void addCartToUser(String userId, ArrayList<Cart_item> cartItems) {
//        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//        String userId;

//        if (currentUser != null) {
//            // User is authenticated, get the user ID
//            userId = currentUser.getUid();
//            Log.d("wassup", "addCartToUser: User ID: " + userId);
//        } else {
//            // User is not authenticated, use a default value for the user ID (you can change this as needed)
//            userId = "default_user";
//            Log.d("wassup", "addCartToUser: User is not authenticated.");
//        }

        DatabaseReference userCartRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("cart");
        Cart cart = new Cart(cartItems);
        userCartRef.setValue(cart)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("wassup", "addCartToUser: Cart added to the user successfully.");
                        // Cart added to the user successfully.
                        // You can show a success message if needed.
                    } else {
                        Log.d("wassup", "addCartToUser: Cart addition failed: " + task.getException());
                        // Cart addition failed.
                        // You can handle the error accordingly.
                    }
                })
                .addOnSuccessListener(aVoid -> Log.d("wassup", "addCartToUser: Database write was successful."))
                .addOnFailureListener(e -> Log.d("wassup", "addCartToUser: Database write failed: " + e.getMessage()));
    }
    /// PRAVEEN CODE
    public int deletealldata()
    {   dataBaseHelper = DatabaseManager.getDataBaseHelper(MainActivity .this);
         int checkloggedout=dataBaseHelper.deleteAllData();
        // Assuming you are using getDefaultSharedPreferences
        SharedPreferences sharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(this);

        // Get the editor
        SharedPreferences.Editor editor = sharedPreferences1.edit();

        // Clear the SharedPreferences
        editor.clear();

        // Apply the changes
        editor.apply();
    return checkloggedout;

    }



}
