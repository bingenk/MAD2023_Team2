package sg.edu.np.mad.mad2023_team2.ui.checkout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.hbb20.CountryCodePicker;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import papaya.in.sendmail.SendMail;
import sg.edu.np.mad.mad2023_team2.R;
import sg.edu.np.mad.mad2023_team2.ui.Cart.checkout_cart_recyclerAdapter;
import sg.edu.np.mad.mad2023_team2.ui.cart_sqllite_database.DataBaseHelper;
import sg.edu.np.mad.mad2023_team2.ui.cart_sqllite_database.DatabaseManager;
import sg.edu.np.mad.mad2023_team2.ui.checkout_cart_sqllite.Cart_item;
import sg.edu.np.mad.mad2023_team2.ui.checkout_cart_sqllite.checkout_cart_details;

public class Checkout extends AppCompatActivity {


    RecyclerView rv;


    TextView  total_price_title,total_price_input;

    checkout_recyclerAdapter recyclerAdapter;
    ArrayList<Cart_item> checkout_cart;
    CountryCodePicker country_picker, country_code_picker, guest_country_picker;



    double total_price_stripe;



    TextInputLayout  First_name_input_layout,Last_name_input_layout, Email_input_layout,Phone_number_layout,  Guest_First_name_layout,Guest_Last_name_layout;

    TextInputEditText    First_name_input_edit_text,Last_name_input_edit_text, Email_input_edit_text, Phone_number_edit_text,Guest_First_name_edit_text, Guest_Last_name_edit_text;

    String customerID,EphericalKey,ClientSecret;

    Button paymentbutton;



    String SECRET_KEY = "sk_test_51NL1IVArVetI75gXAaWfW0ZIEbRwNCbmDvI692u6W5EPaXtOkBRoK7OTwAc5xbcjawcfrKooy5j8hUMQC7qGT87s00XVfKw9F3";
    String PUBLISH_KEY = "pk_test_51NL1IVArVetI75gX1NpOAdurG9Ojti56zfKjGD3VBxFUEDkLZmijmiy0St2SYRPMHpE62mwYikY4tauIftaFsUno00TMiWl405";

    PaymentSheet paymentSheet;

    DataBaseHelper dataBaseHelper;
    TextView total_price_calc_display;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        initalizeViews();



        //RECYCLER VIEW

//        checkout_cart = generateHotels();


        dataBaseHelper = DatabaseManager.getDataBaseHelper(Checkout.this);
        ShowCustomersOnListView(dataBaseHelper);




        //STRIPE PAYMENT

        PaymentConfiguration.init(this, PUBLISH_KEY);

        paymentSheet = new PaymentSheet(this, paymentSheetResult -> {
            onPaymentResult(paymentSheetResult);

        });

        paymentbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateeverything();
                if ( validateeverything()){
                    ///customer details////
                           String First_name= First_name_input_edit_text.getText().toString();
                           String Last_name= Last_name_input_edit_text.getText().toString();
                           String Email= Email_input_edit_text.getText().toString();
                           String residing_country=country_picker.getSelectedCountryName();
                           String Phone_number= country_code_picker.getFormattedFullNumber();

                           String Guest_First_Name= Guest_First_name_edit_text.getText().toString();
                           String Guest_Last_Name= Guest_Last_name_edit_text.getText().toString();
                           String Guest_residing_country=guest_country_picker.getSelectedCountryName();
//                            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
//                            SharedPreferences.Editor editor = sharedPreferences.edit();
//                            editor.putString("total_price", "300");
//                            editor.apply();
                           checkout_details checkout=new checkout_details(checkout_cart,300,First_name,Last_name,Email,residing_country,Phone_number,Guest_First_Name,Guest_Last_Name,Guest_residing_country);
//                    SendMail mail = new SendMail(config.EMAIL, "14Q283362r04",
//                            "pvss1427n@gmail.com",
//                            "Testing Email Sending",
//                            "Yes, it's working well\nI will use it always.");
//                    mail.execute();
                    Toast.makeText(Checkout.this, checkout.toString(), Toast.LENGTH_LONG).show();
                    PaymentFlow();
                }
            }
        });
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://api.stripe.com/v1/customers", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    customerID = object.getString("id");


                    getEphericalKey(customerID);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer " + SECRET_KEY);
                return header;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Checkout.this);
        requestQueue.add(stringRequest);


        country_code_picker.setPhoneNumberValidityChangeListener(new CountryCodePicker.PhoneNumberValidityChangeListener() {
            @Override
            public void onValidityChanged(boolean isValidNumber) {
                // your code
            }
        });




        Phone_number_edit_text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    country_code_picker.setHintExampleNumberEnabled(true);
                } else {
                    Phone_number_edit_text.setHint("");
                }
            }
        });
    }










    private void onPaymentResult(PaymentSheetResult paymentSheetResult) {
        if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            Toast.makeText(this, "payment success", Toast.LENGTH_SHORT).show();
        }
    }

    private void getEphericalKey(String customerID) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://api.stripe.com/v1/ephemeral_keys", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    EphericalKey = object.getString("id");


                    Toast.makeText(Checkout.this, EphericalKey, Toast.LENGTH_SHORT).show();
                    getCientSecret(customerID, EphericalKey);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer " + SECRET_KEY);
                header.put("Stripe-Version", "2022-11-15");
                return header;
            }


            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("customer", customerID);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Checkout.this);
        requestQueue.add(stringRequest);

    }

    private void getCientSecret(String customerID, String ephericalKey) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://api.stripe.com/v1/payment_intents", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    ClientSecret = object.getString("client_secret");

                    Toast.makeText(Checkout.this, ClientSecret, Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer " + SECRET_KEY);
                return header;
            }


            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
