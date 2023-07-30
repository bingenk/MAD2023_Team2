package sg.edu.np.mad.mad2023_team2.ui.Favorites;


import android.content.Context;
import android.util.Log;
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

    private OnLongClickInterface onClickInterface;
    private ArrayList<Object> data;
    private Context context;

    public FavoritesAdapter(OnLongClickInterface onClickInterface, ArrayList<Object> data, Context context) {
        this.onClickInterface = onClickInterface;
        this.data = data;
        this.context = context;
    }

    public void updateAdapter(ArrayList<Object> newList)
    {
        this.data = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FavoritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.accommodations_layout, parent, false);
        return new FavoritesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesViewHolder holder, int position) {
        if (data.get(position) instanceof Favourites)
        {
            Favourites favourites = (Favourites) data.get(position);
            Picasso.with(context).load(favourites.getImage()).fit().centerCrop().into(holder.image);
            holder.name.setText(favourites.getName());
            if (favourites.getStringPrice() != null)
            {
                holder.price.setText(favourites.getStringPrice());
            }
            else
            {
                holder.price.setVisibility(View.GONE);
            }
            holder.address.setText(favourites.getAddress());
            holder.type.setText(favourites.getItemType());
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
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
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickInterface != null)
                    {
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION)
                        {
                            onClickInterface.OnClick(pos);
                        }
                    }
                }
            });
            item.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v)
                {
                    if (onClickInterface != null)
                    {
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION)
                        {
                            onClickInterface.OnLongClick(pos);
                        }
                    }
                    return true;
                }
            });
        }
    }
}
