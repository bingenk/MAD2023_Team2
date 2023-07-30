package sg.edu.np.mad.mad2023_team2.ui.accomodations;

import android.os.Parcel;
import android.os.Parcelable;

import sg.edu.np.mad.mad2023_team2.ui.Favorites.Favourites;

// Model class to store and manage hotel details

// Uses parcelable interface to send accommodation objects through intents
public class Accommodations implements Parcelable, Favourites {

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
    private String image;


    public Accommodations(){}

    // Constructor without image
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


    // Constructor with image
    public Accommodations(int i, String n, String t, String a,double p, double lat, double lon, double r, String district, String z, String distance, String ci, String co,String con, String img, String cots)
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
        hasCots = cots;
    }

    // Parcelable constructor
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
        image = in.readString();
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

    // Get properties methods
    public int getId(){
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getItemType() {
        return "Accommodations";
    }

    @Override
    public String getStringPrice() {
        return "$ "+  String.format("%.2f", price);
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

    public String getImage() {
        return image;
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
        dest.writeString(image);
        dest.writeString(hasCots);
    }


}
