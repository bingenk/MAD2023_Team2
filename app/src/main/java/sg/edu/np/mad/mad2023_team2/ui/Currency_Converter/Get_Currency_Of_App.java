package sg.edu.np.mad.mad2023_team2.ui.Currency_Converter;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

public class Get_Currency_Of_App
{

    public static String getcountrycodesharedprefs(Context context){

        SharedPreferences sharedPreferences = context.getSharedPreferences("CurrencyPrefs", MODE_PRIVATE);
        String Currency_Code = sharedPreferences.getString("currency_code", "");
        return Currency_Code;
    }


    public static double getconversionratesharedprefs(Context context){

        SharedPreferences sharedPreferences = context.getSharedPreferences("CurrencyPrefs", MODE_PRIVATE);
         double conversion_rate= sharedPreferences.getFloat("conversion_rate", 0);
         return conversion_rate;
    }

    public static int getflagidsharedprefs(Context context){

        SharedPreferences sharedPreferences = context.getSharedPreferences("CurrencyPrefs", MODE_PRIVATE);
        int flag_id= sharedPreferences.getInt("FlagResid",0);
        return flag_id;
    }



}
