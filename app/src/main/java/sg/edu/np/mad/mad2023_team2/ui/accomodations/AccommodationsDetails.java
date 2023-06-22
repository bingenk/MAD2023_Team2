package sg.edu.np.mad.mad2023_team2.ui.accomodations;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;

import sg.edu.np.mad.mad2023_team2.R;


public class AccommodationsDetails extends AppCompatActivity {
    private TextView name;
    private TextView desc;
    private TextView type;
    private TextView location;
    private ImageView photo;
    private Accommodations accommodation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accommodations_details);
        getSupportActionBar().hide();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            accommodation = getIntent().getParcelableExtra("accommodation");
        }

        name = findViewById(R.id.details_name);
        type = findViewById(R.id.details_type);
        location = findViewById(R.id.details_address);
        photo = findViewById(R.id.details_image);

        if (accommodation != null) {
            name.setText(accommodation.getName());
            type.setText(accommodation.getType());
            location.setText(accommodation.getAddress());

            if (accommodation.getImage() != null)
                photo.setImageBitmap(accommodation.getImage());
        }
        
        FloatingActionButton map = findViewById(R.id.details_location);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(AccommodationsDetails.this)
                        .setTitle("Sorry! This feature is still being worked on")
                        .setMessage("We will release this in the next update")
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setIcon(R.drawable.baseline_construction_24)
                        .show();
            }
        });

        FloatingActionButton back = findViewById(R.id.details_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}
