package sg.edu.np.mad.mad2023_team2.ui.GoogleMapsDirection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import sg.edu.np.mad.mad2023_team2.R;

public class GoogleMapsDirection extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap myMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_maps_direction);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        //Initialize googleMap
        myMap = googleMap;

        LatLng Singapore = new LatLng(1.290270,103.851959);
        myMap.addMarker(new MarkerOptions().position(Singapore).title("Singapore"));
        myMap.moveCamera(CameraUpdateFactory.newLatLng(Singapore));
    }
}