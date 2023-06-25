package sg.edu.np.mad.mad2023_team2.ui.BookingAttraction.models;

public class Trip {

    private int tripImage;
    private String tripTitle, trip, desc;

    public Trip(int tripImage, String tripTitle, String trip, String desc) {
        this.tripImage = tripImage;
        this.tripTitle = tripTitle;
        this.trip = trip;
        this.desc = desc;
    }

    public int getTripImage() {
        return tripImage;
    }

    public String getTripTitle() {
        return tripTitle;
    }

    public String getTrip() {
        return trip;
    }

    public String getDesc() {return desc; }
}
