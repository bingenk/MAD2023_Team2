package sg.edu.np.mad.mad2023_team2.ui.accomodations;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

// Create only a single instance of the volley requestqueue to prevent overload
public class AccommodationSingleton {
    private static AccommodationSingleton instance;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    private static Context ctx;

    private AccommodationSingleton(Context context) {
        ctx = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized AccommodationSingleton getInstance(Context context) {
        if (instance == null) {
            instance = new AccommodationSingleton(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}
