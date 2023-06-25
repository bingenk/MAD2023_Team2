package sg.edu.np.mad.mad2023_team2.ui.checkout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import sg.edu.np.mad.mad2023_team2.R;
import sg.edu.np.mad.mad2023_team2.ui.cart_sqllite_database.DataBaseHelper;
import sg.edu.np.mad.mad2023_team2.ui.checkout_cart_sqllite.Cart_item;


public class checkout_recyclerAdapter extends RecyclerView.Adapter<checkout_recyclerAdapter.ViewHolder> {



    private static final String TAG="RecyclerAdapter";
    List<Cart_item> checkout_cart;
    private TextView totalChargesTextView;

    public checkout_recyclerAdapter(List<Cart_item> checkout_cart,TextView totalChargesTextView) {

        this.checkout_cart = checkout_cart;
        this.totalChargesTextView = totalChargesTextView;
    }

    //create individual rows necessary to display the items in recycler view
    //oncreateviewholder is only called as many views that can fit on the screen
    @NonNull
    @Override
    public checkout_recyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater =LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.checkout_item_layout,parent,false);
        checkout_recyclerAdapter.ViewHolder viewHolder=new checkout_recyclerAdapter.ViewHolder(view);

        return viewHolder;


    }
    //binds the view by taking in data and putting it in the view
    //onbindviewholder is called for the total number of views unline the viewholder
    @Override
    public void onBindViewHolder(@NonNull checkout_recyclerAdapter.ViewHolder holder, int position) {

        holder.hotel_image.setImageDrawable(null);
        holder.hotel_name.setText(checkout_cart.get(position).getName());
        holder.hotel_type.setText(checkout_cart.get(position).getType());
//        holder.hotel_address.setText(checkout_cart.get(position).getAddress());
        holder.tv_checkin_date.setText(formatdate(checkout_cart.get(position).getCheckin_date()));
        holder.tv_checkout_date.setText(formatdate(checkout_cart.get(position).getCheckout_date()));
        //holder.imageView.setImageDrawable(); ~ to set images
        boolean isExpanded=checkout_cart.get(position).isExpanded();
        holder.expandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
//        holder.details.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
//                View dialogView=LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.custom_popup_cart_item,null);
//                builder.setView(dialogView);
//                builder.setCancelable(true);
//                builder.show();
//            }
//        });



    }
    public double calculateTotalPrice() {
        double totalPrice = 0;
        for (Cart_item item : checkout_cart) {
            totalPrice += item.getPrice();
        }
        return totalPrice;
    }
    public String formatdate(Date y){
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy");
        String stringDate= DateFor.format(y);
        return stringDate;}

    //Represents the number of rows in the recycler view
    @Override
    public int getItemCount() {

        return checkout_cart.size();

    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView hotel_image;
        TextView hotel_name,hotel_type,hotel_address,tv_checkin_date_title,tv_checkin_date,tv_checkout_date_title,tv_checkout_date,tv_total_price,details;
//        ImageButton ib_delete_cart_item;

        LinearLayout clicktoexpand;

        ConstraintLayout expandableLayout;
        public ViewHolder(@NonNull View view) {

            super(view);

            hotel_image=view.findViewById(R.id.hotel_image);
            hotel_name=view.findViewById(R.id.hotel_type);
            hotel_type=view.findViewById(R.id.hotel_name);
//            ib_delete_cart_item=view.findViewById(R.id.ib_delete_cart_item);
            //hotel_address=view.findViewById(R.id.hotel_address);
            //tv_checkin_date_title=itemView.findViewById(R.id.tv_checkin_date_title);
            tv_checkin_date=view.findViewById(R.id.tv_checkin_date);
            //tv_checkout_date_title=itemView.findViewById(R.id.tv_checkout_date_title);
            tv_checkout_date=view.findViewById(R.id.tv_checkout_date);
            expandableLayout=view.findViewById(R.id.cl_booking_details);
            clicktoexpand=view.findViewById(R.id.ll_cart_checkout_item_box);
//            details=view.findViewById(R.id.tv_more_details);


            // removes the view row on long click
//            view.setOnLongClickListener(new View.OnLongClickListener(){
//
//
//                @Override
//                public boolean onLongClick(View v) {
//
//                    checkout_cart.remove(getAdapterPosition());
//                    notifyItemRemoved(getAdapterPosition());
//                    return true;
//                }
//
//
//
//            });

//            ib_delete_cart_item.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//
//
//                    DataBaseHelper dataBaseHelper = new DataBaseHelper(ib_delete_cart_item.getRootView().getContext());
//                    TextView total_charges=view.findViewById(R.id.tv_total_price_caluclated_display);
//                    boolean success = dataBaseHelper.deleteOne(checkout_cart.get(getAdapterPosition()));
//                    checkout_cart.remove(getAdapterPosition());
//                    double totalCharges = calculateTotalPrice();
//                    totalChargesTextView.setText(String.valueOf(totalCharges));;
//
//                    notifyDataSetChanged();
//
//
//                }
//            });

            clicktoexpand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Cart_item item = checkout_cart.get(getAdapterPosition());
                    item.setExpanded(!item.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });

        }

        //getadapter position helps to get the position of the specific item in the list
        @Override
        public void onClick(View v) {

            Toast.makeText(v.getContext(), (CharSequence) checkout_cart.get(getAdapterPosition()), Toast.LENGTH_SHORT).show();
        }
    }
}
