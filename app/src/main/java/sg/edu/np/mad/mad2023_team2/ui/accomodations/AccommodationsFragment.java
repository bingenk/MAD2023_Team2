package sg.edu.np.mad.mad2023_team2.ui.accomodations;


import androidx.appcompat.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Parcelable;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageButton;

import android.widget.ProgressBar;
import android.widget.SearchView;



import com.android.volley.Request;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;


import org.json.JSONArray;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import sg.edu.np.mad.mad2023_team2.R;
import sg.edu.np.mad.mad2023_team2.ui.MainActivity;
import sg.edu.np.mad.mad2023_team2.ui.Recommendations.OnClickInterface;
import sg.edu.np.mad.mad2023_team2.ui.Recommendations.VolleySingleton;

// Showcases all the accommodations to book, with a search bar and to reset search
public class AccommodationsFragment extends Fragment implements OnClickInterface {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private AccommodationsAdapter adapter;
    public ArrayList<Accommodations> hotelList = new ArrayList<>(),searchList = new ArrayList<>();
    private int noAdults,noRooms;
    private Date checkIn,checkOut;
    private int pageNo;
    private RecyclerView rv;
    private ProgressBar progressBar;
    private ProgressBar loadPb;
    private ImageButton filter;
    private Button loadMore;
    private String mParam1,mParam2;

    public AccommodationsFragment() {
        // Required empty public constructor
    }

    // A method to return the interface of the class
    public OnClickInterface getinterface()
    {
        return this;
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

    // Initialize the initial values of the edittext, initialize the loading bar and the buttons onclick functions
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_accomodations, container, false);
        rv = v.findViewById(R.id.accommodations_list);
        noAdults = 2;
        noRooms = 1;
        progressBar = v.findViewById(R.id.hotel_pb);
        loadPb = v.findViewById(R.id.hotel_loadpb);
        pageNo = 0;

        // Creates the popup on open of the fragment
        new AccommodationPopup(v, noAdults, noRooms, checkIn, checkOut, true, new AccommodationPopup.PopupCallback() {
            @Override
            public void getValues(View v,int adults, int rooms, Date checkin, Date checkout) {
                // Binds user values from the popup to the global variables
                noAdults = adults;
                noRooms = rooms;
                checkIn = checkin;
                checkOut = checkout;
                // call api and populate recyclerview with the user values
                retrieveHotels(v,noAdults,noRooms,checkIn,checkOut,pageNo);
            }
        });

        // Changes the recyclerview when the user inputs values to the searchview
        SearchView search = v.findViewById(R.id.hotel_search);
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

