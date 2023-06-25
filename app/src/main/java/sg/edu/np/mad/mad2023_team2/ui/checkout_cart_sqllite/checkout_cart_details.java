package sg.edu.np.mad.mad2023_team2.ui.checkout_cart_sqllite;


import java.util.ArrayList;
import java.util.List;

public class checkout_cart_details {

    ArrayList<Cart_item> allcartitems= new ArrayList<>();
    Double totalprice;

    public checkout_cart_details(ArrayList<Cart_item> allcartitems, Double totalprice) {
        this.allcartitems = allcartitems;
        this.totalprice = totalprice;
    }

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
