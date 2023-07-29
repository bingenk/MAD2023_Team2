package sg.edu.np.mad.mad2023_team2.ui.CarparkAvailability;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import sg.edu.np.mad.mad2023_team2.R;
import sg.edu.np.mad.mad2023_team2.ui.BookingAttraction.RecyclerViewInterface;


public class CarparkAvailability extends AppCompatActivity {
    ArrayList<CarparkDetails> details1 = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carpark_availability);

        RecyclerView recyclerViewCar = findViewById(R.id.recyclerView3);
        Toolbar toolbar = findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        String lat1;
        String lon1;

        ///////////////////////////////////////////////
        //       Retrieve putExtra intent values     //
        ///////////////////////////////////////////////

        if (getIntent().getStringExtra("latitude1") == null || getIntent().getStringExtra("longitude1") == null ){
            lat1 = null;
            lon1 = null;
        }
        else {
            lat1 = getIntent().getStringExtra("latitude1");
            lon1 = getIntent().getStringExtra("longitude1");
        }

        retrieveCarpark(new CarparkAvailability.VolleyCallBacker() {    // CALL: GET Request API for Retrieving Data


            //////////////////////////////////////
            //        OnItemClick Function      //
            //////////////////////////////////////
            @Override
            public void onItemClick(int position) {          // ONCLICK Function for RecyclerView ItemView

            }

            /////////////////////////////////////////////////
            //        VolleyCallBack onSuccess Function    //
            /////////////////////////////////////////////////

            public ArrayList<CarparkDetails> onSuccess(ArrayList<CarparkDetails> details) {

                double radius = 1.7;
                // Once Callback is successful, Send API data to RecyclerView Adapter
                if (lat1 == null || lon1 == null){
                    CarparkAdapter mAdapter = new CarparkAdapter(details, this );
                    recyclerViewCar.setAdapter(mAdapter);
                }
                else {
                    ArrayList<CarparkDetails> detailsWithin = findLocationsWithinRadius(Double.parseDouble(lat1), Double.parseDouble(lon1), radius, details);
                    while (detailsWithin.isEmpty()){
                        radius = radius + 1.0;
                        detailsWithin = findLocationsWithinRadius(Double.parseDouble(lat1), Double.parseDouble(lon1), radius, details);
                    }

                    CarparkAdapter mAdapter = new CarparkAdapter(detailsWithin, this);
                    recyclerViewCar.setAdapter(mAdapter);

                }
                details1 = details;
                return details;
            }

        });


    }

    // GET REQUEST API for carpark details
    public ArrayList<CarparkDetails> retrieveCarpark(final CarparkAvailability.VolleyCallBacker callBack) {

        // GET REQUEST:

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://datamall2.mytransport.sg/ltaodataservice/CarParkAvailabilityv2";
        ArrayList<CarparkDetails> details = new ArrayList<>();
        // ERROR RESPONSE
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {

                    // Response with API request Successful, retrieve JSONObjects to put in RecyclerView Array for Adapter

                    try {
                        JSONArray carparkArray = response.getJSONArray("value");
                        for (int i = 0; i < carparkArray.length(); i++) {

                            JSONObject carparks = carparkArray.getJSONObject(i);

                            while (carparks.has("Area") && carparks.has("Development") && carparks.has("Location") && carparks.has("AvailableLots")) {

                                Log.d("RESPONSE", "while loop response");
                                String area = carparks.getString("Area");
                                String development = carparks.getString("Development");
                                String location = carparks.getString("Location");
                                int availableLots = carparks.getInt("AvailableLots");
                                Log.d("String", location);

                                // Validate that JSONObjects are not empty before putting it in the array

                                if(!area.isEmpty() && !development.isEmpty() && !location.isEmpty()) {
                                    String[] locationSplit;
                                    int count = 0;
                                    locationSplit = location.split(" ");
                                    Double latitude = Double.valueOf(locationSplit[0]);
                                    Double longitude = Double.valueOf(locationSplit[1]);

                                    count++;
                                    CarparkDetails carparkDetails = new CarparkDetails(count, area, development, availableLots, latitude, longitude);
                                    details.add(carparkDetails);    // Add JSONObjects / Strings to RecyclerViewArray
                                }
                                break;
                            }
                        }
                        // CallBack onSuccess
                        callBack.onSuccess(details);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }, error -> Log.d("ERROR", error.toString())) {
            @Override        // INITIALISE Headers for API request
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("AccountKey", "xEaK8WQ/QqC4MNp8FAMkMA==");
                headers.put("accept", "application/json");
                return headers;
            }
        };
        queue.add(jsonRequest);
        return details;
    }
    public final double RADIUS_OF_EARTH_KM = 6371.0;

    // Calculate distance bewteen two lat lon coordinates
    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // Convert latitude and longitude from degrees to radians
        double lat1Rad = Math.toRadians(lat1);
        double lon1Rad = Math.toRadians(lon1);
        double lat2Rad = Math.toRadians(lat2);
        double lon2Rad = Math.toRadians(lon2);

        // Calculate the differences between latitudes and longitudes
        double deltaLat = lat2Rad - lat1Rad;
        double deltaLon = lon2Rad - lon1Rad;

        // Haversine formula
        double a = Math.pow(Math.sin(deltaLat / 2), 2) + Math.cos(lat1Rad) * Math.cos(lat2Rad) * Math.pow(Math.sin(deltaLon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // Calculate the distance using the formula and the Earth's radius
        return RADIUS_OF_EARTH_KM * c;
    }

    //Adding carparks in a new list within given Radius
    public ArrayList<CarparkDetails> findLocationsWithinRadius(double currentLat, double currentLon,
                                                               double radiusKm, List<CarparkDetails> locations) {
        ArrayList<CarparkDetails> carParksWithinRadius = new ArrayList<>();

        for (CarparkDetails location : locations) {
            double distance = calculateDistance(currentLat, currentLon, location.getLatitude(), location.getLongitude());
            Log.d("distance", String.valueOf(distance));
            if (distance <= radiusKm) {
                carParksWithinRadius.add(location);
            }
        }

        return carParksWithinRadius;
    }

    public interface VolleyCallBacker extends RecyclerViewInterface {
        ArrayList<CarparkDetails> onSuccess(ArrayList<CarparkDetails> detailsWithin);
    }
}

