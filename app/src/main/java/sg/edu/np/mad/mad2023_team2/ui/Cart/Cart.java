package sg.edu.np.mad.mad2023_team2.ui.Cart;

import java.util.ArrayList;
import java.util.List;

import sg.edu.np.mad.mad2023_team2.ui.checkout_cart_sqllite.Cart_item;

public class Cart {
    private ArrayList<Cart_item> cartItems;

    public Cart() {
        // Required empty constructor for Firebase
    }

    public Cart(ArrayList<Cart_item> cartItems) {
        this.cartItems = cartItems;
    }

    public ArrayList<Cart_item> getCartItems() {
        return cartItems;
    }

    public void setCartItems(ArrayList<Cart_item> cartItems) {
        this.cartItems = cartItems;
    }
}
