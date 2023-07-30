package sg.edu.np.mad.mad2023_team2.ui.Recommendations;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import sg.edu.np.mad.mad2023_team2.ui.Favorites.Favourites;

public class Restaurant implements Parcelable, Favourites {
    private int id;
    private String name;
    private String desc;
    private ArrayList<String> cuisine;
    private ArrayList<String> dietaryRestrictions;
    private String status;
    private String location;
    private String address;
    private String distanceAway;
    private String priceLevel;
    private String priceRange;
    private double latitude;
    private double longitude;
    private double rating;
    private ArrayList<String> awards;
    private String url;
    private String image;

    public Restaurant(){}

    public Restaurant(int id, String name, ArrayList<String> cuisine, String status, String address, String distanceAway, String priceLevel, double rating, String image) {
        this.id = id;
        this.name = name;
        this.cuisine = cuisine;
        this.status = status;
        this.address = address;
        this.distanceAway = distanceAway;
        this.priceLevel = priceLevel;
        this.rating = rating;
        this.image = image;
    }


    protected Restaurant(Parcel in) {
        id = in.readInt();
        name = in.readString();
        desc = in.readString();
        cuisine = in.createStringArrayList();
        dietaryRestrictions = in.createStringArrayList();
        status = in.readString();
        location = in.readString();
        address = in.readString();
        distanceAway = in.readString();
        priceLevel = in.readString();
        priceRange = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        rating = in.readDouble();
        awards = in.createStringArrayList();
        url = in.readString();
        image = in.readString();
    }

    public Restaurant(int id, String name, String desc, ArrayList<String> cuisine, ArrayList<String> dietaryRestrictions, String status, String location, String address, String distanceAway, String priceLevel, String priceRange, double latitude, double longitude, double rating, ArrayList<String> awards, String url, String image) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.cuisine = cuisine;
        this.dietaryRestrictions = dietaryRestrictions;
        this.status = status;
        this.location = location;
        this.address = address;
        this.distanceAway = distanceAway;
        this.priceLevel = priceLevel;
        this.priceRange = priceRange;
        this.latitude = latitude;
        this.longitude = longitude;
        this.rating = rating;
        this.awards = awards;
        this.url = url;
        this.image = image;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(desc);
        dest.writeStringList(cuisine);
        dest.writeStringList(dietaryRestrictions);
        dest.writeString(status);
        dest.writeString(location);
        dest.writeString(address);
        dest.writeString(distanceAway);
        dest.writeString(priceLevel);
        dest.writeString(priceRange);
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

    public static final Creator<Restaurant> CREATOR = new Creator<Restaurant>() {
        @Override
        public Restaurant createFromParcel(Parcel in) {
            return new Restaurant(in);
        }

        @Override
        public Restaurant[] newArray(int size) {
            return new Restaurant[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getItemType() {
        return "Restaurant";
    }

    @Override
    public String getStringPrice() {
        return priceLevel;
    }

    public String getPrice() {
        return priceLevel;
    }

    public String getDesc() {
        return desc;
    }

    public ArrayList<String> getCuisine() {
        return cuisine;
    }

    public ArrayList<String> getDietaryRestrictions() {
        return dietaryRestrictions;
    }

    public String getStatus() {
        return status;
    }

    public String getLocation() {
        return location;
    }

    public String getAddress() {
        return address;
    }

    public String getDistanceAway() {
        return distanceAway;
    }

    public String getPriceLevel() {
        return priceLevel;
    }

    public String getPriceRange() {
        return priceRange;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getRating() {
        return rating;
    }

    public ArrayList<String> getAwards() {
        return awards;
    }

    public String getImage() {
        return image;
    }

    public String getUrl() {
        return url;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDistanceAway(String distanceAway) {
        this.distanceAway = distanceAway;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setCuisine(ArrayList<String> cuisine) {
        this.cuisine = cuisine;
    }

    public void setDietaryRestrictions(ArrayList<String> dietaryRestrictions) {
        this.dietaryRestrictions = dietaryRestrictions;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPriceLevel(String priceLevel) {
        this.priceLevel = priceLevel;
    }

    public void setPriceRange(String priceRange) {
        this.priceRange = priceRange;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setAwards(ArrayList<String> awards) {
        this.awards = awards;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
