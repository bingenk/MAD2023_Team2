package sg.edu.np.mad.mad2023_team2.ui.accomodations;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class Accommodations implements Parcelable {

    private int id;
    private String name;
    private String description;
    private String type;
    private String address;

    private double price;

    private double latitude;
    private double longitude;
    private byte[] image;


    public Accommodations(){}

    public Accommodations(int i, String n, String d, String r, String a,double p, double lat, double lon)
    {
        id = i;
        name = n;
        description = d;
        type = r;
        address = a;
        price = p;
        latitude = lat;
        longitude = lon;
        image = null;
    }

    public Accommodations(int i, String n, String d, String r, String a,double p, double lat, double lon, byte[] bytes)
    {
        id = i;
        name = n;
        description = d;
        type = r;
        address = a;
        price = p;
        latitude = lat;
        longitude = lon;
        image = bytes;
    }

    protected Accommodations(Parcel in) {
        id = in.readInt();
        name = in.readString();
        description = in.readString();
        type = in.readString();
        address = in.readString();
        price = in.readDouble();
        latitude = in.readDouble();
        longitude = in.readDouble();
        image = in.createByteArray();
    }

    public static final Creator<Accommodations> CREATOR = new Creator<Accommodations>() {
        @Override
        public Accommodations createFromParcel(Parcel in) {
            return new Accommodations(in);
        }

        @Override
        public Accommodations[] newArray(int size) {
            return new Accommodations[size];
        }
    };

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

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public Bitmap getImage() {

        try
        {
            InputStream is = new ByteArrayInputStream(image);
            return BitmapFactory.decodeStream(is);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(type);
        dest.writeString(address);
        dest.writeDouble(price);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeByteArray(image);
    }
}
