package sg.edu.np.mad.mad2023_team2.ui.checkout;

import java.util.ArrayList;

import sg.edu.np.mad.mad2023_team2.ui.checkout_cart_sqllite.Cart_item;


public class checkout_details
{
    ArrayList<Cart_item> checkout_items = new ArrayList<>();

    double total_price;
    String first_name;
    String last_name;
    String email_address;
    String country_of_residence;

    String phone_number;
    String guest_first_name;
    String guest_last_name;
    String guest_country_of_residence;

    public checkout_details(ArrayList<Cart_item> checkout_items, double total_price, String first_name, String last_name, String email_address, String country_of_residence,  String phone_number, String guest_first_name, String guest_last_name, String guest_country_of_residence) {
        this.checkout_items = checkout_items;
        this.total_price = total_price;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email_address = email_address;
        this.country_of_residence = country_of_residence;
        this.phone_number = phone_number;
        this.guest_first_name = guest_first_name;
        this.guest_last_name = guest_last_name;
        this.guest_country_of_residence = guest_country_of_residence;
    }


    public ArrayList<Cart_item> getCheckout_items() {
        return checkout_items;
    }

    public void setCheckout_items(ArrayList<Cart_item> checkout_items) {
        this.checkout_items = checkout_items;
    }

    public double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail_address() {
        return email_address;
    }

    public void setEmail_address(String email_address) {
        this.email_address = email_address;
    }

    public String getCountry_of_residence() {
        return country_of_residence;
    }

    public void setCountry_of_residence(String country_of_residence) {
        this.country_of_residence = country_of_residence;
    }


    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getGuest_first_name() {
        return guest_first_name;
    }

    public void setGuest_first_name(String guest_first_name) {
        this.guest_first_name = guest_first_name;
    }

    public String getGuest_last_name() {
        return guest_last_name;
    }

    public void setGuest_last_name(String guest_last_name) {
        this.guest_last_name = guest_last_name;
    }

    public String getGuest_country_of_residence() {
        return guest_country_of_residence;
    }

    public void setGuest_country_of_residence(String guest_country_of_residence) {
        this.guest_country_of_residence = guest_country_of_residence;
    }

    @Override
    public String toString() {
        return "checkout_details{" +
                "checkout_items=" + checkout_items +
                ", total_price=" + total_price +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", email_address='" + email_address + '\'' +
                ", country_of_residence='" + country_of_residence + '\'' +
                ", phone_number='" + phone_number + '\'' +
                ", guest_first_name='" + guest_first_name + '\'' +
                ", guest_last_name='" + guest_last_name + '\'' +
                ", guest_country_of_residence='" + guest_country_of_residence + '\'' +
                '}';
    }
}
