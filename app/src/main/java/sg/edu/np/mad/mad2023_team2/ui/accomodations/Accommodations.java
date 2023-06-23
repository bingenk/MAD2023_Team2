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
    private String type;
    private String address;
    private double price;
    private double latitude;
    private double longitude;
    private double rating;
    private String district;
    private String zip;
    private String distance;
    private String hasCots;
    private String checkin;
    private String checkout;
    private String configuration;
    private byte[] image;


    public Accommodations(){}

    public Accommodations(int i, String n, String t, String a,double p, double lat, double lon, double r, String district, String z, String distance, String ci, String co, String con)
    {
        id = i;
        name = n;
        type = t;
        address = a;
        price = p;
        latitude = lat;
        longitude = lon;
        image = null;
        rating = r;
        this.district = district;
        zip = z;
        this.distance = distance;
        checkin = ci;
        checkout = co;
        configuration = con;
        hasCots = null;
    }

    public Accommodations(int i, String n, String t, String a,double p, double lat, double lon, double r, String district, String z, String distance, String ci, String co,String cots , String con)
    {
        id = i;
        name = n;
        type = t;
        address = a;
        price = p;
        latitude = lat;
        longitude = lon;
        image = null;
        rating = r;
        this.district = district;
        zip = z;
        this.distance = distance;
        hasCots = cots;
        checkin = ci;
        checkout = co;
        configuration = con;
    }

    public Accommodations(int i, String n, String t, String a,double p, double lat, double lon, double r, String district, String z, String distance, String ci, String co , String con, byte[] img)
    {
        id = i;
        name = n;
        type = t;
        address = a;
        price = p;
        latitude = lat;
        longitude = lon;
        rating = r;
        this.district = district;
        zip = z;
        this.distance = distance;
        checkin = ci;
        checkout = co;
        configuration = con;
        image = img;
        hasCots = null;
    }

    public Accommodations(int i, String n, String t, String a,double p, double lat, double lon, double r, String district, String z, String distance, String ci, String co,String con, byte[] bytes, String cots)
    {
        id = i;
        name = n;
        type = t;
        address = a;
        price = p;
        latitude = lat;
        longitude = lon;
        rating = r;
        this.district = district;
        zip = z;
        this.distance = distance;
        checkin = ci;
        checkout = co;
        configuration = con;
        image = bytes;
        hasCots = cots;
    }

    protected Accommodations(Parcel in) {
        id = in.readInt();
        name = in.readString();
        type = in.readString();
        address = in.readString();
        price = in.readDouble();
        latitude = in.readDouble();
        longitude = in.readDouble();
        rating = in.readDouble();
        district = in.readString();
        zip = in.readString();
        distance = in.readString();
        checkin = in.readString();
        checkout = in.readString();
        configuration = in.readString();
        image = in.createByteArray();
        hasCots = in.readString();
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

    public double getRating() {
        return rating;
    }

    public String getDistrict() {
        return district;
    }

    public String getZip() {
        return zip;
    }

    public String getDistance() {
        return distance;
    }

    public String getHasCots() {
        return hasCots;
    }

    public String getCheckin() {
        return checkin;
    }

    public String getCheckout() {
        return checkout;
    }

    public String getConfiguration() {
        return configuration;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(type);
        dest.writeString(address);
        dest.writeDouble(price);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeDouble(rating);
        dest.writeString(district);
        dest.writeString(zip);
        dest.writeString(distance);
        dest.writeString(checkin);
        dest.writeString(checkout);
        dest.writeString(configuration);
        dest.writeByteArray(image);
        dest.writeString(hasCots);
    }
}
