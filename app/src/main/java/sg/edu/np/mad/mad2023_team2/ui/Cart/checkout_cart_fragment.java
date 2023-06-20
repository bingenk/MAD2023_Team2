package sg.edu.np.mad.mad2023_team2.ui.Cart;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import sg.edu.np.mad.mad2023_team2.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link checkout_cart_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class checkout_cart_fragment extends Fragment {

    RecyclerView rv;

    checkout_cart_recyclerAdapter recyclerAdapter;
    List<Cart_item> checkout_cart;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public checkout_cart_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment checkout_cart_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static checkout_cart_fragment newInstance(String param1, String param2) {
        checkout_cart_fragment fragment = new checkout_cart_fragment();
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
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_checkout_cart_fragment, container, false);

//        mViewModel= ViewModelProviders.of(this).get(CartViewModel.class);


        checkout_cart=generateHotels();


        rv=v.findViewById(R.id.rv_checkout);

        recyclerAdapter=new checkout_cart_recyclerAdapter(checkout_cart);

        // you can also set the layout in the xml file using the layout manager attribute
        rv.setLayoutManager(new LinearLayoutManager(v.getContext()));


        rv.setAdapter(recyclerAdapter);

//To add dividers to between the views
        DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(v.getContext(),DividerItemDecoration.VERTICAL
        );

        rv.addItemDecoration(dividerItemDecoration
        );


        return v;
    }
    public ArrayList<Cart_item> generateHotels()
    {
        ArrayList<Cart_item> hotelList = new ArrayList<>();

        for (int i=0; i<=10; i++)
        {
            hotelList.add(new Cart_item(i,"Holiday Inn","Nostra ligula, commodo hac metus scelerisque nullam luctus ultrices. Quam at et etiam purus lacinia placerat condimentum eros dapibus imperdiet in. Iaculis aliquam integer integer."
                    , "5-Star Hotel", "Himenaeos aliquet sem ac fusce diam et viverra ad",234.00, 1.00, 1.00,null, new Date("10/12/2018"), new Date("10/12/2018")));
        }

        hotelList.add(new Cart_item(11,"Hotel Stay","Nostra ligula, commodo hac metus scelerisque nullam luctus ultrices. Quam at et etiam purus lacinia placerat condimentum eros dapibus imperdiet in. Iaculis aliquam integer integer."
                , "5-Star Hotel", "Himenaeos aliquet sem ac fusce diam et viverra ad",234.00, 1.00, 1.00,null, new Date("10/12/2018"), new Date("10/12/2018")));

        return hotelList;
    }
    public static Date parseDate(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

}