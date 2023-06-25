package sg.edu.np.mad.mad2023_team2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sg.edu.np.mad.mad2023_team2.adapter.TripAdapter;
import sg.edu.np.mad.mad2023_team2.models.Item;
import sg.edu.np.mad.mad2023_team2.models.Trip;

public class BookAttractActivity extends AppCompatActivity implements RecyclerViewInterface{
    List<Item> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_attract);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
//        AndroidNetworking.initialize(getApplicationContext());


        // CALL: GET API to retrieve attractions

//        AndroidNetworking.get("https://api.opentripmap.com/0.1/en/places/radius")
////                .addQueryParameter("apikey", "5ae2e3f221c38a28845f05b61de3e1c96cc4be1e55e26b5e8166170a")
////                .addQueryParameter("radius", "1000")
////                .addQueryParameter("limit", "5")
////                .addQueryParameter("offset", "0")
////                .addQueryParameter("lon", "103.85007")
////                .addQueryParameter("lat", "1.28967")
////                .addQueryParameter("rate", "3")
////                .addQueryParameter("format", "json")
////                .addQueryParameter("kinds", "tourist_object%2Cinteresting_places%2Chistoric_architecture%2Carchitecture")
////                .addHeaders("Referer", "https://opentripmap.io/")
//                .setPriority(Priority.LOW)
//                .build()
//                .getAsJSONArray(new JSONArrayRequestListener() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        // do anything with response
//                        Log.d("CREATION", response.toString());
//                    }
//                    @Override
//                    public void onError(ANError error) {
//                        // handle error
//                        Log.e("STATE", error.toString());
//                    }
//                });
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.opentripmap.com/0.1/en/places/radius?apikey=5ae2e3f221c38a28845f05b61de3e1c96cc4be1e55e26b5e8166170a&radius=1000&limit=5&offset=0&lon=103.85007&lat=1.28967&rate=3&format=json&kinds=tourist_object%2Chistoric_architecture%2Carchitecture";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.d("RESPONSE", "RESPONSES");
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERROR", error.toString());
                    }
                })
            {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "*/*");
                headers.put("Origin", "https://opentripmap.io");
                headers.put("Referer", "https://opentripmap.io/");
                return headers;
            }

            };


        queue.add(stringRequest);





        Trip trip1 = new Trip(R.drawable.image7, "ArtScience Museum", "Adult: $$  Child: $$","1" );
        items.add(new Item(0, trip1));
        Trip trip2 = new Trip(R.drawable.image8, "Flower Dome @Gardens", "Adult: $$  Child: $$","1");
        items.add(new Item(0, trip2));
        Trip trip3 = new Trip(R.drawable.image3, "The Singapore Flyer", "Adult: $$  Child: $$","1");
        items.add(new Item(0, trip3));
        Trip trip4 = new Trip(R.drawable.image4, "Universal Studios Singapore", "Adult: $$  Child: $$","1");
        items.add(new Item(0, trip4));
        Trip trip5 = new Trip(R.drawable.image5, "The Singapore Zoo", "Adult: $$  Child: $$","1");
        items.add(new Item(0, trip5));


        recyclerView.setAdapter(new TripAdapter(items, this));




    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(BookAttractActivity.this, Attract_info.class);

        Item item = items.get(position);
        Trip trip = (Trip) item.getObject();
        String title = trip.getTripTitle();
        int image = trip.getTripImage();
        String desc = trip.getDesc();

        intent.putExtra("Image",image);
        intent.putExtra("Title",title);
        intent.putExtra("Description",desc);
        startActivity(intent);

    }




}