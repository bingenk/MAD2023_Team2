package sg.edu.np.mad.mad2023_team2.ui.Favorites;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Objects;

import sg.edu.np.mad.mad2023_team2.R;
import sg.edu.np.mad.mad2023_team2.ui.Recommendations.Attraction;
import sg.edu.np.mad.mad2023_team2.ui.Recommendations.OnClickInterface;
import sg.edu.np.mad.mad2023_team2.ui.Recommendations.ReccomendationsDetails;
import sg.edu.np.mad.mad2023_team2.ui.Recommendations.RecommendationsWeb;
import sg.edu.np.mad.mad2023_team2.ui.Recommendations.Restaurant;
import sg.edu.np.mad.mad2023_team2.ui.accomodations.Accommodations;
import sg.edu.np.mad.mad2023_team2.ui.accomodations.AccommodationsDetails;


public class FavoritesFragment extends Fragment implements OnClickInterface {

    private SearchView search;
    private TextView itemsText;
    private RecyclerView rv;

    private FavoritesAdapter adapter;
    private ArrayList<Object> favoriteList;

    private DatabaseReference dbRef;

    public FavoritesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_favorites, container, false);
        search = v.findViewById(R.id.fav_search);
        rv = v.findViewById(R.id.fav_rv);
        itemsText = v.findViewById(R.id.fav_noItemsText);

        LinearLayoutManager lm = new LinearLayoutManager(requireContext());
        rv.setLayoutManager(lm);
        rv.setItemAnimator(new DefaultItemAnimator());
        dbRef = FirebaseDatabase.getInstance().getReference();


        if (favoriteList == null)
        {
            itemsText.setVisibility(View.VISIBLE);
            rv.setVisibility(View.GONE);
        }
        return v;
    }

    @Override
    public void onClick(int p) {
        Object item = favoriteList.get(p);
        if (item instanceof Accommodations)
        {
            Intent intent = new Intent(requireActivity(), AccommodationsDetails.class);
            intent.putExtra("accommodation", (Accommodations)item);
            startActivity(intent);
        }
        else
        {
            Intent intent = new Intent(requireActivity(), ReccomendationsDetails.class);
            if (item instanceof Restaurant)
            {
                intent.putExtra("reccomendation",(Restaurant) item);
            }
            else if (item instanceof Attraction)
            {
                intent.putExtra("reccomendation",(Attraction) item);
            }
            startActivity(intent);
        }
    }
}