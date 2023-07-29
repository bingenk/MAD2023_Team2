package sg.edu.np.mad.mad2023_team2.ui.checkout_cart_sqllite;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Cart_item implements Parcelable {
    private int id;
    private String name;
    private String description;
    private String type;
    private String address;
    private double price;
    private double latitude;
    private double longitude;
    private String image;
    private Date checkin_date;
    private Date checkout_date;
    private boolean expanded;

    public Cart_item() {
    }

    public Cart_item(int id, String name, String description, String type, String address, double price,
                     double latitude, double longitude, String image, Date checkin_date, Date checkout_date) {
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
        this.expanded = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Date getCheckin_date() {
        return checkin_date;
    }

    public void setCheckin_date(Date checkin_date) {
        this.checkin_date = checkin_date;
    }

    public Date getCheckout_date() {
        return checkout_date;
    }

    public void setCheckout_date(Date checkout_date) {
        this.checkout_date = checkout_date;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    // Parcelable implementation
    protected Cart_item(Parcel in) {
        id = in.readInt();
        name = in.readString();
        description = in.readString();
        type = in.readString();
        address = in.readString();
        price = in.readDouble();
        latitude = in.readDouble();
        longitude = in.readDouble();
        image = in.readString();
        checkin_date = new Date(in.readLong());
        checkout_date = new Date(in.readLong());
        expanded = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(type);
        dest.writeString(address);
        dest.writeDouble(price);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(image);
        dest.writeLong(checkin_date != null ? checkin_date.getTime() : -1);
        dest.writeLong(checkout_date != null ? checkout_date.getTime() : -1);
        dest.writeByte((byte) (expanded ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
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
}