//                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
//                String myValue = sharedPreferences.getString("total_cost", "0");
                dataBaseHelper = DatabaseManager.getDataBaseHelper(Checkout.this);
                checkout_cart_details calculate_total_price_stripe=dataBaseHelper.getEveryone();
                total_price_stripe =calculate_total_price_stripe.getTotalprice();
                String ts = String.valueOf(total_price_stripe);

                Map<String, String> params = new HashMap<>();
                params.put("customer", customerID);
                params.put("amount",ts+ "00");
                params.put("currency", "usd");
                params.put("automatic_payment_methods[enabled]", "true");


                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Checkout.this);
        requestQueue.add(stringRequest);


    }



    private void PaymentFlow() {
        paymentSheet.presentWithPaymentIntent(
                ClientSecret,new PaymentSheet.Configuration("NPTRAVEL",new PaymentSheet.CustomerConfiguration(
                        customerID,
                        EphericalKey
                ))
        );
    }


    private void  initalizeViews(){
        //recycler view//
        rv = findViewById(R.id.rv_checkout_items);

        //price details//
        total_price_title=findViewById(R.id.total_price);
        total_price_input=findViewById(R.id.total_price_input);


        //contact details//

        First_name_input_layout=findViewById(R.id.First_Name_Input_layout);
        First_name_input_edit_text=findViewById(R.id.First_Name_Input);


        Last_name_input_layout=findViewById(R.id.Last_Name_Input_layout);
        Last_name_input_edit_text=findViewById(R.id.Last_Name_Input);


        Email_input_layout=findViewById(R.id.Email_Input_layout);
        Email_input_edit_text=findViewById(R.id.Email_Input);

        //country picker//
        country_picker=findViewById(R.id.country_picker);

        //phonenumber//
        country_code_picker=findViewById(R.id.country_code_picker);
        Phone_number_layout=findViewById(R.id.til_country_code_selector);
        Phone_number_edit_text=findViewById(R.id.Phone_Number_Input);


        ////Guest information///

           //firstname//
        Guest_First_name_layout=findViewById(R.id.Guest_Information_First_Name_layout);
        Guest_First_name_edit_text=findViewById(R.id.Guest_Information_First_Name);
          //lastname//
        Guest_Last_name_layout=findViewById(R.id.Guest_Information_Last_Name_layout);
        Guest_Last_name_edit_text=findViewById(R.id.Guest_Information_Last_Name);

        //guest country of residence//

        guest_country_picker=findViewById(R.id.Guest_information_country_picker);



   //payment button//
     paymentbutton=findViewById(R.id.btn_proceed_to_payment);


     ///attaches edit text to ccp for phone number///
     country_code_picker.registerCarrierNumberEditText(Phone_number_edit_text);
    }

    private boolean isValidEmail(String email) {
        // Email validation regex pattern
        String emailPattern = "^[a-z0-9._-]+@[a-z]+\\.[a-z]+$";

        return email.matches(emailPattern);
    }


    private boolean validateeverything(){
        String firstName =First_name_input_edit_text.getText().toString().trim();
        String lastName = Last_name_input_edit_text.getText().toString().trim();
        String emailInput =  Email_input_edit_text.getText().toString().trim();
        String phoneNumber = Phone_number_edit_text.getText().toString().trim();
        String guestFirstName = Guest_First_name_edit_text.getText().toString().trim();
        String guestLastName = Guest_Last_name_edit_text.getText().toString().trim();

        boolean isValid = true;

        if (firstName.isEmpty()) {
            First_name_input_edit_text.setError("Please enter your first name");
            isValid = false;
        }

        if (lastName.isEmpty()) {
            Last_name_input_edit_text.setError("Please enter your last name");
            isValid = false;
        }

        if (emailInput.isEmpty()){
            Email_input_edit_text.setError("Field can't be empty");
            return false;
        } else if (!isValidEmail(emailInput)) {
            Email_input_edit_text.setError("Please enter a valid email address\n" +
                    "**Note** Only small case letters allowed.");
            return false;
        }


        if (phoneNumber.isEmpty()) {
            Phone_number_edit_text.setError("Please enter your phone number");
            isValid = false;
        }
        if (!phoneNumber.isEmpty()) {
            isValid=country_code_picker.isValidFullNumber();
            if (!isValid){
                Phone_number_edit_text.setError("Enter a valid phone number !");
            }
        }

        if (guestFirstName.isEmpty()) {
            Guest_First_name_edit_text.setError("Please enter guest's first name");
            isValid = false;
        }

        if (guestLastName.isEmpty()) {
            Guest_Last_name_edit_text.setError("Please enter guest's last name");
            isValid = false;
        }

        return isValid;
    }




