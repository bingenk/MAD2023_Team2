package sg.edu.np.mad.mad2023_team2.ui.Transactions;

import static androidx.core.content.ContentProviderCompat.requireContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mynameismidori.currencypicker.CurrencyPicker;
import com.mynameismidori.currencypicker.CurrencyPickerListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import sg.edu.np.mad.mad2023_team2.R;
import sg.edu.np.mad.mad2023_team2.databinding.ActivityAddExpenseBinding;
import sg.edu.np.mad.mad2023_team2.ui.Currency_Converter.Countries;
import sg.edu.np.mad.mad2023_team2.ui.Currency_Converter.CurrencyConverterUtil;

public class AddExpenseActivity extends AppCompatActivity {
ActivityAddExpenseBinding binding;
private String type;
private ExpenseModel expenseModel;

Button submit;

    TextView convert_from_dropdown_menu, convert_to_dropdown_menu, conversion_rate,tv_currency_code_amt_to_convert,currency_symbol,convert_app_currency_dropdown;
    EditText edit_amount_to_convert_value;
    ArrayList<Countries> arraylist;
    Dialog from_dialog, to_dialog;

    ImageView from_flag_image, to_flag_image,from_country_flag_for_change_App_Currency;
    Button conversion, Apply_Changes_Button;
    String convert_from_value, convert_to_value, conversion_value;

public ArrayList<String> expenseCategories;

    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddExpenseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //initialse the view elements
        initialiseexpenseViews();
        requestQueue = Volley.newRequestQueue(this);
        CurrencyConverterUtil.initialize(this);

        // Initialize the expenseCategories list here
        expenseCategories = new ArrayList<>();
        expenseCategories.add("Food");
        expenseCategories.add("Transport");
        expenseCategories.add("Accommodation");
        expenseCategories.add("Health");
        expenseCategories.add("Others");

        submit = binding.tSubmit;
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, expenseCategories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.categoryDropdown.setAdapter(categoryAdapter);

