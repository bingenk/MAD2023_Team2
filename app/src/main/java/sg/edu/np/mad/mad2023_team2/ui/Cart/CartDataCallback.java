package sg.edu.np.mad.mad2023_team2.ui.Cart;

import java.util.ArrayList;

import sg.edu.np.mad.mad2023_team2.ui.checkout_cart_sqllite.Cart_item;

public interface CartDataCallback {
    void onCartDataLoaded(ArrayList<Cart_item> cartItems);
    void onCancelled(String errorMessage);
}
