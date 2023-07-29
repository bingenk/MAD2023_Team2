package sg.edu.np.mad.mad2023_team2.ui.Favorites;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Objects;

import sg.edu.np.mad.mad2023_team2.R;
import sg.edu.np.mad.mad2023_team2.ui.Recommendations.Attraction;
import sg.edu.np.mad.mad2023_team2.ui.Recommendations.OnClickInterface;
import sg.edu.np.mad.mad2023_team2.ui.Recommendations.Restaurant;
import sg.edu.np.mad.mad2023_team2.ui.accomodations.Accommodations;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder> {

    private OnClickInterface onClickInterface;
    private ArrayList<Object> data;
    private Context context;

    @NonNull
    @Override
    public FavoritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.accommodations_layout, parent, false);
        return new FavoritesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesViewHolder holder, int position) {

        if (data.get(position) instanceof Restaurant)
        {
            Restaurant restaurant = (Restaurant) data.get(position);
            Picasso.with(context).load(restaurant.getImage()).fit().centerCrop().into(holder.image);
            holder.name.setText(restaurant.getName());
            if (restaurant.getPriceLevel() != null)
            {
                holder.price.setText(restaurant.getPriceLevel());
            }
            else
            {
                holder.price.setVisibility(View.GONE);
            }
            holder.address.setText(restaurant.getAddress());
            holder.type.setText("Restraunt");
        }
        if (data.get(position) instanceof Attraction)
        {
            Attraction attraction = (Attraction) data.get(position);
            Picasso.with(context).load(attraction.getImage()).fit().centerCrop().into(holder.image);
            holder.name.setText(attraction.getName());
            if (attraction.getPrice() != null)
            {
                holder.price.setText(attraction.getPrice());
            }
            else
            {
                holder.price.setVisibility(View.GONE);
            }
            holder.address.setText(attraction.getAddress());
            holder.type.setText("Attraction");
        }
        if (data.get(position) instanceof Accommodations)
        {
            Accommodations hotel = (Accommodations) data.get(position);
            holder.name.setText(hotel.getName());
            holder.address.setText(hotel.getAddress());
            holder.type.setText(hotel.getType());
            holder.price.setText("$ "+  String.format("%.2f", hotel.getPrice()));
            Picasso.with(context).load(hotel.getImage()).fit().centerCrop().into(holder.image);
        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class FavoritesViewHolder extends RecyclerView.ViewHolder {

        private TextView name,price,type,address;

        private ImageView image;
        public FavoritesViewHolder(@NonNull View item) {
            super(item);
            image = item.findViewById(R.id.hotel_image);
            name = item.findViewById(R.id.hotel_name);
            type = item.findViewById(R.id.hotel_rating);
            address = item.findViewById(R.id.hotel_address);
            price = item.findViewById(R.id.hotel_price);
        }
    }
}
