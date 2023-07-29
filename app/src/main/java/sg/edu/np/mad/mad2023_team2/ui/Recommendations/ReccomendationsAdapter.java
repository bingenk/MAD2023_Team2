package sg.edu.np.mad.mad2023_team2.ui.Recommendations;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import sg.edu.np.mad.mad2023_team2.R;

public class ReccomendationsAdapter extends RecyclerView.Adapter<ReccomendationsAdapter.ReccomendationsHolder> {

    private OnClickInterface listInterface;

    private ArrayList<Attraction> attractions;

    private ArrayList<Restaurant> restaurants;

    private Context context;

    public ReccomendationsAdapter(OnClickInterface listInterface, ArrayList<Restaurant> restaurants,ArrayList<Attraction> attractions,  Context context) {
        this.listInterface = listInterface;
        this.context = context;
        this.attractions = attractions;
        this.restaurants = restaurants;
    }

    @NonNull
    @Override
    public ReccomendationsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recommendations_layout, parent, false);
        return new ReccomendationsHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ReccomendationsHolder holder, int position) {
        if (restaurants == null)
        {
            Attraction attraction = attractions.get(position);
            holder.name.setText(attraction.getName());

            if (attraction.getRating() > 0)
            {
                holder.ratingNum.setText(String.valueOf(attraction.getRating()));
                holder.ratingBar.setRating((float) attraction.getRating());
            }
            else
            {
                holder.ratingNum.setVisibility(View.GONE);
                holder.ratingBar.setVisibility(View.GONE);
            }

            holder.distance.setText(attraction.getDistanceAway());

            if (attraction.getStatus() == null)
            {
                holder.status.setText(attraction.getStatus());
            }
            else
            {
                holder.status.setVisibility(View.GONE);
            }
            holder.address.setText(attraction.getAddress());
            holder.price.setText(attraction.getPrice());
            holder.type.setText(attraction.getType());
            Picasso.with(context).load(attraction.getImage()).fit().centerCrop().into(holder.image);
        }
        else if (attractions == null)
        {
            Restaurant restaurant = restaurants.get(position);
            holder.name.setText(restaurant.getName());

            if (restaurant.getRating()>0)
            {
                holder.ratingNum.setText(String.valueOf(restaurant.getRating()));
                holder.ratingBar.setRating((float) restaurant.getRating());
            }
            else
            {
                holder.ratingNum.setVisibility(View.GONE);
                holder.ratingBar.setVisibility(View.GONE);
            }


            holder.distance.setText(restaurant.getDistanceAway());
            holder.status.setText(restaurant.getStatus());
            holder.address.setText(restaurant.getAddress());
            holder.price.setText(restaurant.getPriceLevel());

            ArrayList<String> cuisineList = restaurant.getCuisine();
            if (!cuisineList.isEmpty())
            {
                String cuisine = TextUtils.join(", ", cuisineList);
                holder.type.setText(cuisine);
            }
            else {
                holder.type.setVisibility(View.GONE);
            }

            Picasso.with(context).load(restaurant.getImage()).fit().centerCrop().into(holder.image);
        }
    }

    @Override
    public int getItemCount() {
        if (restaurants == null) {
            return attractions.size();
        } else if (attractions == null) {
            return restaurants.size();
        }
        return 0;
    }

    public void setUpdatedList(ArrayList<Restaurant> restraunts, ArrayList<Attraction> attractions)
    {
        if (attractions==null)
        {
            this.restaurants = restraunts;
        }
        else if (restraunts==null)
        {
            this.attractions = attractions;
        }
        notifyDataSetChanged();
    }

    class ReccomendationsHolder extends RecyclerView.ViewHolder
    {
        private TextView name, price,distance,type,address,status,ratingNum;

        private RatingBar ratingBar;

        private ImageView image;

        public ReccomendationsHolder(View v) {
            super(v);
            name = v.findViewById(R.id.recco_name);
            price = v.findViewById(R.id.recco_price);
            distance = v.findViewById(R.id.recco_distance);
            type = v.findViewById(R.id.recco_type);
            address = v.findViewById(R.id.recco_address);
            status = v.findViewById(R.id.recco_status);
            ratingNum = v.findViewById(R.id.recco_ratingnum);
            ratingBar = v.findViewById(R.id.recco_rating);
            image = v.findViewById(R.id.recco_image);

            v.setOnClickListener(new View.OnClickListener() {
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
