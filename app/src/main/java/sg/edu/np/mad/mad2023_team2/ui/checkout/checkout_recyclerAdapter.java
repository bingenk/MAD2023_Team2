package sg.edu.np.mad.mad2023_team2.ui.checkout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import sg.edu.np.mad.mad2023_team2.R;
import sg.edu.np.mad.mad2023_team2.ui.checkout_cart_sqllite.Cart_item;

//////////////This is the recycle adapter for the checkout recycle view////////////////////////
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
///////picasso helps to convert url and set it to the imageview specified/////////////////
        Picasso.with(holder.hotel_image.getContext())
                .load(checkout_cart.get(position).getImage())
                .into(holder.hotel_image);
        holder.hotel_name.setText(checkout_cart.get(position).getName());
        holder.hotel_type.setText(checkout_cart.get(position).getType());
//        holder.hotel_address.setText(checkout_cart.get(position).getAddress());
        holder.tv_checkin_date.setText(formatdate(checkout_cart.get(position).getCheckin_date()));
        holder.tv_checkout_date.setText(formatdate(checkout_cart.get(position).getCheckout_date()));
        //holder.imageView.setImageDrawable(); ~ to set images
        boolean isExpanded=checkout_cart.get(position).isExpanded();
        holder.tv_total_price.setText("$ "+  String.format("%.2f", checkout_cart.get(position).getPrice()));
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


    /////calculates the total price of the items in the cart///////////
    public double calculateTotalPrice() {
        double totalPrice = 0;
        for (Cart_item item : checkout_cart) {
            totalPrice += item.getPrice();
        }
        return totalPrice;
    }


    ///////method formats a Date variable to a string///////////////
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
            hotel_name=view.findViewById(R.id.hotel_name);
            hotel_type=view.findViewById(R.id.hotel_rating);

//            ib_delete_cart_item=view.findViewById(R.id.ib_delete_cart_item);
            //hotel_address=view.findViewById(R.id.hotel_address);
            //tv_checkin_date_title=itemView.findViewById(R.id.tv_checkin_date_title);
            tv_checkin_date=view.findViewById(R.id.tv_checkin_date);
            //tv_checkout_date_title=itemView.findViewById(R.id.tv_checkout_date_title);
            tv_checkout_date=view.findViewById(R.id.tv_checkout_date);
            expandableLayout=view.findViewById(R.id.cl_booking_details);
            tv_total_price=view.findViewById(R.id.tv_total_price_checkout);
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
///////////////////expands the cardview to display the dates and price////////////////
            clicktoexpand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int clickedPosition = getAdapterPosition();
                    Cart_item clickedItem = checkout_cart.get(clickedPosition);

                    // Check if the clicked item is already expanded
                    if (clickedItem.isExpanded()) {
                        // If it is already expanded, collapse it
                        clickedItem.setExpanded(false);
                        notifyItemChanged(clickedPosition);
                    } else {
                        // If it is not expanded, collapse the previously expanded item (if any)
                        int previouslyExpandedPosition = getExpandedItemPosition();
                        if (previouslyExpandedPosition != RecyclerView.NO_POSITION) {
                            Cart_item previouslyExpandedItem = checkout_cart.get(previouslyExpandedPosition);
                            previouslyExpandedItem.setExpanded(false);
                            notifyItemChanged(previouslyExpandedPosition);
                        }

                        // Expand the clicked item
                        clickedItem.setExpanded(true);
                        notifyItemChanged(clickedPosition);
                    }
                }
            });



        }

        // Method to get the position of the currently expanded item
        private int getExpandedItemPosition() {
            for (int i = 0; i < checkout_cart.size(); i++) {
                if (checkout_cart.get(i).isExpanded()) {
                    return i;
                }
            }
            return RecyclerView.NO_POSITION;
        }


        //getadapter position helps to get the position of the specific item in the list
        @Override
        public void onClick(View v) {

       //     Toast.makeText(v.getContext(), (CharSequence) checkout_cart.get(getAdapterPosition()), Toast.LENGTH_SHORT).show();
        }
    }
}
