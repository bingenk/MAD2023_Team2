package sg.edu.np.mad.mad2023_team2.ui.Currency_Converter;

import static android.content.Context.MODE_PRIVATE;
import static java.lang.StrictMath.round;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mynameismidori.currencypicker.CurrencyPicker;
import com.mynameismidori.currencypicker.CurrencyPickerListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import sg.edu.np.mad.mad2023_team2.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Currency_Converter_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Currency_Converter_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    TextView convert_from_dropdown_menu, convert_to_dropdown_menu, conversion_rate,tv_currency_code_amt_to_convert,currency_symbol,convert_app_currency_dropdown;
    EditText edit_amount_to_convert_value;
    ArrayList<Countries> arraylist;
    Dialog from_dialog, to_dialog;

    ImageView from_flag_image, to_flag_image,from_country_flag_for_change_App_Currency;
    Button conversion, Apply_Changes_Button;
    String convert_from_value, convert_to_value, conversion_value;
//    String[] currency = {"AED", "AFN", "ALL", "AMD", "ANG", "AOA", "ARS", "AUD", "AWG", "AZN",
//            "BAM", "BBD", "BDT", "BGN", "BHD", "BIF", "BMD", "BND", "BOB", "BRL", "BSD", "BTC", "BTN", "BWP", "BYN", "BYR", "BZD",
//            "CAD", "CDF", "CHF", "CLF", "CLP", "CNY", "COP", "CRC", "CUC", "CUP", "CVE", "CZK",
//            "DJF", "DKK", "DOP", "DZD",
//            "EGP", "ERN", "ETB", "EUR",
//            "FJD", "FKP",
//            "GBP", "GEL", "GGP", "GHS", "GIP", "GMD", "GNF", "GTQ", "GYD",
//            "HKD", "HNL", "HRK", "HTG", "HUF",
//            "IDR", "ILS", "IMP", "INR", "IQD", "IRR", "ISK",
//            "JEP", "JMD", "JOD", "JPY",
//            "KES", "KGS", "KHR", "KMF", "KPW", "KRW", "KWD", "KYD", "KZT",
//            "LAK", "LBP", "LKR", "LRD", "LSL", "LVL", "LYD",
//            "MAD", "MDL", "MGA", "MKD", "MMK", "MNT", "MOP", "MRO", "MUR", "MVR", "MWK", "MXN", "MYR", "MZN",
//            "NAD", "NGN", "NIO", "NOK", "NPR", "NZD",
//            "OMR",
//            "PAB", "PEN", "PGK", "PHP", "PKR", "PLN", "PYG",
//            "QAR",
//            "RON", "RSD", "RUB", "RWF",
//            "SAR", "SBD", "SCR", "SDG", "SEK", "SGD", "SHP", "SLL", "SOS", "SRD", "STD", "SVC", "SYP", "SZL",
//            "THB", "TJS", "TMT", "TND", "TOP", "TRY", "TTD", "TWD", "TZS",
//            "UAH", "UGX", "USD", "UYU", "UZS",
//            "VEF", "VND", "VUV",
//            "WST",
//            "XAF", "XAG", "XCD", "XDR", "XOF", "XPF",
//            "YER",
//            "ZAR", "ZMK", "ZMW", "ZWL"};

    private RequestQueue requestQueue;
    public Currency_Converter_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Currency_Converter_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Currency_Converter_Fragment newInstance(String param1, String param2) {
        Currency_Converter_Fragment fragment = new Currency_Converter_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_currency__converter_, container, false);

        convert_from_dropdown_menu = v.findViewById(R.id.convert_from_dropdown_menu);
        convert_to_dropdown_menu = v.findViewById(R.id.convert_to_dropdown_menu);
        conversion_rate = v.findViewById(R.id.conversion_rate);
        conversion = v.findViewById(R.id.conversion);
        from_flag_image=v.findViewById(R.id.from_country_flag);
        to_flag_image=v.findViewById(R.id.to_country_flag);
        tv_currency_code_amt_to_convert=v.findViewById(R.id.tv_currency_code_amt_to_convert);
        currency_symbol=v.findViewById(R.id.currency_symbol);
        edit_amount_to_convert_value = v.findViewById(R.id.edit_amount_to_convert_value);
        from_country_flag_for_change_App_Currency=v.findViewById(R.id.from_country_flag_for_change_App_Currency);
        convert_app_currency_dropdown=v.findViewById(R.id.convert_app_currency_dropdown);
        Apply_Changes_Button=v.findViewById(R.id.Apply_Changes_Button);

        requestQueue = Volley.newRequestQueue(requireContext());
        CurrencyConverterUtil.initialize(v.getContext());
