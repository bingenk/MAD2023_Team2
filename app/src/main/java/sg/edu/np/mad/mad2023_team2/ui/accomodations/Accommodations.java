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
    private String rating;
    private String address;
    private byte[] image;

    public Accommodations(){}

    public Accommodations(int i, String n, String d, String r, String a)
    {
        id = i;
        name = n;
        description = d;
        rating = r;
        address = a;
        image = null;
    }

    public Accommodations(int i, String n, String d, String r, String a, byte[] bytes)
    {
        id = i;
        name = n;
        description = d;
        rating = r;
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
        return rating;
    }

    public Bitmap getImage() {
        if (image != null) {
            InputStream is = new ByteArrayInputStream(image);
            return BitmapFactory.decodeStream(is);
        }
        else {return null;}
    }
}
