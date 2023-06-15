package sg.edu.np.mad.mad2023_team2.ui.accomodations;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import sg.edu.np.mad.mad2023_team2.R;

public class AccommodationsAdapter extends RecyclerView.Adapter<AccommodationsHolder> {

    private final HotelListInterface listInterface;
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

    @NonNull
    @Override
    public AccommodationsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.accomodations_layout, parent, false);
        return new AccommodationsHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull AccommodationsHolder holder, int position) {
        Accommodations hotel = data.get(position);
        holder.hotelName.setText(hotel.getName());
        holder.hotelAddress.setText(hotel.getAddress());
        holder.hotelRating.setText(hotel.getRating());
        if (hotel.getImage() != null)
        {
            holder.hotelPhoto.setImageBitmap(hotel.getImage());
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