//
//        from_country_flag_for_change_App_Currency.setVisibility(View.VISIBLE);
//        int flagid=Get_Currency_Of_App.getflagidsharedprefs(v.getContext());
        String Country_code=Get_Currency_Of_App.getcountrycodesharedprefs(v.getContext());
//        double conversion_rate=Get_Currency_Of_App.getconversionratesharedprefs(v.getContext());
        convert_app_currency_dropdown.setText(Country_code);
//        Log.d("wassup", "onCreateView: "+flagid);
//        from_country_flag_for_change_App_Currency.setImageResource(flagid);


//
//        arraylist = new ArrayList<>();
//        for(String i : currency) {
//            Countries c= new Countries(i,i,i);
//            arraylist.add(c);
//        }
        convert_from_dropdown_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrencyPicker picker = CurrencyPicker.newInstance("Select Currency");
                picker.setListener(new CurrencyPickerListener() {
                    @Override
                    public void onSelectCurrency(String name, String code, String symbol, int flagDrawableResID) {
                        // Handle the currency selection here
                          // Assign the selected value to convert_from_value
                        convert_from_value = code;
                        convert_from_dropdown_menu.setText(code);
                        from_flag_image.setVisibility(View.VISIBLE);
                        from_flag_image.setImageResource(flagDrawableResID);
                        tv_currency_code_amt_to_convert.setText(code);
                        tv_currency_code_amt_to_convert.setVisibility(View.VISIBLE);
                        picker.dismiss(); // Dismiss the currency picker dialog manually
                    }




                });
                picker.show(getParentFragmentManager(), "CURRENCY_PICKER");

            }
        });

        convert_to_dropdown_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CurrencyPicker picker = CurrencyPicker.newInstance("Select Currency");
                picker.setListener(new CurrencyPickerListener() {
                    @Override
                    public void onSelectCurrency(String name, String code, String symbol, int flagDrawableResID) {
                        // Handle the currency selection here
                        convert_to_value = code; // Assign the selected value to convert_from_value
                        convert_to_dropdown_menu.setText(code);
                        to_flag_image.setVisibility(View.VISIBLE);
                        to_flag_image.setImageResource(flagDrawableResID);
                        picker.dismiss(); // Dismiss the currency picker dialog manually
                    }




                });
                picker.show(getParentFragmentManager(), "CURRENCY_PICKER");
            }
        });


        conversion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateAmountToConvert() && validateConvertFrom() && validateConvertTo()) {
                    Double editAmountToConvertValue = Double.valueOf(edit_amount_to_convert_value.getText().toString());
                    CurrencyExchange(convert_from_value, convert_to_value, editAmountToConvertValue);
                }
            }
        });