//    private void listeners(){
//        sendBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String code =ccp.getSelectedCountryCode();
//                String country =ccp.getSelectedCountryEnglishName();
//                String number=phoneTextView.getText().toString();
//            }
//        });
//    }


//    public ArrayList<Cart_item> generateHotels()
//    {
//        ArrayList<Cart_item> hotelList = new ArrayList<>();
//
//        for (int i=0; i<=2; i++)
//        {
//            hotelList.add(new Cart_item(i,"Holiday Inn","Nostra ligula, commodo hac metus scelerisque nullam luctus ultrices. Quam at et etiam purus lacinia placerat condimentum eros dapibus imperdiet in. Iaculis aliquam integer integer."
//                    , "5-Star Hotel", "Himenaeos aliquet sem ac fusce diam et viverra ad",234.00, 1.00, 1.00,null, new Date("10/12/2018"), new Date("10/12/2018")));
//        }
//
//        hotelList.add(new Cart_item(11,"Hotel Stay","Nostra ligula, commodo hac metus scelerisque nullam luctus ultrices. Quam at et etiam purus lacinia placerat condimentum eros dapibus imperdiet in. Iaculis aliquam integer integer."
//                , "5-Star Hotel", "Himenaeos aliquet sem ac fusce diam et viverra ad",234.00, 1.00, 1.00,null, new Date("10/12/2018"), new Date("10/12/2018")));
//
//        return hotelList;
//    }
    public static Date parseDate(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (ParseException e) {
            return null;
        }
    }
    private void ShowCustomersOnListView(DataBaseHelper dataBaseHelper) {
        checkout_cart_details details=this.dataBaseHelper.getEveryone();
        rv=findViewById(R.id.rv_checkout_items);

        double totalprice=details.getTotalprice();
        total_price_calc_display=findViewById(R.id.total_price_input);

        total_price_calc_display.setText(String.valueOf(totalprice));

        total_price_stripe=totalprice;
        recyclerAdapter=new checkout_recyclerAdapter(details.getAllcartitems(),total_price_calc_display);

        // you can also set the layout in the xml file using the layout manager attribute
        rv.setLayoutManager(new LinearLayoutManager(Checkout.this));


        rv.setAdapter(recyclerAdapter);

//To add dividers to between the views
        DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(Checkout.this,DividerItemDecoration.VERTICAL
        );

        rv.addItemDecoration(dividerItemDecoration
        );
//        findViewById(R.id.progressBar_checkout_cart).setVisibility(View.GONE);






        Toast.makeText(Checkout.this, (details.getTotalprice()).toString(), Toast.LENGTH_SHORT).show();
    }




}


/////////////////extra code/////////////////////
//private void initRecyclerView() {
//
//        recyclerAdapter = new checkout_recyclerAdapter(checkout_cart);
//
//        // you can also set the layout in the xml file using the layout manager attribute
//        rv.setLayoutManager(new LinearLayoutManager(this));
//
//
//        rv.setAdapter(recyclerAdapter);
//
//        //To add dividers to between the views
//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL
//        );
//
//        rv.addItemDecoration(dividerItemDecoration
//        );
//
//    }

//    ArrayList<Cart_item> cartitem1=details.getAllcartitems();

//        Bitmap bitmap = BitmapFactory.decodeByteArray(cartitem1.get(0).getImage(), 0, (cartitem1.get(0).getImage()).length);
//        imageView.setImageBitmap(bitmap);