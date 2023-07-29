package sg.edu.np.mad.mad2023_team2.ui.Cart;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import sg.edu.np.mad.mad2023_team2.ui.MainActivity;
import sg.edu.np.mad.mad2023_team2.ui.cart_sqllite_database.DataBaseHelper;
import sg.edu.np.mad.mad2023_team2.ui.cart_sqllite_database.DatabaseManager;
import sg.edu.np.mad.mad2023_team2.ui.checkout_cart_sqllite.Cart_item;

public class ApiManager {

    private static ApiManager instance;
    private Context context;
    private boolean apiCalled;
    DataBaseHelper dataBaseHelper;

    private ApiManager(Context context) {
        this.context = context.getApplicationContext();
        this.apiCalled = false;
    }

    public static synchronized ApiManager getInstance(Context context) {
        if (instance == null) {
            instance = new ApiManager(context);
        }
        return instance;
    }

    // Add your API callback method here
    public void makeApiCall() {
        if (!apiCalled) {
            dataBaseHelper = DatabaseManager.getDataBaseHelper(context);
            Addcartfromfb(dataBaseHelper);
            apiCalled = true;
        }
    }

    // Method to reset the apiCalled variable
    public void resetApiCalled() {
        apiCalled = false;
    }

    private void getCartData(String userId, CartDataCallback callback) {
        DatabaseReference userCartRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("cart");

        userCartRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Cart cart = snapshot.getValue(Cart.class);
                    if (cart != null) {
                        Log.d("wassup", "ShowCustomersOnListView: firebase cart mfker3");
                        callback.onCartDataLoaded(cart.getCartItems());
                    } else {
                        Log.d("wassup", "ShowCustomersOnListView: firebase cart mfker4");
                        callback.onCancelled("Cart data is null");
                    }
                } else {
                    Log.d("wassup", "ShowCustomersOnListView: firebase cart mfker5");
                    callback.onCancelled("Cart data does not exist for the user.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onCancelled("Error reading cart data: " + error.getMessage());
            }
        });
    }

    private void Addcartfromfb(DataBaseHelper dataBaseHelper) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("CartFb", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", ""); // The second argument is the default value if the key is not found
        Log.d("wassup", "ShowCustomersOnListView: firebase cart mfker10");
        Log.d("wassup", username);

        getCartData(username, new CartDataCallback() {
            @Override
            public void onCartDataLoaded(ArrayList<Cart_item> cartItems) {
                if (cartItems != null) {
                    for (Cart_item i : cartItems) {
                        Log.d("wassup", "ShowCustomersOnListView: firebase cart mfker1");
                        dataBaseHelper.addOne(i);
                    }
                } else {
                    Log.d("wassup", "ShowCustomersOnListView: firebase cart mfker2");
                    Toast.makeText(context, "Cart from firebase was empty", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(String errorMessage) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }


}