//        exit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Close_MainActivity();
//            }
//        });

        convert_app_currency_dropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CurrencyPicker picker = CurrencyPicker.newInstance("Select Currency");
                picker.setListener(new CurrencyPickerListener() {
                    @Override
                    public void onSelectCurrency(String name, String code, String symbol, int flagDrawableResID) {
                        // Handle the currency selection here
//                        convert_to_value = code; // Assign the selected value to convert_from_value
                        convert_app_currency_dropdown.setText(code);
                        from_country_flag_for_change_App_Currency.setVisibility(View.VISIBLE);
                        from_country_flag_for_change_App_Currency.setImageResource(flagDrawableResID);

//                        SharedPreferences sharedPreferences = v.getContext().getSharedPreferences("CurrencyPrefs", MODE_PRIVATE);
//                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                        editor.putInt("FlagResid",flagDrawableResID);
//                        editor.apply();



                        picker.dismiss(); // Dismiss the currency picker dialog manually
                    }




                });
                picker.show(getParentFragmentManager(), "CURRENCY_PICKER");
            }
        });
        // Inside your onClick method for the conversion button

        Apply_Changes_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String convertToValue = convert_app_currency_dropdown.getText().toString().trim();

                if (convertToValue.isEmpty()) {
                    // Display error message if 'Convert To' currency is not selected
                    convert_app_currency_dropdown.setError("Please select a currency to change to");
                    Toast.makeText(v.getContext(), "Please select 'Convert To' currency", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Add additional validation if needed
                // For example, you can check if the 'convertToValue' is a valid currency code

                // Step 2: Call the CurrencyConverterUtil's convertCurrency method
                CurrencyConverterUtil.convertCurrency("SGD", convertToValue, 1.0, new CurrencyConverterUtil.CurrencyConversionCallback() {
                    @Override
                    public void onCurrencyConverted(double convertedAmount, String targetCurrency) {
                        try {
                            convert_app_currency_dropdown.setText(targetCurrency);
                            String stringValue = String.valueOf(convertedAmount); // Convert double to String

                            Toast.makeText(v.getContext(), stringValue, Toast.LENGTH_SHORT).show();

                            // Save the updated currency code and conversion rate in SharedPreferences
                            SharedPreferences sharedPreferences = v.getContext().getSharedPreferences("CurrencyPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("currency_code", targetCurrency);
                            editor.putFloat("conversion_rate", (float) convertedAmount);
                            editor.apply();

                        } catch (Exception e) {
                            // Log any exceptions that occur during UI update
                            Log.e("CurrencyConversion", "Error in onCurrencyConverted: " + e.getMessage());
                        }
                    }

                    @Override
                    public void onConversionError(String errorMessage) {
                        // Handle the error, e.g., show an error message to the user
                        Log.e("CurrencyConversion", "Error: " + errorMessage);
                    }
                });
            }
        });





        return v;
    }


    protected void CurrencyExchange(String convert_from_value, String convert_to_value, Double edit_amount_to_convert_value) {
        Log.d("yo", "CurrencyExchange: 232");

        // Passes in the data from the user to the url using concatenation
        String url = "https://currency-converter-by-api-ninjas.p.rapidapi.com/v1/convertcurrency?have=" + convert_from_value + "&want=" + convert_to_value + "&amount=" + edit_amount_to_convert_value;

        // Creates a new volley request to the API and retrieve the JsonObjectRequest
        JsonObjectRequest currencyRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Double old_amt = round(response.getDouble("old_amount"), 3);
                            Double new_amt = round(response.getDouble("new_amount"), 3);
                            Double conversion_value = round((new_amt / old_amt), 3);
                            Double convertedvalue=round(conversion_value*edit_amount_to_convert_value,3);
                            conversion_rate.setText(convertedvalue.toString());
                            currency_symbol.setText(convert_to_dropdown_menu.getText());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        error.printStackTrace();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("X-RapidAPI-Host", "currency-converter-by-api-ninjas.p.rapidapi.com");
                headers.put("X-RapidAPI-Key", "c349ff60cfmsh17d2a09c838fa4dp1f7fc9jsnf1afcabd330d");
                return headers;
            }
        };

        // Add the request to the request queue

        requestQueue.add(currencyRequest);


    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (requestQueue != null) {
            requestQueue.cancelAll(this);
        }
        requestQueue = null;
    }



    public static double round(double value, int currency) {
        if (currency < 0) throw new IllegalArgumentException();
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(currency, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }






    // Validate the amount to convert value
    private boolean validateAmountToConvert() {
        String amountToConvert = edit_amount_to_convert_value.getText().toString().trim();
        if (amountToConvert.isEmpty()) {
            edit_amount_to_convert_value.setError("Please enter the amount to convert");
            return false;
        }
        // Add additional validation if needed
        return true;
    }

    // Validate the 'convert from' dropdown value
    private boolean validateConvertFrom() {
        String convertFrom = convert_from_dropdown_menu.getText().toString().trim();
        if (convertFrom.isEmpty()) {
            convert_from_dropdown_menu.setError("Please select 'Convert From' currency");
            return false;
        }
        // Add additional validation if needed
        return true;
    }

    // Validate the 'convert to' dropdown value
    private boolean validateConvertTo() {
        String convertTo = convert_to_dropdown_menu.getText().toString().trim();
        if (convertTo.isEmpty()) {
            convert_to_dropdown_menu.setError("Please select 'Convert To' currency");
            return false;
        }
        // Add additional validation if needed
        return true;
    }


}


