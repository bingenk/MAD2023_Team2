package sg.edu.np.mad.mad2023_team2.ui.accomodations;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;

import sg.edu.np.mad.mad2023_team2.R;
import sg.edu.np.mad.mad2023_team2.ui.Currency_Converter.Get_Currency_Of_App;

// A Adapter to fit the data into the recyclerview in the AccommodationsFragment activity
public class AccommodationsAdapter extends RecyclerView.Adapter<AccommodationsAdapter.AccommodationsHolder>{

    private HotelListInterface listInterface;
    private ArrayList<Accommodations> data;

    //PRAVEEN CODE
    private Context context;

    private String Currency_Code;

    private double conversion_Rate;

    // Constructor of adapter
    // Needs the interface to allow the onclick of the recyclerview item
    public AccommodationsAdapter(ArrayList<Accommodations> d, HotelListInterface listInterface, Context context)
    {
        if (d == null) {
            data = new ArrayList<>();
        } else {
            data = d;
        }
        this.listInterface = listInterface;
        this.context = context;
    }

    // Sets the new list as the data set and refresh the recyclerview
    public void setUpdatedList(ArrayList<Accommodations> newList)
    {
        this.data = newList;
        notifyDataSetChanged();
    }

    // Inflates the item layout and sets it as the view holder
    @NonNull
    @Override
    public AccommodationsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.accommodations_layout, parent, false);
        return new AccommodationsHolder(item);
    }

    // Binds the data to the view holder
    @Override
    public void onBindViewHolder(@NonNull AccommodationsHolder holder, int position) {

        //PRAVEEN CODE
        Currency_Code = Get_Currency_Of_App.getcountrycodesharedprefs(context);
        conversion_Rate=Get_Currency_Of_App.getconversionratesharedprefs(context);

        //
        Accommodations hotel = data.get(position);
        holder.hotelName.setText(hotel.getName());
        holder.hotelAddress.setText(hotel.getAddress());
        holder.hotelRating.setText(hotel.getType());

        //PRAVEEN CODE
        holder.hotelPrice.setText(Currency_Code+" "+  String.format("%.2f", hotel.getPrice()*conversion_Rate));
        //
        Picasso.with(context).load(hotel.getImage()).fit().centerCrop().into(holder.hotelPhoto);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    // View holder to bind data to the item layout
    class AccommodationsHolder extends RecyclerView.ViewHolder {
        ImageView hotelPhoto;
        TextView hotelName,hotelRating,hotelAddress,hotelPrice;
        AccommodationsHolder(View view)
        {
            super(view);
            hotelPhoto = view.findViewById(R.id.hotel_image);
            hotelName = view.findViewById(R.id.hotel_name);
            hotelRating = view.findViewById(R.id.hotel_type);
            hotelAddress = view.findViewById(R.id.hotel_address);
            hotelPrice = view.findViewById(R.id.hotel_price);

            // Sets the onclick listener to the interface onClick() method
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listInterface != null)
                    {
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION)
                        {
                            listInterface.onClick(pos);
                        }
                    }
                }
            });

        }
    }

}
