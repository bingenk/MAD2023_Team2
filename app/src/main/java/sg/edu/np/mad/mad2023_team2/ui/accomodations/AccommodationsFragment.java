package sg.edu.np.mad.mad2023_team2.ui.accomodations;


import androidx.appcompat.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;

import android.media.Image;
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

import android.widget.Button;
import android.widget.ImageButton;

import android.widget.ProgressBar;
import android.widget.SearchView;



import com.android.volley.Request;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;


import org.json.JSONArray;

import org.json.JSONException;
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
        rv = v.findViewById(R.id.accommodations_list);
        noAdults = 2;
        noRooms = 1;
        progressBar = v.findViewById(R.id.hotel_pb);
        loadPb = v.findViewById(R.id.hotel_loadpb);
        pageNo = 0;
        new AccommodationPopup(v, noAdults, noRooms, checkIn, checkOut, true, new AccommodationPopup.PopupCallback() {
            @Override
            public void getValues(View v,int adults, int rooms, Date checkin, Date checkout) {
                noAdults = adults;
                noRooms = rooms;
                checkIn = checkin;
                checkOut = checkout;
                retrieveHotels(v,noAdults,noRooms,checkIn,checkOut,pageNo);
            }
        });

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
                        pageNo = 0;
                        hotelList.clear();
                        retrieveHotels(v,noAdults,noRooms,checkIn,checkOut,pageNo);
                    }
                });
            }
        });

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
        if (adapter!=null)
        {
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

    protected void retrieveHotels(View v, int noAdults,int noRooms, Date checkin, Date checkout,int pageNo) {
        if (pageNo==0)
        {
            progressBar.setVisibility(View.VISIBLE);
        }
        else{
            loadPb.setVisibility(View.VISIBLE);
        }
        loadMore.setVisibility(View.GONE);
        filter.setEnabled(false);

        if (hotelList.isEmpty())
        {
            rv.setVisibility(View.GONE);
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String checkinS = dateFormat.format(checkin);
        String checkoutS = dateFormat.format(checkout);

        String url= "https://booking-com.p.rapidapi.com/v1/hotels/search?checkin_date="+checkinS+"&dest_type=city&units=metric&checkout_date="+checkoutS+"&adults_number="+noAdults+ "&order_by=popularity&dest_id=-73635&filter_by_currency=SGD&locale=en-gb&room_number="+noRooms;

        AlertDialog.Builder errorAlert =  new AlertDialog.Builder(v.getContext());
        errorAlert.setTitle("Error occurred when fetching request \n:(");
        errorAlert.setMessage("Do you want to try again?");

        errorAlert.setPositiveButton("Reload", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                retrieveHotels(v, noAdults,noRooms,checkin,checkout,pageNo);
            }
        });

        errorAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        JsonObjectRequest hotelRequest = new JsonObjectRequest(Request.Method.GET, url, null,
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
                                                distances.length()==1?"":distances.getJSONObject(1).getString("text")
                                        ));
                                if (adapter==null)
                                {
                                    adapter = new AccommodationsAdapter(hotelList, getinterface(), v.getContext());
                                    LinearLayoutManager lm = new LinearLayoutManager(requireContext());
                                    rv.setLayoutManager(lm);
                                    rv.setItemAnimator(new DefaultItemAnimator());
                                    rv.setAdapter(adapter);
                                }
                                else
                                {
                                    adapter.setUpdatedList(hotelList);
                                }
                                if (pageNo==0)
                                {
                                    rv.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                }
                                else
                                {
                                    loadPb.setVisibility(View.GONE);
                                }
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
        AccommodationSingleton.getInstance(v.getContext()).addToRequestQueue(hotelRequest);
    }

}