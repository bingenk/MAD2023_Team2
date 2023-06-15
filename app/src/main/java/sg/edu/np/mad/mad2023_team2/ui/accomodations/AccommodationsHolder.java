package sg.edu.np.mad.mad2023_team2.ui.accomodations;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import sg.edu.np.mad.mad2023_team2.R;

public class AccommodationsHolder extends RecyclerView.ViewHolder {
    ImageView hotelPhoto;
    TextView hotelName,hotelRating,hotelAddress;
    public AccommodationsHolder(View view)
    {
        super(view);
        hotelPhoto = view.findViewById(R.id.hotel_image);
        hotelName = view.findViewById(R.id.hotel_name);
        hotelRating = view.findViewById(R.id.hotel_rating);
        hotelAddress = view.findViewById(R.id.hotel_address);
    }
}
