package sg.edu.np.mad.mad2023_team2.ui.Cart;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.Date;

import androidx.annotation.NonNull;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;

public class Cart_item implements Parcelable {

    private int id;
    private String name;
    private String description;
    private String type;
    private String address;

    private double price;

    private double latitude;
    private double longitude;
    private byte[] image;

    private Date checkin_date;
    private Date checkout_date;


    public Cart_item(){}

    public Cart_item(int id, String name, String description, String type, String address, double price, double latitude, double longitude, byte[] image, Date checkin_date, Date checkout_date) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.address = address;
        this.price = price;
        this.latitude = latitude;
        this.longitude = longitude;
        this.image = image;
        this.checkin_date = checkin_date;
        this.checkout_date = checkout_date;
    }



    protected Cart_item(Parcel in) {
        id = in.readInt();
        name = in.readString();
        description = in.readString();
        type = in.readString();
        address = in.readString();
        price = in.readDouble();
        latitude = in.readDouble();
        longitude = in.readDouble();
        image = in.createByteArray();
        checkin_date=(java.util.Date) in.readSerializable();
        checkout_date=(java.util.Date) in.readSerializable();
    }

    public static final Creator<Cart_item> CREATOR = new Creator<Cart_item>() {
        @Override
        public Cart_item createFromParcel(Parcel in) {
            return new Cart_item(in);
        }

        @Override
        public Cart_item[] newArray(int size) {
            return new Cart_item[size];
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

    public Date getCheckin_date() {
        return checkin_date;
    }



    public Date getCheckout_date() {
        return checkout_date;
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
        dest.writeSerializable(checkin_date);
        dest.writeSerializable(checkout_date);
    }
}