        // Allows user to change the values by opening the popup again
        filter = v.findViewById(R.id.hotel_filter);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AccommodationPopup(v, noAdults, noRooms, checkIn, checkOut, false, new AccommodationPopup.PopupCallback() {
                    @Override
                    public void getValues(View v,int adults, int rooms, Date checkin, Date checkout) {
                        noAdults = adults;
                        noRooms = rooms;
                        checkIn = checkin;
                        checkOut = checkout;
                        pageNo = 0; // Reset to the first page
                        hotelList.clear();
                        retrieveHotels(v,noAdults,noRooms,checkIn,checkOut,pageNo);
                    }
                });
            }
        });

        // Allow users to load in more items to the recyclerview by calling the api to get the data from the other pages
        loadMore = v.findViewById(R.id.hotel_load);
        loadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    pageNo+=1;
                    retrieveHotels(v,noAdults,noRooms,checkIn,checkOut,pageNo);
            }
        });

        return v;
    }

    // Filters the value to find if the hotel name is same as the user input
    public void filter(String text)
    {
        text = text.toLowerCase();
        ArrayList<Accommodations> searchData = new ArrayList<>();
        for (Accommodations a : hotelList)
        {
            // Checks if the name of the accommodation would contain the user's search, and adds data to the searchlist
            if (a.getName().toLowerCase().contains(text))
            {
                searchData.add(a);
            }
        }
        // Assigns values gotten to the global variable list
        searchList = searchData;

        // Checks if adapter is not null, else creates the new recyclerview
        if (adapter!=null)
        {
            // Updates the recyclerview to display the search results only
            adapter.setUpdatedList(searchList);
        }
        else
        {
            adapter = new AccommodationsAdapter(hotelList, getinterface(), getView().getContext());
            RecyclerView rv = getView().findViewById(R.id.accommodations_list);
            LinearLayoutManager lm = new LinearLayoutManager(requireContext());
            rv.setLayoutManager(lm);
            rv.setItemAnimator(new DefaultItemAnimator());
            rv.setAdapter(adapter);
        }
    }

    // Sends users to the page of the accommodation when they click on the item
    @Override
    public void onClick(int pos) {
        Accommodations item;

        // Checks if the user is searching for hotels using searchview, else it uses the normal values
        if(!searchList.isEmpty())
        {
            item = searchList.get(pos);
        }
        else
        {
            item = hotelList.get(pos);
        }

        // Sends them to the AccommodationDetails activity while passing the accommodation object to the activity
        Intent intent = new Intent(getView().getContext(), AccommodationsDetails.class);
        intent.putExtra("accommodation", (Parcelable) item);
        startActivity(intent);
    }

    // Calls the api and populates the recyclerview with the api data
    private void retrieveHotels(View v, int noAdults,int noRooms, Date checkin, Date checkout,int pageNo) {
        // Shows the progress bar while hiding the load more button and disabling the filter button
        if (pageNo==0)
        {
            progressBar.setVisibility(View.VISIBLE);
        }
        else{
            loadPb.setVisibility(View.VISIBLE);
        }
        loadMore.setVisibility(View.GONE);
        filter.setEnabled(false);

        // Hides the recyclerview if there is no data
        if (hotelList.isEmpty())
        {
            rv.setVisibility(View.GONE);
        }

        // Formats the date to the required date format of the api
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String checkinS = dateFormat.format(checkin);
        String checkoutS = dateFormat.format(checkout);

        // Passes in the data from the user to the url using concatenation
        String url= "https://booking-com.p.rapidapi.com/v1/hotels/search?checkin_date="+checkinS+"&dest_type=city&units=metric&checkout_date="+checkoutS+"&adults_number="+noAdults+ "&order_by=popularity&dest_id=-73635&filter_by_currency=SGD&locale=en-gb&room_number="+noRooms+"&page_number="+pageNo;

        // Builds new alert dialog to show when there is an error
        AlertDialog.Builder errorAlert =  new AlertDialog.Builder(v.getContext());
        errorAlert.setTitle("Error occurred when fetching request \n:(");
        errorAlert.setMessage("Do you want to try again?");

        errorAlert.setPositiveButton("Reload", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Retry retrieving hotels again
                retrieveHotels(v, noAdults,noRooms,checkin,checkout,pageNo);
            }
        });

        errorAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Send back to home page if the user does not want to retry
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        // Creates a new volley request to the api and retrieve the jsonobject
        JsonObjectRequest hotelRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Find the jsonarray in the api
                            JSONArray hotelArray = response.getJSONArray("result");
                            for (int i = 0; i< hotelArray.length() ; i++)
                            {
                                // iterate through the array and store the information into a accommodation object which is added to the global list
                                JSONObject hotel = hotelArray.getJSONObject(i);
                                JSONArray distances = hotel.getJSONArray("distances");
                                hotelList.add(
                                        new Accommodations(
                                                hotel.getInt("hotel_id"),
                                                hotel.getString("hotel_name"),
                                                hotel.getString("accommodation_type_name"),
                                                hotel.getString("address"),
                                                hotel.getDouble("min_total_price"),
                                                hotel.getDouble("latitude"),
                                                hotel.getDouble("longitude"),
                                                hotel.getDouble("review_score"),
                                                hotel.getString("district"),
                                                hotel.getString("zip"),
                                                distances.getJSONObject(0).getString("text"),
                                                hotel.getJSONObject("checkin").getString("from"),
                                                hotel.getJSONObject("checkout").getString("until"),
                                                Html.fromHtml(hotel.getString("unit_configuration_label")).toString(),
                                                hotel.getString("max_1440_photo_url"),
                                                // Checks if the jsonarray has more than one items, if not returns a blank string
                                                distances.length()==1?"":distances.getJSONObject(1).getString("text")
                                        ));
                                // Checks if adapter is null and creates a new adapter and recyclerview with the data
                                if (adapter==null)
                                {
                                    adapter = new AccommodationsAdapter(hotelList, getinterface(), v.getContext());
                                    LinearLayoutManager lm = new LinearLayoutManager(requireContext());
                                    rv.setLayoutManager(lm);
                                    rv.setItemAnimator(new DefaultItemAnimator());
                                    rv.setAdapter(adapter);
                                }

                                // Would update the recyclerview with the new data if there is already an adapter
                                else
                                {
                                    adapter.setUpdatedList(hotelList);
                                }

                                // Makes the recyclerview visible when api finishes populating the recyclerview and hides the progressbar (Only when the recyclerview is not visible in the first place)
                                if (pageNo==0)
                                {
                                    rv.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                }

                                // Hides the progress bar at the bottom of the recyclerview if the user is just loading more items
                                else
                                {
                                    loadPb.setVisibility(View.GONE);
                                }
                                // Shows the loadmore button and enables the filter button
                                loadMore.setVisibility(View.VISIBLE);
                                filter.setEnabled(true);
                            }
                        } catch (Exception e) {
                            errorAlert.show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        errorAlert.show();
                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("X-RapidAPI-Host", "booking-com.p.rapidapi.com");
                headers.put("X-RapidAPI-Key", "ee43496dbdmshde591371c7a5a5dp1bee16jsn743aa16e6928");
                return headers;
            }
        };
        VolleySingleton.getInstance(v.getContext()).addToRequestQueue(hotelRequest);
    }
}