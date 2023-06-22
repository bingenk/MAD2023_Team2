package sg.edu.np.mad.mad2023_team2.ui.accomodations;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import sg.edu.np.mad.mad2023_team2.R;

public class AccommodationsAdapter extends RecyclerView.Adapter<AccommodationsAdapter.AccommodationsHolder>{

    private HotelListInterface listInterface;
    private ArrayList<Accommodations> data;

    public AccommodationsAdapter(ArrayList<Accommodations> d, HotelListInterface listInterface)
    {
        if (d == null) {
            data = new ArrayList<>();
        } else {
            data = d;
        }
        this.listInterface = listInterface;
    }

    public void setSearch(ArrayList<Accommodations> searchList)
    {
        this.data = searchList;
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
        if (hotel.getImage() != null)
        {
            holder.hotelPhoto.setImageBitmap(hotel.getImage());
        }
    }
    @Override
    public int getItemCount() {
        return data.size();
    }


    class AccommodationsHolder extends RecyclerView.ViewHolder {
        ImageView hotelPhoto;
        TextView hotelName,hotelRating,hotelAddress;
        AccommodationsHolder(View view)
        {
            super(view);
            hotelPhoto = view.findViewById(R.id.hotel_image);
            hotelName = view.findViewById(R.id.hotel_type);
            hotelRating = view.findViewById(R.id.hotel_name);
            hotelAddress = view.findViewById(R.id.hotel_address);

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