//
//    ///////////method to validate all the input by the///////////////////////////
//    private boolean validate_Currency_Exchange(){
//        String from_country = convert_from_dropdown_menu.getText().toString().trim();
//        String to_country =  convert_to_dropdown_menu.getText().toString().trim();
//        String convert_amt =  edit_amount_to_convert_value.getText().trim
//
//
//        boolean isValid = true;
//
//        if (firstName.isEmpty()) {
//            First_name_input_edit_text.setError("Please enter your first name");
//            isValid = false;
//        }
//
//        if (lastName.isEmpty()) {
//            Last_name_input_edit_text.setError("Please enter your last name");
//            isValid = false;
//        }
//
//        if (emailInput.isEmpty()){
//            Email_input_edit_text.setError("Field can't be empty");
//            return false;
//        } else if (!isValidEmail(emailInput)) {
//            Email_input_edit_text.setError("Please enter a valid email address\n" +
//                    "**Note** Only small case letters allowed.");
//            return false;
//        }
//
//
//        if (phoneNumber.isEmpty()) {
//            Phone_number_edit_text.setError("Please enter your phone number");
//            isValid = false;
//        }
//        if (!phoneNumber.isEmpty()) {
//            isValid=country_code_picker.isValidFullNumber();
//            if (!isValid){
//                Phone_number_edit_text.setError("Enter a valid phone number !");
//            }
//        }
//
//        if (guestFirstName.isEmpty()) {
//            Guest_First_name_edit_text.setError("Please enter guest's first name");
//            isValid = false;
//        }
//
//        if (guestLastName.isEmpty()) {
//            Guest_Last_name_edit_text.setError("Please enter guest's last name");
//            isValid = false;
//        }
//
//        return isValid;
//    }
//





//    public void Close_MainActivity() {
//        .finish();
//        System.exit(0);
//    }



















//
//    public String getConversionRate(String convert_from_value, String convert_to_value, Double edit_amount_to_convert_value) {
//        RequestQueue requestqueue = Volley.newRequestQueue(getView().getContext());
//        String url = "https://currency-converter-by-api-ninjas.p.rapidapi.com/v1/convertcurrency" ;
//
//        StringRequest stringrequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                JSONObject jsonobject = null;
//                try {
//                    jsonobject = new JSONObject(response);
//                    Double conversion_rate_value = round(((Double) jsonobject.get(convert_from_value + "_" + convert_to_value)), 2);
//                    conversion_value = "" + round((conversion_rate_value * edit_amount_to_convert_value), 2);
//                    conversion_rate.setText(conversion_value);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
//
//
//        requestqueue.add(stringrequest);
//        return null;
//
