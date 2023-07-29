package sg.edu.np.mad.mad2023_team2.ui.Currency_Converter;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CurrencyConverterUtil {

    private static RequestQueue requestQueue;

    public static void initialize(Context context) {
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public static void convertCurrency(String fromCurrency, String toCurrency, double amountToConvert, CurrencyConversionCallback callback) {
        // Passes in the data from the user to the URL using concatenation
        String url = "https://currency-converter-by-api-ninjas.p.rapidapi.com/v1/convertcurrency?have=" + fromCurrency + "&want=" + toCurrency + "&amount=" + amountToConvert;

        Log.d("whatthefluck", "onClick: 17");

        JsonObjectRequest currencyRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("whatthefluck", "onClick: 18");
                            Double oldAmount = response.getDouble("old_amount");
                            Double newAmount = response.getDouble("new_amount");
                            Log.d("whatthefluck", "onClick: 19");
                            Double conversionRate = newAmount / oldAmount;
                            Double convertedAmount = conversionRate * amountToConvert;
                            Log.d("whatthefluck", "onClick: 20");
                            callback.onCurrencyConverted(conversionRate, toCurrency);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("whatthefluck", "onClick: 21");
                            callback.onConversionError("Error parsing response");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Log.d("whatthefluck", "API Error Response: " + error.networkResponse);
                        callback.onConversionError("Error fetching conversion data");
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

    public interface CurrencyConversionCallback {
        void onCurrencyConverted(double conversion_rate, String targetCurrency);
        void onConversionError(String errorMessage);
    }
}
