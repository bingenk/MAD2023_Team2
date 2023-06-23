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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageButton;

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

import sg.edu.np.mad.mad2023_team2.MainActivity;
import sg.edu.np.mad.mad2023_team2.R;


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
    public ArrayList<Accommodations> hotelList = new ArrayList<>();
    private ArrayList<Accommodations> searchList = new ArrayList<>();
    private final AccommodationPopup popup = new AccommodationPopup();
    private int noAdults;
    private int noRooms;
    private int noChildren;
    private int noNights;
    private Date checkin;
    private Date checkout;
    private String mParam1;
    private String mParam2;

    public AccommodationsFragment() {
        // Required empty public constructor
    }

    public HotelListInterface getinterface()
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_accomodations, container, false);
        noAdults = 2;
        noRooms = 1;
        noChildren = 0;
        noNights = 1;
        checkin = new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000);
        checkout =  new Date(checkin.getTime() + (24 * 60 * 60 * 1000));
        retrieveHotels(v, noAdults,noRooms,checkin,checkout, new hotelCallback() {
            @Override
            public void onRetrieved(ArrayList<Accommodations> hotels) {
                hotelList = hotels;
                RecyclerView rv = (RecyclerView) v.findViewById(R.id.accommodations_list);
                adapter = new AccommodationsAdapter(hotelList, getinterface());
                LinearLayoutManager lm = new LinearLayoutManager(requireContext());
                rv.setHasFixedSize(true);
                rv.setLayoutManager(lm);
                rv.setItemAnimator(new DefaultItemAnimator());
                rv.setAdapter(adapter);
                v.findViewById(R.id.hotel_pb).setVisibility(View.GONE);
            }
        });

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
            hotelList.add(new Accommodations(i,"Holiday Inn","5-Star Hotel", "Himenaeos aliquet sem ac fusce diam et viverra ad",234.00, 1.00, 1.00, 4.8, "Bedok","123456", "1km from city centre", "15:00", "00:00","Hotel room: 2 beds"));
        }

        hotelList.add(new Accommodations(11,"Hotel Stay", "5-Star Hotel", "Himenaeos aliquet sem ac fusce diam et viverra ad",234.00, 1.00, 1.00, 4.8, "Bedok","123456", "1km from city centre", "15:00", "00:00", "Hotel room, 3 beds"));

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

    private void retrieveHotels(View v, int noAdults,int noRooms, Date checkin, Date checkout,  final hotelCallback callback) {

        String url= "https://booking-com.p.rapidapi.com/v1/hotels/search";
        JSONObject params = new JSONObject();
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String checkinS= dateFormat.format(checkin);
            String checkoutS = dateFormat.format(checkout);
            params.put("checkin_date", checkinS);
            params.put("checkout_date", checkoutS);
            params.put("dest_type", "city");
            params.put("units", "metric");
            params.put("adults_number", String.valueOf(noAdults));
            params.put("order_by", "popularity");
            params.put("dest_id", "-73635");
            params.put("filter_by_currency", "SGD");
            params.put("locale", "en-gb");
            params.put("room_number", String.valueOf(noRooms));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        AlertDialog.Builder errorAlert =  new AlertDialog.Builder(v.getContext());
        errorAlert.setTitle("Error occurred when fetching request \n:(");
        errorAlert.setMessage("Do you want to try again?");
        errorAlert.setPositiveButton("Reload", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                retrieveHotels(v, noAdults,noRooms,checkin,checkout, new hotelCallback() {
                    @Override
                    public void onRetrieved(ArrayList<Accommodations> hotels) {
                        hotelList = hotels;
                    }
                });
            }
        });

        errorAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        JsonObjectRequest hotelRequest = new JsonObjectRequest(Request.Method.GET, url, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ArrayList<Accommodations> temp = new ArrayList<>();
                        try {
                            JSONArray hotelArray = response.getJSONArray("result");
                            Log.d("Json", response.toString());

                            for (int i = 0; i< hotelArray.length() ; i++)
                            {
                                JSONObject hotel = hotelArray.getJSONObject(i);
                                JSONArray distances = hotel.getJSONArray("distances");
                                if (distances.length() == 1)
                                {
                                    temp.add(
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
                                                    Html.fromHtml(hotel.getString("unit_configuration_label")).toString()
                                            ));
                                }
                                else if (distances.length() == 2)
                                {
                                    temp.add(
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
                                                    distances.getJSONObject(1).getString("text")
                                            ));
                                }
                            }
                            callback.onRetrieved(temp);
                            for(Accommodations a : hotelList)
                            {
                                Log.d("Names in response", a.getName());
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
        AccommodationSingleton.getInstance(v.getContext()).addToRequestQueue(hotelRequest);
        for(Accommodations a : hotelList)
        {
            Log.d("Names", a.getName());
        }
    }
    interface hotelCallback {
        void onRetrieved(ArrayList<Accommodations> hotels);
    }
}