package sg.edu.np.mad.mad2023_team2.ui.CarparkAvailability;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import sg.edu.np.mad.mad2023_team2.R;
import sg.edu.np.mad.mad2023_team2.ui.BookingAttraction.RecyclerViewInterface;

public class CarparkAdapter extends RecyclerView.Adapter<CarparkAdapter.MyViewHolder> {

    private final RecyclerViewInterface recyclerViewInterface;
    private List<CarparkDetails> details;

    public CarparkAdapter(List<CarparkDetails> details, RecyclerViewInterface recyclerViewInterface){
        this.recyclerViewInterface = recyclerViewInterface;
        this.details = details;
    }

    @NonNull
    @Override
    public CarparkAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CarparkAdapter.MyViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.carpark_row, parent, false), recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull CarparkAdapter.MyViewHolder holder, int position) {
        CarparkDetails carparkDetails = details.get(position);
        ((CarparkAdapter.MyViewHolder) holder).setCarparkDate(carparkDetails);
    }

    @Override
    public int getItemCount() {

        return details.size();
    }

     static class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView development, area, number;

        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            development = itemView.findViewById(R.id.textViewCar13);
            area = itemView.findViewById(R.id.textViewCar8);
            number = itemView.findViewById(R.id.textViewCar14);


            itemView.setOnClickListener(v -> {
                if (recyclerViewInterface != null){
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION){
                        recyclerViewInterface.onItemClick(pos);
                    }
                }
            });
        }
        void setCarparkDate(CarparkDetails set)
        {
            development.setText(set.getDevelopment());
            area.setText(set.getArea());
            number.setText(String.valueOf(set.getAvailableLots()));
        }
    }
}
