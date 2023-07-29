package sg.edu.np.mad.mad2023_team2.ui.Cart;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import sg.edu.np.mad.mad2023_team2.R;
import sg.edu.np.mad.mad2023_team2.ui.Currency_Converter.Get_Currency_Of_App;
import sg.edu.np.mad.mad2023_team2.ui.cart_sqllite_database.DataBaseHelper;
import sg.edu.np.mad.mad2023_team2.ui.checkout_cart_sqllite.Cart_item;


////////CHECKOUT CART RECYCLE ADAPTER is for the recycle view in the checkout cart fragment/////////////
public class checkout_cart_recyclerAdapter extends RecyclerView.Adapter<checkout_cart_recyclerAdapter.ViewHolder> {
    public static final String CART_ITEM_TABLE = "CART_ITEM_TABLE";

   private static final String TAG="RecyclerAdapter";
    ArrayList<Cart_item> checkout_cart;
    Context context;

    private TextView totalChargesTextView;

    private String Currency_Code;


    private double conversion_Rate;



    ///////The checkout cart recyler has two paramters , the Text view is added so that the total price text view can be updated when an item from the cart is deleted///////

    public checkout_cart_recyclerAdapter(Context context, ArrayList<Cart_item> checkout_cart, TextView totalChargesTextView) {
        this.context = context;
        this.checkout_cart = checkout_cart;
        this.totalChargesTextView = totalChargesTextView;
    }



    //create individual rows necessary to display the items in recycler view
    //oncreateviewholder is only called as many views that can fit on the screen
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater =LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.cart_checkout_objects,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);

        return viewHolder;


    }
    //binds the view by taking in data and putting it in the view
    //onbindviewholder is called for the total number of views unline the viewholder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int Position = position;


        //Currency_conversion_praveen

        Currency_Code = Get_Currency_Of_App.getcountrycodesharedprefs(context);
        conversion_Rate=Get_Currency_Of_App.getconversionratesharedprefs(context);

     Cart_item model =checkout_cart.get(Position);
//        Picasso.with(context).load(checkout_cart.get(Position).getImage()).into(holder.hotel_image);
        Picasso.with(holder.hotel_image.getContext())
                .load(checkout_cart.get(Position).getImage())
                .into(holder.hotel_image);
        holder.hotel_name.setText(checkout_cart.get(Position).getName());
        holder.hotel_type.setText(checkout_cart.get(Position).getType());
        holder.tv_total_price.setText(Currency_Code+ " "+ String.format("%.2f", checkout_cart.get(Position).getPrice()*conversion_Rate));
//        holder.hotel_address.setText(checkout_cart.get(Position).getAddress());
        holder.tv_checkin_date.setText(formatdate(checkout_cart.get(Position).getCheckin_date()));
        holder.tv_checkout_date.setText(formatdate(checkout_cart.get(Position).getCheckout_date()));
        //holder.imageView.setImageDrawable(); ~ to set images

        ////// The two lines of code below checks if the contranit layout containing gthe date and price of the hotel is expanded and setting the visibilty according to onclick////////
        boolean isExpanded=checkout_cart.get(Position).isExpanded();
        holder.expandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);


        ///////shows a popup containing the details of the hotel when the details textview is clicked/////////////
        holder.details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                View dialogView=LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.custom_popup_cart_item,null);
                TextView name = dialogView.findViewById(R.id.booking_name);


                ///////////pop up///////////////
                TextView pop_up_name = dialogView.findViewById(R.id.booking_name);
                TextView pop_up_desc = dialogView.findViewById(R.id.booking_config);
                TextView pop_up_address=dialogView.findViewById(R.id.tv_address_input);

                ImageView pop_up_image = dialogView.findViewById(R.id.booking_image);
                TextView pop_up_price = dialogView.findViewById(R.id.booking_price);

                Picasso.with(pop_up_image.getContext())
                        .load(checkout_cart.get(Position).getImage())
                        .into(pop_up_image);

                pop_up_name.setText(checkout_cart.get(Position).getName());
                pop_up_desc.setText(checkout_cart.get(Position).getDescription());
                pop_up_address.setText(checkout_cart.get(Position).getAddress());
                pop_up_price.setText(Currency_Code+" "+  String.format("%.2f", (checkout_cart.get(Position).getPrice())*conversion_Rate));
                builder.setView(dialogView);
                builder.setCancelable(true);
                builder.show();
            }
        });







    }


    ///////// method helps to calculate the total price of the cart///////
    public double calculateTotalPrice() {
        double totalPrice = 0;
        for (Cart_item item : checkout_cart) {
            totalPrice += item.getPrice();
        }
        return totalPrice;
    }

