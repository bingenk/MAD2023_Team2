package sg.edu.np.mad.mad2023_team2.ui.BookingAttraction;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import android.graphics.BitmapFactory;


import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class Attractions implements Parcelable {
    private int id;
    private String name;
    private String description;
    private String address;
    private double aPrice;
    private double cPrice;
    private byte[] image;


    public Attractions(){}

    public Attractions(int i, String n, String d, String a, double ap, double cp)
    {
        id = i;
        name = n;
        description = d;
        address = a;
        aPrice = ap;
        cPrice = cp;
        image = null;
    }

    public Attractions(int i, String n, String d, String a, double ap, double cp, byte[] bytes)
    {
        id = i;
        name = n;
        description = d;
        address = a;
        aPrice = ap;
        cPrice = cp;
        image = bytes;
    }

    protected Attractions(Parcel in) {
        id = in.readInt();
        name = in.readString();
        description = in.readString();
        address = in.readString();
        aPrice = in.readDouble();
        cPrice = in.readDouble();
        image = in.createByteArray();
    }

    public static final Creator<Attractions> CREATOR = new Creator<Attractions>() {
        @Override
        public Attractions createFromParcel(Parcel in) {
            return new Attractions(in);
        }

        @Override
        public Attractions[] newArray(int size) {
            return new Attractions[size];
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

    public double getAPrice() {
        return aPrice;
    }

    public double getCPrice() {
        return cPrice;
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
        dest.writeString(address);
        dest.writeDouble(aPrice);
        dest.writeDouble(cPrice);
        dest.writeByteArray(image);
    }
}
