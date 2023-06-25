package sg.edu.np.mad.mad2023_team2.ui.BookingAttraction.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import sg.edu.np.mad.mad2023_team2.R;
import sg.edu.np.mad.mad2023_team2.ui.BookingAttraction.RecyclerViewInterface;
import sg.edu.np.mad.mad2023_team2.ui.BookingAttraction.models.Item;
import sg.edu.np.mad.mad2023_team2.ui.BookingAttraction.models.Trip;

public class TripAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;
    private List<Item> items;

    public TripAdapter(List<Item> items, RecyclerViewInterface recyclerViewInterface) {
        this.items = items;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == 0)
        {
            return new TripViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_trip, parent, false), recyclerViewInterface);
        }
        else
        {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(getItemViewType(position) == 0){
            Trip trip = (Trip) items.get(position).getObject();
            ((TripViewHolder) holder).setTripDate(trip);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getType();
    }

    static class TripViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView imageTrip;
        private TextView textTripTitle, textTrip;

        TripViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface)
        {
            super(itemView);
            imageTrip = itemView.findViewById(R.id.imageTrip);
            textTripTitle = itemView.findViewById(R.id.textTripTitle);
            textTrip = itemView.findViewById(R.id.textTrip);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    if (recyclerViewInterface != null){
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION){



                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });
        }

        void setTripDate(Trip trip)
        {
            imageTrip.setImageResource(trip.getTripImage());
            textTripTitle.setText(trip.getTripTitle());
            textTrip.setText(trip.getTrip());
        }
    }


}
