package sg.edu.np.mad.mad2023_team2.ui.accomodations;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import sg.edu.np.mad.mad2023_team2.R;

public class AccommodationsBooking extends AppCompatActivity {

    private TextView name, config, adults, rooms, checkin, checkout, price;
    private ImageView image;
    private Accommodations accommodation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accommodations_booking);
        getSupportActionBar().hide();

        accommodation = getIntent().getParcelableExtra("accommodations");
        SharedPreferences data = getSharedPreferences("Values", Context.MODE_PRIVATE);
        int numAdults = data.getInt("adults", 0);
        int numRooms = data.getInt("rooms", 0);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        name = findViewById(R.id.booking_name);
        config = findViewById(R.id.booking_config);
        adults = findViewById(R.id.booking_adults);
        rooms = findViewById(R.id.booking_rooms);
        checkin = findViewById(R.id.booking_checkin);
        checkout = findViewById(R.id.booking_checkout);
        image = findViewById(R.id.booking_image);
        price = findViewById(R.id.booking_price);

        config.setText(accommodation.getConfiguration());
        Picasso.with(this).load(accommodation.getImage()).fit().centerCrop().into(image);

        adults.setText(String.valueOf(numAdults));
        rooms.setText(String.valueOf(numRooms));
        price.setText("$ "+  String.format("%.2f", accommodation.getPrice()));
        name.setText(accommodation.getName());
        try {
            Date checkIn = dateFormat.parse(data.getString("checkin", "2023-01-01"));
            Date checkOut = dateFormat.parse(data.getString("checkout", "2023-01-01"));
            checkin.setText(dateFormat.format(checkIn));
            checkout.setText(dateFormat.format(checkOut));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        FloatingActionButton back = findViewById(R.id.booking_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}