package sg.edu.np.mad.mad2023_team2.ui.BookingAttraction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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
import sg.edu.np.mad.mad2023_team2.ui.BookingAttraction.adapter.TripAdapter;
import sg.edu.np.mad.mad2023_team2.ui.BookingAttraction.models.Item;
import sg.edu.np.mad.mad2023_team2.ui.BookingAttraction.models.Trip;


public class BookAttractActivity extends AppCompatActivity {

    //////////////////////////////////////
    //        INITIALISE VALUES         //
    //////////////////////////////////////

    List<Item> items1 = new ArrayList<>();

    //////////////////////////////////////
    //        onCreate Function         //
    //////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_attract);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);  // Assign recyclerView to xml RecyclerView
        ArrayList<Item> itemList;
        itemList = retrieveAttract(new VolleyCallBack() {    // CALL: GET Request API for Retrieving Data


            //////////////////////////////////////
            //        OnItemClick Function      //
            //////////////////////////////////////
            @Override
            public void onItemClick(int position) {          // ONCLICK Function for RecyclerView ItemView
                Intent intent = new Intent(BookAttractActivity.this, Attract_info.class);
                Item item = items1.get(position);
                Trip trip = (Trip) item.getObject();
                String title = trip.getTripTitle();
                String imageUrl = trip.getTripImage();
                String desc = trip.getDesc();
                String address = trip.getTrip();

                intent.putExtra("Image", imageUrl);    // Transfer Data from API to Attract_Info Activity and Change Activity
                intent.putExtra("Title", title);
                intent.putExtra("Address", address);
                intent.putExtra("Description", desc);
                startActivity(intent);
            }

            /////////////////////////////////////////////////
            //        VolleyCallBack onSuccess Function    //
            /////////////////////////////////////////////////

            @Override
            public ArrayList<Item> onSuccess(ArrayList<Item> items) {

                // Once Callback is successful, Send API data to RecyclerView Adapter
                TripAdapter mAdapter = new TripAdapter(items, this);
                recyclerView.setAdapter(mAdapter);
                items1 = items;
                return items;
            }

        });
    }



    /////////////////////////////////////////////////////
    //        GET API Function Using VolleyRequest     //
    /////////////////////////////////////////////////////
    public ArrayList<Item> retrieveAttract(final VolleyCallBack callBack) {

        // GET REQUEST:

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://travel-advisor.p.rapidapi.com/attractions/list?location_id=294265&currency=SGD&lang=en_US&lunit=km&limit=30&sort=recommended";
        ArrayList<Item> items = new ArrayList<>();
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        // Response with API request Successful, retrieve JSONObjects to put in RecyclerView Array for Adapter

                        try {
                            JSONArray attractArray = response.getJSONArray("data");
                            for (int i = 0; i < attractArray.length(); i++) {

                                JSONObject attract = attractArray.getJSONObject(i);

                                while (attract.has("name") && attract.has("address") && attract.has("description") && attract.has("photo")) {

                                    String imageurl = attract.getJSONObject("photo").getJSONObject("images").getJSONObject("original").getString("url");
                                    String name = attract.getString("name");
                                    Log.d("String", name);
                                    String address = attract.getString("address");
                                    String description = attract.getString("description");

                                    // Validate that JSONObjects are not empty before putting it in the array

                                    if(!name.isEmpty() && !address.isEmpty() && !description.isEmpty() && !imageurl.isEmpty()) {
                                        Trip trip = new Trip(imageurl, name, address, description);
                                        items.add(new Item(0, trip));    // Add JSONObjects / Strings to RecyclerViewArray
                                    }
                                    break;
                                }

                            }
                            // CallBack onSuccess
                            callBack.onSuccess(items);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }


                    }

                }, new Response.ErrorListener() {        // ERROR RESPONSE
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR", error.toString());
            }

        }) {
            @Override        // INIIALISE Headers for API request
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("X-RapidAPI-Key", "ebfc51dbe5msh7bb78c35f36b70cp1abf38jsn287c523483c9");
                headers.put("X-RapidAPI-Host", "travel-advisor.p.rapidapi.com");
                return headers;
            }
        };
        queue.add(jsonRequest);
        return items;
    }


    ////////////////////////////////////////////////////
    //        VolleyCallBack Function for GET API     //
    ////////////////////////////////////////////////////

    public interface VolleyCallBack extends RecyclerViewInterface {
        ArrayList<Item> onSuccess(ArrayList<Item> items);
    }


}








