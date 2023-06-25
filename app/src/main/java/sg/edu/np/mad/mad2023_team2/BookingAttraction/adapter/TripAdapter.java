package sg.edu.np.mad.mad2023_team2.BookingAttraction.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import sg.edu.np.mad.mad2023_team2.R;
import sg.edu.np.mad.mad2023_team2.BookingAttraction.RecyclerViewInterface;
import sg.edu.np.mad.mad2023_team2.BookingAttraction.models.Item;
import sg.edu.np.mad.mad2023_team2.BookingAttraction.models.Trip;

public class TripAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ////////////////////////////////////////////
    //        DEFINE Objects in class         //
    ////////////////////////////////////////////
    private final RecyclerViewInterface recyclerViewInterface;
    private List<Item> items;

    ///////////////////////////////
    //        CONSTRUCTOR        //
    ///////////////////////////////
    public TripAdapter(List<Item> items, RecyclerViewInterface recyclerViewInterface) {
        this.items = items;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    //////////////////////////////////////////////
    //       OnCreateViewHolder Function        //
    //////////////////////////////////////////////

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {      // ViewHolder Operator for RecyclerViewAdapter

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

    ////////////////////////////////////
    //        OTHER OPERATORS         //
    ////////////////////////////////////

    @Override
    public int getItemCount() {return items.size();}

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getType();
    }


    ////////////////////////////////////////////////////////////////////////////////////////////
    //        SET VALUES in ARRAY to RecyclerViewContents via item_container_trip.xml         //
    ////////////////////////////////////////////////////////////////////////////////////////////
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
            Picasso.get().load(trip.getTripImage()).into(imageTrip);   // Set Values via Picasso Library and setText()
            textTripTitle.setText(trip.getTripTitle());
            textTrip.setText(trip.getTrip());
        }
    }


}
