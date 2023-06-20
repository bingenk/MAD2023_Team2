package sg.edu.np.mad.mad2023_team2.ui.checkout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
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
import java.util.List;
import java.util.Map;

import sg.edu.np.mad.mad2023_team2.MainActivity;
import sg.edu.np.mad.mad2023_team2.R;
import sg.edu.np.mad.mad2023_team2.ui.Cart.Cart_item;
import sg.edu.np.mad.mad2023_team2.ui.Cart.checkout_cart_recyclerAdapter;

public class Checkout extends AppCompatActivity {


    RecyclerView rv;

    checkout_recyclerAdapter recyclerAdapter;
    List<Cart_item> checkout_cart;
    private CountryCodePicker ccp;
    private EditText phoneTextView;
    private Button sendBtn;

    private TextInputEditText email_Input;

    Button paymentbutton;

    String SECRET_KEY="sk_test_51NL1IVArVetI75gXAaWfW0ZIEbRwNCbmDvI692u6W5EPaXtOkBRoK7OTwAc5xbcjawcfrKooy5j8hUMQC7qGT87s00XVfKw9F3";
    String PUBLISH_KEY="pk_test_51NL1IVArVetI75gX1NpOAdurG9Ojti56zfKjGD3VBxFUEDkLZmijmiy0St2SYRPMHpE62mwYikY4tauIftaFsUno00TMiWl405";

    PaymentSheet paymentSheet;


    String customerID;
    String EphericalKey;
    String ClientSecret;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        initalizeViews();

        ccp.isValidFullNumber();

        //RECYCLER VIEW

        checkout_cart=generateHotels();


        rv=findViewById(R.id.rv_checkout_items);

        recyclerAdapter=new checkout_recyclerAdapter(checkout_cart);

        // you can also set the layout in the xml file using the layout manager attribute
        rv.setLayoutManager(new LinearLayoutManager(this));


        rv.setAdapter(recyclerAdapter);

       //To add dividers to between the views
        DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(this,DividerItemDecoration.VERTICAL
        );

        rv.addItemDecoration(dividerItemDecoration
        );




        //STRIPE PAYMENT

        PaymentConfiguration.init(this,PUBLISH_KEY);

        paymentSheet=new PaymentSheet(this,paymentSheetResult -> {
            onPaymentResult(paymentSheetResult);

        });

        paymentbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ValidateEmail();
                PaymentFlow();
            }
        });
        StringRequest stringRequest=new StringRequest(Request.Method.POST, "https://api.stripe.com/v1/customers", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object=new JSONObject(response);
                    customerID=object.getString("id");
                    Toast.makeText(Checkout.this, customerID, Toast.LENGTH_SHORT).show();

                    getEphericalKey(customerID);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> header=new HashMap<>();
                header.put("Authorization","Bearer "+SECRET_KEY);
                return header;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(Checkout.this);
        requestQueue.add(stringRequest);



    }

    private void onPaymentResult(PaymentSheetResult paymentSheetResult) {
        if (paymentSheetResult instanceof PaymentSheetResult.Completed){
            Toast.makeText(this, "payment success", Toast.LENGTH_SHORT).show();
        }
    }

    private void getEphericalKey(String customerID) {


        StringRequest stringRequest=new StringRequest(Request.Method.POST, "https://api.stripe.com/v1/ephemeral_keys", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object=new JSONObject(response);
                    EphericalKey=object.getString("id");

                    getEphericalKey(customerID);
                    Toast.makeText(Checkout.this, EphericalKey, Toast.LENGTH_SHORT).show();
                    getCientSecret(customerID,EphericalKey);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> header=new HashMap<>();
                header.put("Authorization","Bearer "+SECRET_KEY);
                header.put("Stripe-Version","2022-11-15");
                return header;
            }


            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params=new HashMap<>();
                params.put("customer",customerID);

                return params;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(Checkout.this);
        requestQueue.add(stringRequest);

    }

    private void getCientSecret(String customerID, String ephericalKey) {

        StringRequest stringRequest=new StringRequest(Request.Method.POST, "https://api.stripe.com/v1/payment_intents", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object=new JSONObject(response);
                    ClientSecret=object.getString("client_secret");

                    Toast.makeText(Checkout.this, ClientSecret, Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> header=new HashMap<>();
                header.put("Authorization","Bearer "+SECRET_KEY);
                return header;
            }


            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params=new HashMap<>();
                params.put("customer",customerID);
                params.put("amount","10"+"00");
                params.put("currency","usd");
                params.put("automatic_payment_methods[enabled]","true");


                return params;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(Checkout.this);
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
     ccp=(CountryCodePicker) findViewById(R.id.country_code_picker);
     phoneTextView=(EditText) findViewById(R.id.Phone_Number_Input);
     email_Input=findViewById(R.id.Email_Input);
     paymentbutton=findViewById(R.id.btn_proceed_to_payment);
     ccp.registerCarrierNumberEditText(phoneTextView);
    }

    private boolean ValidateEmail(){

        String emailInput = email_Input.getText().toString().trim();
        if (emailInput.isEmpty()){
            email_Input.setError("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {

         email_Input.setError("Please enter a valid email address");
         return false;
        } else{
            email_Input.setError(null);
            return true;
        }
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


    public ArrayList<Cart_item> generateHotels()
    {
        ArrayList<Cart_item> hotelList = new ArrayList<>();

        for (int i=0; i<=2; i++)
        {
            hotelList.add(new Cart_item(i,"Holiday Inn","Nostra ligula, commodo hac metus scelerisque nullam luctus ultrices. Quam at et etiam purus lacinia placerat condimentum eros dapibus imperdiet in. Iaculis aliquam integer integer."
                    , "5-Star Hotel", "Himenaeos aliquet sem ac fusce diam et viverra ad",234.00, 1.00, 1.00,null, new Date("10/12/2018"), new Date("10/12/2018")));
        }

        hotelList.add(new Cart_item(11,"Hotel Stay","Nostra ligula, commodo hac metus scelerisque nullam luctus ultrices. Quam at et etiam purus lacinia placerat condimentum eros dapibus imperdiet in. Iaculis aliquam integer integer."
                , "5-Star Hotel", "Himenaeos aliquet sem ac fusce diam et viverra ad",234.00, 1.00, 1.00,null, new Date("10/12/2018"), new Date("10/12/2018")));

        return hotelList;
    }
    public static Date parseDate(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

}