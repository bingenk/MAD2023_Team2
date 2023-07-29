package sg.edu.np.mad.mad2023_team2.ui.Cart;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import sg.edu.np.mad.mad2023_team2.ui.cart_sqllite_database.DataBaseHelper;
import sg.edu.np.mad.mad2023_team2.ui.cart_sqllite_database.DatabaseManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


public class DeleteAllManager {
    private static DeleteAllManager instance;
    private Context context;
    private boolean apiCalled;



    private DeleteAllManager(Context context) {
        this.context = context.getApplicationContext();
        this.apiCalled = false;
    }

    public static synchronized DeleteAllManager getInstance(Context context) {
        if (instance == null) {
            instance = new DeleteAllManager(context);
        }
        return instance;
    }

    public void makeDeleteCall() {
        if (!apiCalled) {

            deletealldata();
            apiCalled = true;
        }
    }

    public void deletealldata() {

        Log.d("wassup", "deletealldata: 1");
        DataBaseHelper dataBaseHelper = DatabaseManager.getDataBaseHelper(context);
        int checkloggedout = dataBaseHelper.deleteAllData();

        SharedPreferences sharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(context);

        // Get the editor
        SharedPreferences.Editor editor = sharedPreferences1.edit();

        // Clear the SharedPreferences
        editor.clear();

        // Apply the changes
        editor.apply();

        // Set apiCalled to true since deletealldata() has been called
        apiCalled = true;


    }

    // Method to reset the apiCalled variable
    public void resetApiCalled() {
        apiCalled = false;
    }


}
