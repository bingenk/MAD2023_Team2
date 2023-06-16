package sg.edu.np.mad.mad2023_team2.ui.accomodations;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.jar.Attributes;

public class Accommodations {

    private int id;
    private String name;
    private String description;
    private String type;
    private String address;

    private double latitude;
    private double longitude;
    private byte[] image;


    public Accommodations(){}

    public Accommodations(int i, String n, String d, String r, String a, double lat, double lon)
    {
        id = i;
        name = n;
        description = d;
        type = r;
        address = a;
        latitude = lat;
        longitude = lon;
        image = null;
    }

    public Accommodations(int i, String n, String d, String r, String a, byte[] bytes)
    {
        id = i;
        name = n;
        description = d;
        type = r;
        address = a;
        image = bytes;
    }

    public int getId(){
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getAddress() {
        return address;
    }

    public String getRating() {
        return type;
    }

    public Bitmap getImage() {
        if (image != null) {
            InputStream is = new ByteArrayInputStream(image);
            return BitmapFactory.decodeStream(is);
        }
        else {return null;}
    }
}
