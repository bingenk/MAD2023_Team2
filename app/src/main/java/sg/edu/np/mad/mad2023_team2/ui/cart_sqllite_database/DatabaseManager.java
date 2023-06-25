package sg.edu.np.mad.mad2023_team2.ui.cart_sqllite_database;

import android.content.Context;
/////////This is a singleton class so that the database can be used in multiple packages witthout recreating the database instance multiple times////////
public class DatabaseManager {
    private static DataBaseHelper dataBaseHelper;

    public static DataBaseHelper getDataBaseHelper(Context context) {
        if (dataBaseHelper == null) {
            dataBaseHelper = new DataBaseHelper(context.getApplicationContext());
        }
        return dataBaseHelper;
    }
}