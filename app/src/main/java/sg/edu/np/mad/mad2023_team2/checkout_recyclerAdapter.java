package sg.edu.np.mad.mad2023_team2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class checkout_recyclerAdapter extends RecyclerView.Adapter<checkout_recyclerAdapter.ViewHolder> {

//    private static final String TAG="RecyclerAdapter";
    List<String> checkout_cart;

    public checkout_recyclerAdapter(List<String> checkout_cart) {
        this.checkout_cart = checkout_cart;
    }

    //create individual rows necessary to display the items in recycler view
    //oncreateviewholder is only called as many views that can fit on the screen
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater =LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.checkout_objects,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;


    }
    //binds the view by taking in data and putting it in the view
    //onbindviewholder is called for the total number of views unline the viewholder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.rowCountTextView.setText(String.valueOf(position));
        holder.textView.setText(checkout_cart.get(position));

        //holder.imageView.setImageDrawable(); ~ to set images

    }
    //Represents the number of rows in the recycler view
    @Override
    public int getItemCount() {
        return checkout_cart.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imageView;
        TextView textView,rowCountTextView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imageView);
            textView=itemView.findViewById(R.id.textView);
            rowCountTextView=itemView.findViewById(R.id.rowCountTextView);

            itemView.setOnClickListener(this);
            // removes the view row on long click
            itemView.setOnLongClickListener(new View.OnLongClickListener(){


                @Override
                public boolean onLongClick(View v) {
                    checkout_cart.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    return true;
                }
            });
        }

        //getadapter position helps to get the position of the specific item in the list
        @Override
        public void onClick(View v) {
            Toast.makeText(v.getContext(), checkout_cart.get(getAdapterPosition()), Toast.LENGTH_SHORT).show();
        }
    }
}