        Intent intent = getIntent();
        if (intent != null) {
            type = intent.getStringExtra("type");
            expenseModel = (ExpenseModel) intent.getSerializableExtra("model");
            if (expenseModel != null) {
                binding.tDelete.setVisibility(View.VISIBLE);
                // The expenseModel is not null, so it's an update operation
                type = expenseModel.getType();
                binding.amount.setText(String.valueOf(expenseModel.getAmount()));
                int categoryIndex = expenseCategories.indexOf(expenseModel.getCategory());
                binding.categoryDropdown.setSelection(categoryIndex);
                binding.note.setText(expenseModel.getNote());
            } else {
                // The expenseModel is null, so it's a new expense
                expenseModel = null;
            }
        } else {
            // Handle the case when the intent is null (if needed)
        }





        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (expenseModel==null){
                    Log.d("Wassup", "createExpense: 1312124praveen");
                    createExpense();
                }
                else {
                    updateExpense();
                }


          }
        });

        binding.tDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteExpense();
            }


        });

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
                picker.show(getSupportFragmentManager(), "CURRENCY_PICKER");

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
                picker.show(getSupportFragmentManager(), "CURRENCY_PICKER");
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



    private void deleteExpense() {
        // Firebase related code
        SharedPreferences sharedPreferences = getSharedPreferences("CartFb", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        Log.d("whoo", username+"wassup");
        DatabaseReference expensesRef = FirebaseDatabase.getInstance().getReference("users")
                .child(username)
                .child("Expense")
                .child(expenseModel.getExpenseId());

        expensesRef.removeValue()
                .addOnSuccessListener(aVoid -> {
                    // Expense deleted successfully
                    Toast.makeText(this, "Expense deleted successfully!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, Transaction_Activity.class);
                    startActivity(intent);

                })
                .addOnFailureListener(e -> {
                    // Error occurred while deleting the expense
                    Toast.makeText(this, "Failed to delete expense: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
//
//        finish();
    }

//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.add_menu,menu);
//        return true;
//    }





//  bi
//
//
//    {
//        int id=item.getItemId();
//        if(id==R.id.saveExpense)
//        {
//            createExpense();
//            return true;
//        }
//        return false;
//    }
private void createExpense() {
    if (!validateAmount() || !validateCategory() || !validateNote()) {
        // Validation failed, return without creating the expense
        return;
    }
    Log.d("Wassup", "createExpense: 1312124");
    String expenseId = UUID.randomUUID().toString();
    String amount = binding.amount.getText().toString();
    String note = binding.note.getText().toString();
    String category = binding.categoryDropdown.getSelectedItem().toString(); // Get the selected category from the dropdown


    if (amount.trim().length() == 0) {
        binding.amount.setError("Empty");
        return;
    }

    // If expenseModel is null, create a new ExpenseModel object
    if (expenseModel == null) {
        expenseModel = new ExpenseModel();
    }

    // Set the properties of the expenseModel
    expenseModel.setExpenseId(expenseId);
    expenseModel.setAmount(Long.parseLong(amount));
    expenseModel.setCategory(category);
    expenseModel.setNote(note);
    expenseModel.setType("expense");
    Date currentDate = new Date();
    expenseModel.setTime(currentDate);


    // Firebase related code
    SharedPreferences sharedPreferences = getSharedPreferences("CartFb", MODE_PRIVATE);
    String username = sharedPreferences.getString("username", "");
    Log.d("whoo", username+"wassup1");
    if (username == null || username.isEmpty()) {
        // Show an error message or handle the case when the username is not available
        Toast.makeText(this, "Username not found. Cannot update expense.", Toast.LENGTH_SHORT).show();
        return;
    }
    DatabaseReference expensesRef = FirebaseDatabase.getInstance().getReference("users").child(username).child("Expense");
    Log.d("Wassup", "createExpense: 1312124fwefwe");
    // Push the ExpenseModel object to the database
    expensesRef.child(expenseId).setValue(expenseModel)
            .addOnSuccessListener(aVoid -> {
                // Expense added successfully
                Toast.makeText(this, "Expense added successfully!", Toast.LENGTH_SHORT).show();
                expenseModel = null;
                finish();
            })
            .addOnFailureListener(e -> {
                // Error occurred while adding the expense
                Toast.makeText(this, "Failed to add expense: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });

//    finish();
}

    private void updateExpense() {


        // Check if expenseModel is null before proceeding with the update
        if (expenseModel == null) {
            Toast.makeText(this, "Expense not found for update", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!validateAmount() || !validateCategory() || !validateNote()) {
            // Validation failed, return without creating the expense
            return;
        }

        String expenseId = expenseModel.getExpenseId();
        String amount = binding.amount.getText().toString();
        String note = binding.note.getText().toString();
        String category = binding.categoryDropdown.getSelectedItem().toString(); // Get the selected category from the dropdown

        // ... Rest of the code ...

        // Firebase related code
        SharedPreferences sharedPreferences = getSharedPreferences("CartFb", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        Log.d("whoo", username+"wassup2");
        if (username == null || username.isEmpty()) {
            // Show an error message or handle the case when the username is not available
            Toast.makeText(this, "Username not found. Cannot update expense.", Toast.LENGTH_SHORT).show();
            return;
        }
        DatabaseReference expensesRef = FirebaseDatabase.getInstance().getReference("users").child(username).child("Expense");

        // Create a new ExpenseModel object
        ExpenseModel model = new ExpenseModel(expenseId, note, category, type, Long.parseLong(amount), expenseModel.getTime());

        // Push the ExpenseModel object to the database
        expensesRef.child(expenseId).setValue(model)
                .addOnSuccessListener(aVoid -> {
                    // Expense updated successfully
                    Toast.makeText(this, "Expense updated successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    // Error occurred while updating the expense
                    Toast.makeText(this, "Failed to update expense: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    // Log the error for debugging purposes
                    Log.e("AddExpenseActivity", "Error updating expense: " + e.getMessage());
                });
//
//        finish();
    }



    public void initialiseexpenseViews()
    {

        convert_from_dropdown_menu = findViewById(R.id.ae_convert_from_dropdown_menu);
        convert_to_dropdown_menu = findViewById(R.id.ae_convert_to_dropdown_menu);
        conversion_rate = findViewById(R.id.ae_conversion_rate);
        conversion = findViewById(R.id.ae_conversion);
        from_flag_image=findViewById(R.id.ae_from_country_flag);
        to_flag_image=findViewById(R.id.ae_to_country_flag);
        tv_currency_code_amt_to_convert=findViewById(R.id.ae_tv_currency_code_amt_to_convert);
        currency_symbol=findViewById(R.id.ae_currency_symbol);
        edit_amount_to_convert_value = findViewById(R.id.ae_edit_amount_to_convert_value);

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


    public static double round(double value, int currency) {
        if (currency < 0) throw new IllegalArgumentException();
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(currency, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private boolean validateAmount() {
        String amountStr = binding.amount.getText().toString().trim();
        if (amountStr.isEmpty()) {
            binding.amount.setError("Please enter the amount");
            return false;
        }

        // Validate if the amount is a valid number (optional)
        try {
            double amount = Double.parseDouble(amountStr);
            if (amount <= 0) {
                binding.amount.setError("Please enter a valid positive amount");
                return false;
            }
        } catch (NumberFormatException e) {
            binding.amount.setError("Please enter a valid number");
            return false;
        }

        // Clear the error if the amount is valid
        binding.amount.setError(null);
        return true;
    }

    private boolean validateCategory() {
        String category = binding.categoryDropdown.getSelectedItem().toString();
        if (category.isEmpty()) {
            // Show an error message if the category is not selected
            Toast.makeText(this, "Please select a category", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean validateNote() {
        String note = binding.note.getText().toString().trim();
        if (note.isEmpty()) {
            binding.note.setError("Please enter a note");
            return false;
        }

        // Add any additional validation checks for the note field if needed

        // Clear the error if the note is valid
        binding.note.setError(null);
        return true;
    }





}
