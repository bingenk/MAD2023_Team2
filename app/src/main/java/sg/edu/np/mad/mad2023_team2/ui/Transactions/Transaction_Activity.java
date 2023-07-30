package sg.edu.np.mad.mad2023_team2.ui.Transactions;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import sg.edu.np.mad.mad2023_team2.R;
import sg.edu.np.mad.mad2023_team2.databinding.ActivityTransactionBinding;
import sg.edu.np.mad.mad2023_team2.ui.Currency_Converter.Get_Currency_Of_App;

public class Transaction_Activity extends AppCompatActivity implements OnItemsClick  {

    private ArrayList<ExpenseModel> expenseList = new ArrayList<>();
    ActivityTransactionBinding binding;
    private ExpenseAdapter expenseAdapter;

    private List<String> expenseCategories = new ArrayList<>();

    private String Currency_Code;


    private double conversion_Rate;



    private long income=0,expense=0;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTransactionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //get the currency details to be set
        Currency_Code= Get_Currency_Of_App.getcountrycodesharedprefs(this);
        conversion_Rate=Get_Currency_Of_App.getconversionratesharedprefs(this);


        retrieveAllExpenses();
        expenseAdapter=new ExpenseAdapter(this,this,expenseList);
        binding.recycler.setAdapter(expenseAdapter);
        binding.recycler.setLayoutManager(new LinearLayoutManager(this));


        intent = new Intent(Transaction_Activity.this,AddExpenseActivity.class);


        binding.addExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });




    }
    protected void onResume(){
        super.onResume();
        income=0;expense=0;
        retrieveAllExpenses();
    }

    // Add a new method to retrieve all expense data from Firebase
    private void retrieveAllExpenses() {
        // Firebase related code

        SharedPreferences sharedPreferences = getSharedPreferences("CartFb", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        DatabaseReference expensesRef = FirebaseDatabase.getInstance().getReference("users")
                .child(username)
                .child("Expense");

        expensesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                expenseList.clear(); // Clear the previous data from the ArrayList
                for (DataSnapshot expenseSnapshot : dataSnapshot.getChildren()) {
                    // Loop through each expense data and extract it as an ExpenseModel object
                    ExpenseModel expenseModel = expenseSnapshot.getValue(ExpenseModel.class);
                    if (expenseModel != null) {
                        expenseList.add(expenseModel);
                    }
                }

                HashMap<String, Double> categoryTotals = calculateCategoryTotals();
                setUpGraph(categoryTotals);

                // Create a new instance of the adapter and set it to the RecyclerView
                expenseAdapter = new ExpenseAdapter(Transaction_Activity.this, Transaction_Activity.this, expenseList);
                binding.recycler.setAdapter(expenseAdapter);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // An error occurred while retrieving data
                // Handle this case as needed
                Toast.makeText(Transaction_Activity.this, "Error retrieving expense data", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void setUpGraph(HashMap<String, Double> categoryTotals) {
        List<PieEntry> pieEntryList = new ArrayList<>();
        List<Integer> colorlist = new ArrayList<>();

        for (String category : categoryTotals.keySet()) {
            DecimalFormat decimalFormat = new DecimalFormat("#.00");
            double totalAmount = Double.parseDouble(decimalFormat.format(categoryTotals.get(category)*conversion_Rate));
            pieEntryList.add(new PieEntry((float) totalAmount, category));

            // Set custom colors for each category based on its name
            int color = getColorForCategory(category);
            colorlist.add(color);
        }

        PieDataSet pieDataSet = new PieDataSet(pieEntryList, ""); // Remove the second argument or provide a suitable description for the data set
        pieDataSet.setColors(colorlist);
        pieDataSet.setValueTextColor(getResources().getColor(R.color.white));
        pieDataSet.setValueTextSize(12f); // Set the text size for the pie chart values
        // Enable touch gestures on the pie chart (scrolling and zooming)
        binding.pieChart.setTouchEnabled(true);

        // Adjust the drag deceleration friction coefficient for smoother scrolling (optional)
        binding.pieChart.setDragDecelerationFrictionCoef(0.95f);

        PieData pieData = new PieData(pieDataSet);
        binding.pieChart.setData(pieData);
        binding.pieChart.invalidate();
    }

    // Method to get a color based on the category name
    private int getColorForCategory(String category) {
        switch (category) {
            case "Food":
                return getResources().getColor(R.color.colorCategory1);
            case "Transport":
                return getResources().getColor(R.color.colorCategory2);

            case "Accommodation":
                return getResources().getColor(R.color.colorCategory3);

            case "Health":
                return getResources().getColor(R.color.colorCategory4);
            case "Others":
                return getResources().getColor(R.color.colorCategory5);

            // Add more cases for other categories
            default:
                // Return a default color for categories that are not explicitly defined
                return getRandomColor();
        }
    }

    // Method to get a random color from a predefined list of colors
    private int getRandomColor() {
        List<Integer> colors = new ArrayList<>();
        colors.add(getResources().getColor(R.color.teal_700));
        colors.add(getResources().getColor(R.color.purple_200));
        // Add more colors if needed

        int randomIndex = (int) (Math.random() * colors.size());
        return colors.get(randomIndex);
    }


    @Override
    public void onClick(ExpenseModel expenseModel) {
        // Check if expenseModel is null to determine if it's a new expense or an update
        if (expenseModel != null) {
            // Updating an existing expense
            Intent intent = new Intent(this, AddExpenseActivity.class);
            intent.putExtra("type", "update");
            intent.putExtra("model", expenseModel);
            startActivity(intent);
        } else {
            // Creating a new expense
            Intent intent = new Intent(this, AddExpenseActivity.class);
            startActivity(intent);
        }
    }



    private HashMap<String, Double> calculateCategoryTotals() {
        // Create a HashMap to store the category as key and total amount as value
        HashMap<String, Double> categoryTotals = new HashMap<>();

        // Check if expenseList is null or empty
        if (expenseList == null || expenseList.isEmpty()) {
            return categoryTotals;
        }

        // Iterate through the expenseList to calculate the total amount for each category
        for (ExpenseModel expense : expenseList) {
            String category = expense.getCategory();
            double amount = expense.getAmount();

            // Check if the category is already present in the HashMap
            if (categoryTotals.containsKey(category)) {
                // If yes, update the total amount for that category
                double totalAmount = categoryTotals.get(category) + amount;
                categoryTotals.put(category, totalAmount);
            } else {
                // If the category is not present, add it to the HashMap with the current amount as total
                categoryTotals.put(category, amount);
            }
        }

        // Now you have a HashMap with category as key and total amount as value
        // You can display this information to the user or perform any other operations as needed

        // For example, you can print the results to the console
        for (String category : categoryTotals.keySet()) {
            double totalAmount = categoryTotals.get(category);
            System.out.println("Category: " + category + ", Total Amount: " + totalAmount);
        }
        return categoryTotals;
    }


}


//
//    @Override
//    protected void onStart() {
//        super.onStart() ;
//        ProgressDialog progressDialog=new ProgressDialog(  this);
//        progressDialog.setTitle("Please");
//        progressDialog.setMessage("Wait");
//        progressDialog.setCancelable(false);
//        progressDialog.show();
//        if(FirebaseAuth.getInstance().getCurrentUser()==null)
//        {
//            progressDialog.show();
//            FirebaseAuth.getInstance()
//                    .signInAnonymously()
//                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
//                                              @Override
//                                              public void onSuccess(AuthResult authResult) {
//                                                  progressDialog.cancel();
//                                              }
//                                          })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            progressDialog.cancel();
//                            Toast.makeText(Transaction_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    });
//        }
//
//
//}
