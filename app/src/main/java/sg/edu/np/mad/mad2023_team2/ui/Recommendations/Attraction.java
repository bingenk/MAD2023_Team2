package sg.edu.np.mad.mad2023_team2.ui.Recommendations;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Attraction implements Parcelable {
    private int id;
    private String name;
    private String desc;
    private String status;
    private String type;
    private String location;
    private String address;
    private String distanceAway;
    private String price;
    private double latitude;
    private double longitude;
    private double rating;
    private ArrayList<String> awards;

    private String url;
    private String image;

    public Attraction(int id, String name, String desc, String status, String type, String location, String address, String distanceAway, String price, double latitude, double longitude, double rating, ArrayList<String> awards, String url, String image) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.status = status;
        this.type = type;
        this.location = location;
        this.address = address;
        this.distanceAway = distanceAway;
        this.price = price;
        this.latitude = latitude;
        this.longitude = longitude;
        this.rating = rating;
        this.awards = awards;
        this.url = url;
        this.image = image;
    }

    protected Attraction(Parcel in) {
        id = in.readInt();
        name = in.readString();
        desc = in.readString();
        status = in.readString();
        type = in.readString();
        location = in.readString();
        address = in.readString();
        distanceAway = in.readString();
        price = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        rating = in.readDouble();
        awards = in.createStringArrayList();
        url = in.readString();
        image = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(desc);
        dest.writeString(status);
        dest.writeString(type);
        dest.writeString(location);
        dest.writeString(address);
        dest.writeString(distanceAway);
        dest.writeString(price);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeDouble(rating);
        dest.writeStringList(awards);
        dest.writeString(url);
        dest.writeString(image);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Attraction> CREATOR = new Creator<Attraction>() {
        @Override
        public Attraction createFromParcel(Parcel in) {
            return new Attraction(in);
        }

        @Override
        public Attraction[] newArray(int size) {
            return new Attraction[size];
        }
    };

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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDistanceAway() {
        return distanceAway;
    }

    public void setDistanceAway(String distanceAway) {
        this.distanceAway = distanceAway;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
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

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public ArrayList<String> getAwards() {
        return awards;
    }

    public void setAwards(ArrayList<String> awards) {
        this.awards = awards;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }
}
