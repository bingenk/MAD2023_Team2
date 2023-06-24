package sg.edu.np.mad.mad2023_team2.ui.cart_sqllite_database;

import android.content.Context;

public class DatabaseManager {
    private static DataBaseHelper dataBaseHelper;

    public static DataBaseHelper getDataBaseHelper(Context context) {
        if (dataBaseHelper == null) {
            dataBaseHelper = new DataBaseHelper(context.getApplicationContext());
        }
        return dataBaseHelper;
    }
}