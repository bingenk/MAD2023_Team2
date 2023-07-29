package sg.edu.np.mad.mad2023_team2.ui.Currency_Converter;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import sg.edu.np.mad.mad2023_team2.ui.Cart.Cart;
import sg.edu.np.mad.mad2023_team2.ui.Cart.CartDataCallback;
import sg.edu.np.mad.mad2023_team2.ui.cart_sqllite_database.DataBaseHelper;
import sg.edu.np.mad.mad2023_team2.ui.cart_sqllite_database.DatabaseManager;
import sg.edu.np.mad.mad2023_team2.ui.checkout_cart_sqllite.Cart_item;

public class SetCurrencyDetailsManager {

    private static sg.edu.np.mad.mad2023_team2.ui.Currency_Converter.SetCurrencyDetailsManager instance;
    private Context context;
    private boolean apiCalled;
    DataBaseHelper dataBaseHelper;

    private SetCurrencyDetailsManager(Context context) {
        this.context = context.getApplicationContext();
        this.apiCalled = false;
    }

    public static synchronized sg.edu.np.mad.mad2023_team2.ui.Currency_Converter.SetCurrencyDetailsManager getInstance(Context context) {
        if (instance == null) {
            instance = new SetCurrencyDetailsManager(context);
        }
        return instance;
    }

    // Add your API callback method here
    public void makeApiCall(String currency_code,double conversion_rate) {
        if (!apiCalled) {

           setCurrencyDetails(currency_code,conversion_rate);
            apiCalled = true;
        }
    }

    // Method to reset the apiCalled variable
    public void resetApiCalled() {
        apiCalled = false;
    }

    private void setCurrencyDetails(String currencyCode, double conversionRate) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("CurrencyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("currency_code", currencyCode);
        editor.putFloat("conversion_rate", (float) conversionRate);
        editor.apply();
    }



}
