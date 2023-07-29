package sg.edu.np.mad.mad2023_team2.ui.GoogleMapsDirection;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.PopupMenu;



import java.io.IOException;
import java.util.List;

import sg.edu.np.mad.mad2023_team2.R;

public class GoogleMapsDirection extends Fragment implements OnMapReadyCallback, PopupMenu.OnMenuItemClickListener {

    private GoogleMap myMap;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_REQUEST_CODE = 100;
    private ImageButton mapOptionsMenuButton; // to be used for map options


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_direction, container, false);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        SearchView mapSearchView = view.findViewById(R.id.mapSearch); // Initialize SearchView
        mapOptionsMenuButton = view.findViewById(R.id.mapOptionsMenu); // Initialize ImageButton

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mapSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = mapSearchView.getQuery().toString();
                List<Address> addressList = null;

                if (!location.isEmpty()) {
                    Geocoder geocoder = new Geocoder(requireActivity());
                    try {
                        addressList = geocoder.getFromLocationName(location, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (addressList != null && !addressList.isEmpty()) {
                        Address address = addressList.get(0);
                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                        myMap.addMarker(new MarkerOptions().position(latLng).title(location));
                        myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                    } else {
                        Toast.makeText(requireActivity(), "No Location Found!", Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        mapOptionsMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });

        return view;
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        // Initialize googleMap
        myMap = googleMap;
        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // request permission
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST_CODE);
        } else {
            // Permission already granted, enable the "My Location" feature
            myMap.setMyLocationEnabled(true);
            getLocationAndUpdateMap();
        }

        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        googleMap.getUiSettings().setScrollGesturesEnabled(true);
        googleMap.getUiSettings().setRotateGesturesEnabled(false);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocationAndUpdateMap();
            }
        }
    }

    private void getLocationAndUpdateMap() {
        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation().addOnSuccessListener(requireActivity(), new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                        myMap.addMarker(new MarkerOptions().position(userLocation).title("You Are Here"));
                        myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
                    }
                }
            });
        }
    }

    // PopupMenu for map types
    public void showPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(requireActivity(), v);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.map_options);
        popupMenu.show();
    }

    // Handle click events on popupMenu items
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.normal_map) {
            myMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            return true;
        } else if (id == R.id.hybrid_map) {
            myMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            return true;
        } else if (id == R.id.satellite_map) {
            myMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            return true;
        } else if (id == R.id.terrain_map) {
            myMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            return true;
        } else {
            return false;
        }
    }
}
