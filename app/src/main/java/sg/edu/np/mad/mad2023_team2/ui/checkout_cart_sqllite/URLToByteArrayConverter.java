package sg.edu.np.mad.mad2023_team2.ui.checkout_cart_sqllite;


import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class URLToByteArrayConverter {
    public interface OnConversionCompleteListener {
        void onConversionComplete(byte[] byteArray);
        void onConversionFailure(Exception e);
    }

    public static void convertURLToByteArray(final String urlString, final OnConversionCompleteListener listener) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(urlString);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setDoInput(true);

                    connection.connect();

                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        InputStream inputStream = connection.getInputStream();
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            byteArrayOutputStream.write(buffer, 0, bytesRead);
                        }

                        byte[] byteArray = byteArrayOutputStream.toByteArray();

                        inputStream.close();
                        byteArrayOutputStream.close();

                        if (listener != null) {
                            listener.onConversionComplete(byteArray);
                        }
                    } else {
                        throw new IOException("Failed to retrieve data from the URL. Response code: " + responseCode);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    if (listener != null) {
                        listener.onConversionFailure(e);
                    }
                }
            }
        });

        thread.start();
    }
}
