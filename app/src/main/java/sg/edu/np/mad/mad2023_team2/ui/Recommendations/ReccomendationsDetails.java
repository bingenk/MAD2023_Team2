package sg.edu.np.mad.mad2023_team2.ui.Recommendations;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import sg.edu.np.mad.mad2023_team2.R;

public class ReccomendationsDetails extends AppCompatActivity {

    private ImageView reccoDetailsImage;
    private FloatingActionButton reccoDetailsBack;
    private RatingBar reccoDetailsRatingBar;
    private TextView reccoDetailsPrice,reccoDetailsName,reccoDetailsRatingNum,reccoDetailsAddress,reccoDetailsType,reccoDetailsStatus,reccoDetailsDistance,reccoDetailsLocation,reccoDetailsAwards,reccoDetailsDesc,reccoDetailsDietary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reccomendations_details);

        // Initialize all elements
        reccoDetailsPrice = findViewById(R.id.recco_details_price);
        reccoDetailsName = findViewById(R.id.recco_details_name);
        reccoDetailsRatingNum = findViewById(R.id.recco_details_ratingNum);
        reccoDetailsAddress = findViewById(R.id.recco_details_address);
        reccoDetailsType = findViewById(R.id.recco_details_type);
        reccoDetailsStatus = findViewById(R.id.recco_details_status);
        reccoDetailsDistance = findViewById(R.id.recco_details_distance);
        reccoDetailsLocation = findViewById(R.id.recco_details_location);
        reccoDetailsAwards = findViewById(R.id.recco_details_awards);
        reccoDetailsDesc = findViewById(R.id.recco_details_desc);
        reccoDetailsDietary = findViewById(R.id.recco_details_dietary);

        reccoDetailsImage = findViewById(R.id.recco_details_image);
        reccoDetailsBack = findViewById(R.id.recco_details_back);
        reccoDetailsRatingBar = findViewById(R.id.recco_details_ratingBar);

        if (getIntent().getParcelableExtra("reccomendation") instanceof Restaurant)
        {
            Restaurant restaurant = getIntent().getParcelableExtra("reccomendation");

            reccoDetailsName.setText(restaurant.getName());
            reccoDetailsAddress.setText(restaurant.getAddress());
            reccoDetailsDesc.setText(restaurant.getDesc());

            if (restaurant.getPriceRange() != null)
            {
                reccoDetailsPrice.setText(restaurant.getPriceRange());
            }
            else if (restaurant.getPriceLevel() != null)
            {
                reccoDetailsPrice.setText(restaurant.getPriceLevel());
            }
            else
            {
                reccoDetailsPrice.setVisibility(View.GONE);
            }
            checkAndSetText(reccoDetailsDistance,restaurant.getDistanceAway());
            checkAndSetText(reccoDetailsLocation, restaurant.getLocation());
            checkAndSetText(reccoDetailsStatus, restaurant.getStatus());
            checkAndSetRating(reccoDetailsRatingNum, reccoDetailsRatingBar, restaurant.getRating());
            checkAndSetList(reccoDetailsType, restaurant.getCuisine(), "");
            checkAndSetList(reccoDetailsAwards, restaurant.getAwards(), "\uD83C\uDFC6 Awards:");
            checkAndSetList(reccoDetailsDietary, restaurant.getDietaryRestrictions(), "\uD83C\uDF7D ");
            Picasso.with(this).load(restaurant.getImage()).fit().centerCrop().into(reccoDetailsImage);
        }

        else
        {
            Attraction attraction = getIntent().getParcelableExtra("reccomendation");

            reccoDetailsDietary.setVisibility(View.GONE);

            reccoDetailsName.setText(attraction.getName());
            reccoDetailsAddress.setText(attraction.getAddress());
            reccoDetailsType.setText(attraction.getType());
            reccoDetailsDesc.setText(attraction.getDesc());
            reccoDetailsDistance.setText(attraction.getDistanceAway());

            checkAndSetText(reccoDetailsPrice, attraction.getPrice());
            checkAndSetText(reccoDetailsLocation, attraction.getLocation());
            checkAndSetText(reccoDetailsStatus, attraction.getStatus());
            checkAndSetRating(reccoDetailsRatingNum, reccoDetailsRatingBar, attraction.getRating());
            checkAndSetList(reccoDetailsAwards, attraction.getAwards(), "\uD83C\uDFC6 Awards:");
            Picasso.with(this).load(attraction.getImage()).fit().centerCrop().into(reccoDetailsImage);
        }

        reccoDetailsBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void checkAndSetText(TextView text, String value)
    {
        if (value != null && !value.isEmpty())
        {
            text.setText(value);
        }
        else
        {
            text.setVisibility(View.GONE);
        }
    }

    private void checkAndSetList(TextView text, ArrayList<String> valueList, String s)
    {
        if (!valueList.isEmpty() && valueList != null)
        {
            s = s + TextUtils.join(",", valueList);
            text.setText(s);
        }
        else
        {
            text.setVisibility(View.GONE);
        }
    }

    private void checkAndSetRating(TextView num, RatingBar ratingBar, double rating)
    {
        if (rating > 0)
        {
            num.setText(String.valueOf(rating));
            ratingBar.setRating((float) rating);
        }
        else
        {
            num.setVisibility(View.GONE);
            ratingBar.setVisibility(View.GONE);
        }
    }
}