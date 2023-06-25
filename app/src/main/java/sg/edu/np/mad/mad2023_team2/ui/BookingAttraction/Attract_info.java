package sg.edu.np.mad.mad2023_team2.ui.BookingAttraction;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import sg.edu.np.mad.mad2023_team2.R;

public class Attract_info extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attract_info1);


        String image = getIntent().getStringExtra("Image");
        String title = getIntent().getStringExtra("Title");
        String desc = getIntent().getStringExtra("Description");
        String address = getIntent().getStringExtra("Address");

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