//////////////method helps to format a Date variable to a string///////////////
    public String formatdate(Date y){
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy");
         String stringDate= DateFor.format(y);
         return stringDate;}

    //Represents the number of rows in the recycler view///
    @Override
    public int getItemCount() {

        return checkout_cart.size();

    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView hotel_image;
        TextView hotel_name,hotel_type,hotel_address,tv_checkin_date_title,tv_checkin_date,tv_checkout_date_title,tv_checkout_date,tv_total_price,details;
        ImageButton ib_delete_cart_item;

        LinearLayout clicktoexpand;

        ConstraintLayout expandableLayout;




        public ViewHolder(@NonNull View view) {

            super(view);
            ////////////initialise the views/////////////

            hotel_image=view.findViewById(R.id.hotel_image);
            hotel_name=view.findViewById(R.id.hotel_name);
            hotel_type=view.findViewById(R.id.hotel_type);
            ib_delete_cart_item=view.findViewById(R.id.ib_delete_cart_item);
//            hotel_address=view.findViewById(R.id.hotel_address);
            //tv_checkin_date_title=itemView.findViewById(R.id.tv_checkin_date_title);
            tv_checkin_date=view.findViewById(R.id.tv_checkin_date);
            //tv_checkout_date_title=itemView.findViewById(R.id.tv_checkout_date_title);
            tv_checkout_date=view.findViewById(R.id.tv_checkout_date);
            expandableLayout=view.findViewById(R.id.cl_booking_details);
            clicktoexpand=view.findViewById(R.id.ll_cart_checkout_item_box);
            details=view.findViewById(R.id.tv_more_details);
            tv_total_price=view.findViewById(R.id.tv_total_price_cart);






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
//            });
//
//            ib_delete_cart_item.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    checkout_cart.remove(getAdapterPosition());
//                    notifyItemRemoved(getAdapterPosition());
//
//                }
//            });

            ///////////////deletes the cart item from the list and the sqllite database and updates the total price//////////////
           ib_delete_cart_item.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {


                    DataBaseHelper dataBaseHelper = new DataBaseHelper(ib_delete_cart_item.getRootView().getContext());
                    TextView total_charges=view.findViewById(R.id.textView2);
                    boolean success = dataBaseHelper.deleteOne(checkout_cart.get(getAdapterPosition()));
                    checkout_cart.remove(getAdapterPosition());

                    // get username from shared prefs to use in firebase
                    SharedPreferences sharedPreferences = context.getSharedPreferences("CartFb", MODE_PRIVATE);
                    String username = sharedPreferences.getString("username", ""); // The second argument is the default value if the key is not found
                    Log.d("wassup", "ShowCustomersOnListView: firebase cart mfker");
                    Log.d("wassup", username);
                    addCartToUser(username,checkout_cart);

                    double totalCharges = calculateTotalPrice();

                    totalChargesTextView.setText(String.format("%.2f", totalCharges));

                    notifyDataSetChanged();


                }
            });


//////////////////expands the constrint layout in the cardview on click and displays the checkin,checkout dates and the price of the hotel//////////
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
//
//            Toast.makeText(v.getContext(), (CharSequence) checkout_cart.get(getAdapterPosition()), Toast.LENGTH_SHORT).show();
        }


        public void addCartToUser(String userId, ArrayList<Cart_item> cartItems) {
//        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//        String userId;

//        if (currentUser != null) {
//            // User is authenticated, get the user ID
//            userId = currentUser.getUid();
//            Log.d("wassup", "addCartToUser: User ID: " + userId);
//        } else {
//            // User is not authenticated, use a default value for the user ID (you can change this as needed)
//            userId = "default_user";
//            Log.d("wassup", "addCartToUser: User is not authenticated.");
//        }

            DatabaseReference userCartRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("cart");
            Cart cart = new Cart(cartItems);
            userCartRef.setValue(cart)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d("wassup", "addCartToUser: Cart added to the user successfully.");
                            // Cart added to the user successfully.
                            // You can show a success message if needed.
                        } else {
                            Log.d("wassup", "addCartToUser: Cart addition failed: " + task.getException());
                            // Cart addition failed.
                            // You can handle the error accordingly.
                        }
                    })
                    .addOnSuccessListener(aVoid -> Log.d("wassup", "addCartToUser: Database write was successful."))
                    .addOnFailureListener(e -> Log.d("wassup", "addCartToUser: Database write failed: " + e.getMessage()));
        }



    }


}


