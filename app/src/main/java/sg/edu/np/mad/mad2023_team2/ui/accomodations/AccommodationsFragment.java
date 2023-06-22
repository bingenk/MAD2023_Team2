package sg.edu.np.mad.mad2023_team2.ui.accomodations;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Map;
import java.util.Objects;

import sg.edu.np.mad.mad2023_team2.R;
import sg.edu.np.mad.mad2023_team2.ui.home.HomeFragment;

public class AccommodationsFragment extends Fragment implements HotelListInterface {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

//    private int id;
//    private String hotelName;
//    private String hotelAddr;
//    private String hotelType;
//    private String hotelDesc;
//    private double hotelPrice;
//    private double hotelLat;
//    private double hotelLon;

    private AccommodationsAdapter adapter;
    private ArrayList<Accommodations> hotelList = generateHotels();
    private ArrayList<Accommodations> searchList = new ArrayList<>();
    private AccommodationPopup popup = new AccommodationPopup();
    private int noAdults;
    private int noRooms;
    private int noChildren;
    private int noNights;
    private String mParam1;
    private String mParam2;

    public AccommodationsFragment() {
        // Required empty public constructor
    }

    public static AccommodationsFragment newInstance(String param1, String param2) {
        AccommodationsFragment fragment = new AccommodationsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_accomodations, container, false);
        RecyclerView rv = (RecyclerView)v.findViewById(R.id.accommodations_list);
        adapter = new AccommodationsAdapter(hotelList,this);
        LinearLayoutManager lm = new LinearLayoutManager(requireContext());
        rv.setHasFixedSize(true);
        rv.setLayoutManager(lm);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(adapter);
        noAdults = 2;
        noChildren = 0;
        noNights = 1;
        retrieveHotels(v, noAdults, noChildren, noNights);

        SearchView search = (SearchView) v.findViewById(R.id.hotel_search);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });

        ImageButton filter = (ImageButton) v.findViewById(R.id.hotel_filter);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.ShowPopup(v);
            }
        });

        return v;
    }

    public void filter(String text)
    {
        text = text.toLowerCase();
        ArrayList<Accommodations> searchData = new ArrayList<>();
        for (Accommodations a : hotelList)
        {
            if (a.getName().toLowerCase().contains(text))
            {
                searchData.add(a);
            }
        }
        searchList = searchData;
        adapter.setSearch(searchList);
    }

    public ArrayList<Accommodations> generateHotels()
    {
        ArrayList<Accommodations> hotelList = new ArrayList<>();
        for (int i=0; i<=10; i++)
        {
            hotelList.add(new Accommodations(i,"Holiday Inn","5-Star Hotel", "Himenaeos aliquet sem ac fusce diam et viverra ad",234.00, 1.00, 1.00));
        }

        hotelList.add(new Accommodations(11,"Hotel Stay", "5-Star Hotel", "Himenaeos aliquet sem ac fusce diam et viverra ad",234.00, 1.00, 1.00));

        return hotelList;
    }

    @Override
    public void onClick(int pos) {
        Accommodations item;

        if(!searchList.isEmpty())
        {
            item = searchList.get(pos);
        }
        else
        {
            item = hotelList.get(pos);
        }
        
        Intent intent = new Intent(getView().getContext(), AccommodationsDetails.class);
        intent.putExtra("accommodation", (Parcelable) item);
        startActivity(intent);
    }

    private void retrieveHotels(View v, int noAdults, int noChildren, int noNights) {

        String url= "https://booking-com.p.rapidapi.com/v2/hotels/search?order_by=popularity&adults_number=2&checkin_date=2023-09-27&filter_by_currency=SGD&dest_id=-73635&locale=en-gb&checkout_date=2023-09-28&units=metric&room_number=1&dest_type=city&include_adjacency=true&children_number=2&page_number=0&children_ages=5%2C0&categories_filter_ids=class%3A%3A2%2Cclass%3A%3A4%2Cfree_cancellation%3A%3A1";


        AlertDialog.Builder errorAlert =  new AlertDialog.Builder(v.getContext());
        errorAlert.setTitle("Error occurred when fetching request :(");
        errorAlert.setMessage("Do you want to try again?");
        errorAlert.setPositiveButton("Reload", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                retrieveHotels(v,noAdults, noChildren, noNights);
            }
        });
        errorAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(v.getContext(), HomeFragment.class));
            }
        });

        JsonObjectRequest hotelRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray hotelArray = response.getJSONArray("results");
                            Log.d("Json", response.toString());

                            for (int i=0; i<hotelArray.length() ; i++)
                            {
                                JSONObject hotel = hotelArray.getJSONObject(i);
                                ArrayList<Accommodations> accommodations = new ArrayList<>();
//                                accommodations.add(
////                                        new Accommodations(
////
////                                    )
//                                );
                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        errorAlert.show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("X-RapidAPI-Host", "booking-com.p.rapidapi.com");
                headers.put("X-RapidAPI-Key", "ee43496dbdmshde591371c7a5a5dp1bee16jsn743aa16e6928");
                return headers;
            }
        };

        AccommodationSingleton.getInstance(v.getContext()).addToRequestQueue(hotelRequest);

    }
}