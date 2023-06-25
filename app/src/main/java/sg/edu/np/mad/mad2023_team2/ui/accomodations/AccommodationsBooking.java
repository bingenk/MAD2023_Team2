package sg.edu.np.mad.mad2023_team2.ui.accomodations;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import sg.edu.np.mad.mad2023_team2.R;
import sg.edu.np.mad.mad2023_team2.ui.Cart.checkout_cart_recyclerAdapter;
import sg.edu.np.mad.mad2023_team2.ui.cart_sqllite_database.DataBaseHelper;
import sg.edu.np.mad.mad2023_team2.ui.checkout_cart_sqllite.Cart_item;
import sg.edu.np.mad.mad2023_team2.ui.checkout_cart_sqllite.checkout_cart_details;
import sg.edu.np.mad.mad2023_team2.ui.checkout_cart_sqllite.checkout_cart_sqllite;

public class AccommodationsBooking extends AppCompatActivity {

    private TextView name, config, adults, rooms, checkin, checkout, price;
    private ImageView image;
    private Accommodations accommodation;
    private Date checkIn, checkOut;

    private ArrayAdapter customerArrayAdapter;
    private DataBaseHelper dataBaseHelper;
    private Button book;

    private RecyclerView rv;

    private Cart_item itemModel;

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
        book = findViewById(R.id.booking_cart);

        config.setText(accommodation.getConfiguration());
        Picasso.with(this).load(accommodation.getImage()).fit().centerCrop().into(image);

        adults.setText(String.valueOf(numAdults));
        rooms.setText(String.valueOf(numRooms));
        price.setText("$ "+  String.format("%.2f", accommodation.getPrice()));
        name.setText(accommodation.getName());
        try {
            checkIn = dateFormat.parse(data.getString("checkin", "2023-01-01"));
            checkOut = dateFormat.parse(data.getString("checkout", "2023-01-01"));
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

        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int id =1;
                String hotel_name=accommodation.getName();
                String hotel_description=accommodation.getConfiguration();
                String hotel_type=accommodation.getType();
                String hotel_address=accommodation.getAddress();
                double hotel_price=accommodation.getPrice();
                Date hotel_check_in_date=checkIn;
                Date hotel_check_out_date=checkOut;
                Double hotel_latitude=accommodation.getLatitude();
                Double hotel_longtitude=accommodation.getLongitude();
                String url = accommodation.getImage();

                try {
                    itemModel=new Cart_item(id,hotel_name,hotel_description,hotel_type,hotel_address,hotel_price,hotel_latitude,hotel_longtitude,url,hotel_check_in_date,hotel_check_out_date);
//                                Toast.makeText(MainActivity.this, itemModel.toString(), Toast.LENGTH_SHORT).show();
                }
                catch (Exception e){
//                                Toast.makeText(MainActivity.this, "Error creating customer", Toast.LENGTH_SHORT).show();
                    try {
                        itemModel=new Cart_item(id,hotel_name,hotel_description,hotel_type,hotel_address,hotel_price,hotel_latitude,hotel_longtitude,url,hotel_check_in_date,hotel_check_out_date);
                    } catch (Exception ex) {

                        throw new RuntimeException(ex);
                    }
                }
                dataBaseHelper = new DataBaseHelper(AccommodationsBooking.this);
                boolean success = dataBaseHelper.addOne(itemModel);
                new AlertDialog.Builder(AccommodationsBooking.this)
                        .setTitle("Successfully added to cart!")
                        .setIcon(R.drawable.baseline_add_shopping_cart_24)
                        .setPositiveButton("Back", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setCancelable(true)
                        .show();
                Toast.makeText(AccommodationsBooking.this, "success"+success, Toast.LENGTH_SHORT).show();
            }
        });
}}
