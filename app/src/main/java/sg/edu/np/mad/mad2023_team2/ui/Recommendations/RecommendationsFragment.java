package sg.edu.np.mad.mad2023_team2.ui.Recommendations;


import static com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.location.LocationRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import sg.edu.np.mad.mad2023_team2.R;
import sg.edu.np.mad.mad2023_team2.ui.MainActivity;

public class RecommendationsFragment extends Fragment implements OnClickInterface {
    private ToggleButton restaurantToggle, attractionToggle;
    private Button loadMore;
    private TextView selectText;
    private ImageButton filter;
    private RecyclerView rv;
    private ReccomendationsAdapter adapter;
    private ProgressBar progressBar;
    private ArrayList<Attraction> attractionList = new ArrayList<>();
    private ArrayList<Restaurant> restrauntList = new ArrayList<>();
    private double latitude, longitude;
    private int pageNo;
    private boolean isRestaurant;

    private int distance;
    private String unit;

    private FusedLocationProviderClient locationClient;

    public RecommendationsFragment() {
    }

    public OnClickInterface getinterface() {
        return this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recommendations, container, false);

        restaurantToggle = v.findViewById(R.id.restaurant_toggle);
        attractionToggle = v.findViewById(R.id.attractions_toggle);
        filter = v.findViewById(R.id.recco_filter);
        rv = v.findViewById(R.id.recco_rv);
        progressBar = v.findViewById(R.id.recco_pb);
        selectText = v.findViewById(R.id.recco_selecttext);
        loadMore = v.findViewById(R.id.recco_load);
        locationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        LinearLayoutManager lm = new LinearLayoutManager(requireContext());
        rv.setLayoutManager(lm);
        rv.setItemAnimator(new DefaultItemAnimator());

        getLocation();

        if (restrauntList.isEmpty() && attractionList.isEmpty())
        {
            filter.setEnabled(false);
        }

        restaurantToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onChecked(getView().getRootView(), isChecked, restaurantToggle, attractionToggle);
            }
        });

        attractionToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onChecked(getView().getRootView(), isChecked, attractionToggle, restaurantToggle);
            }
        });

        loadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageNo += 1;
                generateList(v, latitude, longitude, pageNo, distance, unit);
            }
        });

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RecommendationsPopUp(getView(), isRestaurant,new RecommendationsPopUp.PopupCallback() {
                    @Override
                    public void getValues(int distVal, String unitVal) {
                        restrauntList.clear();
                        attractionList.clear();
                        distance = distVal;
                        unit = unitVal;
                        pageNo = 0;
                        Toast.makeText(getContext(),"Filters applied!",Toast.LENGTH_SHORT).show();
                        Log.d("RADIUSINPUT", unit);
                        generateList(getView(),latitude,longitude,pageNo,distance,unit);
                    }
                });
            }
        });

        return v;
    }

    private void onChecked(View v, boolean checked, ToggleButton buttonPressed, ToggleButton buttonOther) {
        if (checked) {
            buttonOther.setChecked(false);
            buttonPressed.setEnabled(false);
            isRestaurant = restaurantToggle.isChecked();
            unit = "km";
            distance = 10;
            pageNo = 0;
            getLocation();
            if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            {
                generateList(v, latitude, longitude, pageNo, distance, unit);
            }
            else
            {
                selectText.setText("Please enable location services to start finding nearby recommendations! Click on a button to try again");
            }
        } else if (!checked) {
            buttonPressed.setEnabled(true);
        }
    }

    private void generateList(View v, double lat, double lon, int pageNo, int radiusDistance,String unit) {
        progressBar.setVisibility(View.VISIBLE);
        restaurantToggle.setEnabled(false);
        attractionToggle.setEnabled(false);
        selectText.setVisibility(View.GONE);
        loadMore.setVisibility(View.GONE);

        String url = "";

        if (isRestaurant) {
            url = "https://travel-advisor.p.rapidapi.com/restaurants/list-by-latlng?latitude=" + lat + "&longitude=" + lon + "&offset=" + pageNo * 30 + "&limit=30&currency=SGD&distance="+ radiusDistance +"&open_now=true&lunit="+ unit +"&lang=en_US";
        } else {
            url = "https://travel-advisor.p.rapidapi.com/attractions/list-by-latlng?latitude=" + lat + "&longitude=" + lon + "&offset=" + pageNo * 30 + "&lunit="+ unit +"&currency=SGD&limit=30&distance="+ radiusDistance +"&lang=en_US&";
        }

        if (pageNo == 0) {
            rv.setVisibility(View.GONE);
        }

        if (url != "") {
            JsonObjectRequest hotelRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                // Find the jsonarray in the api
                                JSONArray dataArray = response.getJSONArray("data");
                                Log.d("JSONData", dataArray.toString());
                                for (int i = 0; i < dataArray.length(); i++) {

                                    JSONObject item = dataArray.getJSONObject(i);

                                    if (item.has("cuisine")) {
                                        ArrayList<String> cuisine = new ArrayList<>();
                                        ArrayList<String> dietaryRestrictions = new ArrayList<>();
                                        ArrayList<String> awards = new ArrayList<>();
                                        for (int j = 0; j < item.getJSONArray("cuisine").length(); j++) {
                                            cuisine.add(item.getJSONArray("cuisine").getJSONObject(j).getString("name"));
                                        }

                                        for (int j = 0; j < item.getJSONArray("dietary_restrictions").length(); j++) {
                                            dietaryRestrictions.add(item.getJSONArray("dietary_restrictions").getJSONObject(j).getString("name"));
                                        }

                                        for (int j = 0; j < item.getJSONArray("awards").length(); j++) {
                                            awards.add(item.getJSONArray("awards").getJSONObject(j).getString("name"));
                                        }

                                        restrauntList.add(new Restaurant(
                                                Integer.parseInt(item.getString("location_id")),
                                                item.getString("name"),
                                                item.getString("description"),
                                                cuisine,
                                                dietaryRestrictions,
                                                item.has("open_now_text") ? item.getString("open_now_text") : "",
                                                item.has("neighborhood_info") ? item.getJSONArray("neighborhood_info").getJSONObject(0).getString("name") : null,
                                                item.getString("address"),
                                                item.isNull("distance_string") ? String.format("%.1f " + unit + " Away!", Double.parseDouble(item.getString("distance"))) : item.getString("distance_string") + " Away",
                                                item.getString("price_level"),
                                                item.has("price") ? item.getString("price") : "",
                                                Double.parseDouble(item.getString("latitude")),
                                                Double.parseDouble(item.getString("longitude")),
                                                item.has("rating") ? Double.parseDouble(item.getString("rating")) : 0,
                                                awards,
                                                item.getString("web_url"),
                                                item.has("photo") ? item.getJSONObject("photo").getJSONObject("images").getJSONObject("original").getString("url") : "https://static.vecteezy.com/system/resources/thumbnails/004/141/669/small/no-photo-or-blank-image-icon-loading-images-or-missing-image-mark-image-not-available-or-image-coming-soon-sign-simple-nature-silhouette-in-frame-isolated-illustration-vector.jpg"
                                        ));
                                    } else if (item.has("name") && Integer.parseInt(item.getString("location_id"))!= 0) {
                                        ArrayList<String> awards = new ArrayList<>();

                                        for (int j = 0; j < item.getJSONArray("awards").length(); j++) {
                                            awards.add(item.getJSONArray("awards").getJSONObject(j).getString("name"));
                                        }

                                        Log.d("DISTANCE", String.valueOf(item.isNull("distance_string")));
                                        Log.d("DISTANCE STRING", item.getString("distance_string"));

                                        attractionList.add(new Attraction(
                                                Integer.parseInt(item.getString("location_id")),
                                                item.getString("name"),
                                                item.getString("description"),
                                                item.has("open_now_text") ? item.getString("open_now_text") : "",
                                                item.getJSONArray("subtype").getJSONObject(0).getString("name"),
                                                item.has("neighborhood_info") ? item.getJSONArray("neighborhood_info").getJSONObject(0).getString("name") : null,
                                                item.getString("address"),
                                                item.isNull("distance_string") ? String.format("%.1f " + unit + " Away!", Double.parseDouble(item.getString("distance"))) : item.getString("distance_string") + " Away",
                                                item.has("offer_group") ? item.getJSONObject("offer_group").getString("lowest_price") : null,
                                                Double.parseDouble(item.getString("latitude")),
                                                Double.parseDouble(item.getString("longitude")),
                                                item.has("rating") ? Double.parseDouble(item.getString("rating")) : 0,
                                                awards,
                                                item.getString("web_url"),
                                                item.has("photo") ? item.getJSONObject("photo").getJSONObject("images").getJSONObject("original").getString("url") : "https://static.vecteezy.com/system/resources/thumbnails/004/141/669/small/no-photo-or-blank-image-icon-loading-images-or-missing-image-mark-image-not-available-or-image-coming-soon-sign-simple-nature-silhouette-in-frame-isolated-illustration-vector.jpg"
                                        ));
                                    }

                                    if (adapter == null || pageNo == 0) {
                                        if (isRestaurant) {
                                            adapter = new ReccomendationsAdapter(getinterface(), restrauntList, null, v.getContext());
                                            rv.setAdapter(adapter);
                                        } else {
                                            adapter = new ReccomendationsAdapter(getinterface(), null, attractionList, v.getContext());
                                            rv.setAdapter(adapter);
                                        }
                                    }

                                    if (adapter != null && pageNo >= 1) {
                                        if (isRestaurant) {
                                            adapter.setUpdatedList(restrauntList, null);
                                        } else {
                                            adapter.setUpdatedList(null, attractionList);
                                        }
                                    }

                                    if (isRestaurant) {
                                        attractionToggle.setEnabled(true);
                                    } else {
                                        restaurantToggle.setEnabled(true);
                                    }

                                    progressBar.setVisibility(View.GONE);
                                    rv.setVisibility(View.VISIBLE);
                                    loadMore.setVisibility(View.VISIBLE);
                                    filter.setEnabled(true);
                                }
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            showErrorAlert(v, lat, lon, pageNo, radiusDistance, unit, error);
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("X-RapidAPI-Host", "travel-advisor.p.rapidapi.com");
                    headers.put("X-RapidAPI-Key", "ee43496dbdmshde591371c7a5a5dp1bee16jsn743aa16e6928");
                    return headers;
                }
            };
            VolleySingleton.getInstance(v.getContext()).addToRequestQueue(hotelRequest);
        } else {
            showErrorAlert(v, lat, lon, pageNo, radiusDistance, unit,  null);
        }
    }

    private void showErrorAlert(View v, double lat, double lon, int pageNo, int distance, String unit, VolleyError error) {
        AlertDialog.Builder errorAlert = new AlertDialog.Builder(v.getContext());
        errorAlert.setTitle("Error occurred when fetching request :(");
        errorAlert.setMessage(error != null ? error.toString() + "\n" : "" + "Do you want to try again?");

        errorAlert.setPositiveButton("Reload", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Retry retrieving items again
                generateList(v, lat, lon, pageNo,distance,unit);
            }
        });

        errorAlert.setNegativeButton("Go back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Send back to home page if the user does not want to retry
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        errorAlert.show();
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();

                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                    } else {
                        LocationRequest locationRequest = new LocationRequest()
                                .setPriority(PRIORITY_HIGH_ACCURACY)
                                .setInterval(5)
                                .setFastestInterval(0)
                                .setNumUpdates(1);

                        locationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

                        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(getActivity(), new String[]{
                                    Manifest.permission.ACCESS_COARSE_LOCATION,
                                    Manifest.permission.ACCESS_FINE_LOCATION},44);
                        }

                        locationClient.requestLocationUpdates(locationRequest, new LocationCallback() {
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                Location location = locationResult.getLastLocation();
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();

                            }
                        }, Looper.myLooper());
                    }
                }
            });
        }
        else
        {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION},44);
        }
    }

    @Override
    public void onClick(int p) {
        Intent intent = new Intent(getView().getContext(), ReccomendationsDetails.class);
        if (isRestaurant)
        {
            intent.putExtra("reccomendation",restrauntList.get(p));
        }
        else
        {
            intent.putExtra("reccomendation",attractionList.get(p));
        }
        startActivity(intent);
    }
}