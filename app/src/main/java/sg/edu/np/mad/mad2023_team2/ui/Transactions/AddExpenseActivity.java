package sg.edu.np.mad.mad2023_team2.ui.Transactions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import sg.edu.np.mad.mad2023_team2.R;
import sg.edu.np.mad.mad2023_team2.databinding.ActivityAddExpenseBinding;

public class AddExpenseActivity extends AppCompatActivity {
ActivityAddExpenseBinding binding;
private String type;
private ExpenseModel expenseModel;

Button submit;

public ArrayList<String> expenseCategories;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddExpenseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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

        if ("Income".equals(type)) {
            binding.incomeRadio.setChecked(true);
        } else {
            binding.expenseRadio.setChecked(true);
        }


        binding.incomeRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type="Income";
            }
        });
        binding.expenseRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type="Expense";
            }
        });

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

    Log.d("Wassup", "createExpense: 1312124");
    String expenseId = UUID.randomUUID().toString();
    String amount = binding.amount.getText().toString();
    String note = binding.note.getText().toString();
    String category = binding.categoryDropdown.getSelectedItem().toString(); // Get the selected category from the dropdown

    boolean incomeChecked = binding.incomeRadio.isChecked();
    if (incomeChecked) {
        type = "Income";
    } else {
        type = "Expense";
    }

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
    expenseModel.setType(type);
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




    }
