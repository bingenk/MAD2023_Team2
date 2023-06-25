package sg.edu.np.mad.mad2023_team2.ui.BookingAttraction.models;

import android.net.Uri;

import java.net.URI;


////////////////////////////////////////////////////////////
//        TRIP CLASS stores contents of RecyclerView      //
////////////////////////////////////////////////////////////
public class Trip {


    private String tripImage, tripTitle, trip, desc;

    public Trip(String tripImage, String tripTitle, String trip, String desc) {
        this.tripImage = tripImage;
        this.tripTitle = tripTitle;
        this.trip = trip;
        this.desc = desc;
    }

    public String getTripImage() {
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
