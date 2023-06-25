package sg.edu.np.mad.mad2023_team2.ui.accomodations;

import android.content.Context;
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

public class AccommodationsAdapter extends RecyclerView.Adapter<AccommodationsAdapter.AccommodationsHolder>{

    private HotelListInterface listInterface;
    private ArrayList<Accommodations> data;
    private Context context;

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

    public void setUpdatedList(ArrayList<Accommodations> newList)
    {
        this.data = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AccommodationsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.accommodations_layout, parent, false);
        return new AccommodationsHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull AccommodationsHolder holder, int position) {
        Accommodations hotel = data.get(position);
        holder.hotelName.setText(hotel.getName());
        holder.hotelAddress.setText(hotel.getAddress());
        holder.hotelRating.setText(hotel.getType());
        holder.hotelPrice.setText("$ "+  String.format("%.2f", hotel.getPrice()));
        Picasso.with(context).load(hotel.getImage()).fit().centerCrop().into(holder.hotelPhoto);
    }
    @Override
    public int getItemCount() {
        return data.size();
    }


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
