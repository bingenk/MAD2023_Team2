package sg.edu.np.mad.mad2023_team2.ui.accomodations;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.os.Bundle;

import android.view.View;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;


import sg.edu.np.mad.mad2023_team2.R;
import sg.edu.np.mad.mad2023_team2.ui.Currency_Converter.Get_Currency_Of_App;
import sg.edu.np.mad.mad2023_team2.ui.Currency_Converter.Favorites.FavoritesManager;

// Showcases information of the accommodation the user has selected
public class AccommodationsDetails extends AppCompatActivity {
    private TextView name, type, address, district, zip, price, config, rating, distance, cots, checkin, checkout;
    private ImageView photo;
    private Accommodations accommodation;

    //PRAVEEN CODE
    private String Currency_Code;

    private double conversion_Rate;
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accommodations_details);
    //Currency_conversion_praveen
        Currency_Code = Get_Currency_Of_App.getcountrycodesharedprefs(this);
        conversion_Rate= Get_Currency_Of_App.getconversionratesharedprefs(this);
        //

        // Gets the accommodation object that the user clicked on
        accommodation = getIntent().getParcelableExtra("accommodation");

        // Initiates all the view items
        name = findViewById(R.id.details_name);
        photo = findViewById(R.id.details_image);
        type = findViewById(R.id.details_type);
        address = findViewById(R.id.details_address);
        district = findViewById(R.id.details_district);
        zip = findViewById(R.id.details_zip);
        price = findViewById(R.id.details_price);
        config = findViewById(R.id.details_config);
        rating = findViewById(R.id.details_rating);
        distance = findViewById(R.id.details_distance);
        cots = findViewById(R.id.details_cots);
        checkin = findViewById(R.id.details_checkin);
        checkout = findViewById(R.id.details_checkout);


        // Shows the information from the accommodation object if it is there
        if (accommodation != null) {
            name.setText(accommodation.getName());
            type.setText(accommodation.getType());
            address.setText(accommodation.getAddress());
            Picasso.with(this).load(accommodation.getImage()).fit().centerCrop().into(photo);
            district.setText(accommodation.getDistrict());
            zip.setText(accommodation.getZip());
            //PRAVEEN CODE
            price.setText(Currency_Code+" "+ String.format("%.2f", accommodation.getPrice()*conversion_Rate));
            //
            config.setText(accommodation.getConfiguration());
            rating.setText("Rating: " + accommodation.getRating());
            distance.setText(accommodation.getDistance());
            if (accommodation.getHasCots() == "") {
                cots.setVisibility(View.GONE);
            } else {
                cots.setText(accommodation.getHasCots());
            }
            checkin.setText("Check-In time: from "+accommodation.getCheckin());
            checkout.setText("Check-Out time: until "+accommodation.getCheckout());
        }

        // For use in the next stage
        FloatingActionButton fav = findViewById(R.id.details_fav);
        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Accommodations accommodation = getIntent().getParcelableExtra("accommodation");

               fav.setEnabled(false);
               new FavoritesManager(accommodation,getApplicationContext(),"Accommodations",accommodation.getId()).createObject();
               fav.setEnabled(true);
            }
        });

        // Goes back to the previous activity
        FloatingActionButton back = findViewById(R.id.details_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        // Brings users to the page to book the items while sending the accommodation object too
        Button book = findViewById(R.id.details_book);
        if (getIntent().getBooleanExtra("isViewing",false))
        {
            book.setEnabled(false);
        }

        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccommodationsDetails.this, AccommodationsBooking.class);
                intent.putExtra("accommodations", accommodation);
                startActivity(intent);
            }
        });
    }
}
