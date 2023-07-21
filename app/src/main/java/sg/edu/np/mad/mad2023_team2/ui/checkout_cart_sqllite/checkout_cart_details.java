package sg.edu.np.mad.mad2023_team2.ui.checkout_cart_sqllite;


import java.util.ArrayList;
import java.util.List;


/////////////this class has two properties , the array list to contain all the items in the cart , and the double property to contain the total price of all the items in the cart/////
public class checkout_cart_details {

    ArrayList<Cart_item> allcartitems= new ArrayList<>();
    Double totalprice;

    /// constructor/////
    public checkout_cart_details(ArrayList<Cart_item> allcartitems, Double totalprice) {
        this.allcartitems = allcartitems;
        this.totalprice = totalprice;
    }


    /////////getters and setters///////////
    public ArrayList<Cart_item> getAllcartitems() {
        return allcartitems;
    }

    public void setAllcartitems(ArrayList<Cart_item> allcartitems) {
        this.allcartitems = allcartitems;
    }

    public Double getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(Double totalprice) {
        this.totalprice = totalprice;
    }
}
