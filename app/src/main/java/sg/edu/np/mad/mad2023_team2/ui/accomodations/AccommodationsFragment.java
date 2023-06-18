package sg.edu.np.mad.mad2023_team2.ui.accomodations;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

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
    private ArrayList<Accommodations> hotelList = generateHotels();
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
        return v;
    }

    public void filter(String text)
    {
        text = text.toLowerCase();
        ArrayList<Accommodations> searchList = new ArrayList<>();
        for (Accommodations a : hotelList)
        {
            if (a.getName().toLowerCase().contains(text))
            {
                searchList.add(a);
            }
        }
        adapter.setSearch(searchList);
    }

    public ArrayList<Accommodations> generateHotels()
    {
        ArrayList<Accommodations> hotelList = new ArrayList<>();
        for (int i=0; i<=10; i++)
        {
            hotelList.add(new Accommodations(i,"Holiday Inn","Nostra ligula, commodo hac metus scelerisque nullam luctus ultrices. Quam at et etiam purus lacinia placerat condimentum eros dapibus imperdiet in. Iaculis aliquam integer integer."
                    , "5-Star Hotel", "Himenaeos aliquet sem ac fusce diam et viverra ad",234.00, 1.00, 1.00));
        }

        hotelList.add(new Accommodations(11,"Hotel Stay","Nostra ligula, commodo hac metus scelerisque nullam luctus ultrices. Quam at et etiam purus lacinia placerat condimentum eros dapibus imperdiet in. Iaculis aliquam integer integer."
                , "5-Star Hotel", "Himenaeos aliquet sem ac fusce diam et viverra ad",234.00, 1.00, 1.00));

        return hotelList;
    }

    @Override
    public void onClick(int pos) {
        Accommodations item = hotelList.get(pos);
        Intent intent = new Intent(getView().getContext(), AccommodationsDetails.class);
        intent.putExtra("accommodation", (Parcelable) item);
        startActivity(intent);
    }
}