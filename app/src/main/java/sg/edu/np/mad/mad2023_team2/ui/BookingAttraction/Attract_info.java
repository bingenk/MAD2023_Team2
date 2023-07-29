package sg.edu.np.mad.mad2023_team2.ui.BookingAttraction;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import sg.edu.np.mad.mad2023_team2.R;
import sg.edu.np.mad.mad2023_team2.ui.CarparkAvailability.CarparkAvailability;

public class Attract_info extends AppCompatActivity {
    //////////////////////////////////
    //        OnCreateFunction      //
    //////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attract_info1);

        // Nearby Car Parks button on click function
        Button button = findViewById(R.id.CarparkButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Attract_info.this, CarparkAvailability.class);
                String latitude = getIntent().getStringExtra("Latitude");
                String longitude = getIntent().getStringExtra("Longitude");
                Log.d("lat", latitude);
                Log.d("lon", longitude);
                intent.putExtra("latitude1", latitude);    // Transfer Data from API to Attract_Info Activity and Change Activity
                intent.putExtra("longitude1", longitude);
                startActivity(intent);
            }
        });

        // Retrieve attraction details
        String image = getIntent().getStringExtra("Image");
        String title = getIntent().getStringExtra("Title");
        String desc = getIntent().getStringExtra("Description");
        String address = getIntent().getStringExtra("Address");

        //
        TextView descView = findViewById(R.id.info_desc);
        TextView titleView = findViewById(R.id.info_title);
        ImageView imageView = findViewById(R.id.info_image);
        TextView addView = findViewById(R.id.info_address);




        descView.setText(desc);
        descView.setMovementMethod(new ScrollingMovementMethod());
        addView.setText(address);
        titleView.setText(title);
        Picasso.with(this).load(image).into(imageView);














    }
}