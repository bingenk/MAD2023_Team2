package sg.edu.np.mad.mad2023_team2.ui.BookingAttraction;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import sg.edu.np.mad.mad2023_team2.R;

public class Attract_info extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attract_info1);


        int image = getIntent().getIntExtra("Image", 0);
        String title = getIntent().getStringExtra("Title");
        String desc = getIntent().getStringExtra("Description");

        TextView descView = findViewById(R.id.info_desc);
        TextView titleView = findViewById(R.id.info_title);
        ImageView imageView = findViewById(R.id.info_image);

        descView.setText(desc);
        titleView.setText(title);
        imageView.setImageResource(image);


    }
}