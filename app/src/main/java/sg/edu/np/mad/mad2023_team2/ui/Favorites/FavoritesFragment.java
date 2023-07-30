package sg.edu.np.mad.mad2023_team2.ui.Favorites;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import sg.edu.np.mad.mad2023_team2.R;
import sg.edu.np.mad.mad2023_team2.ui.MainActivity;
import sg.edu.np.mad.mad2023_team2.ui.Recommendations.Attraction;
import sg.edu.np.mad.mad2023_team2.ui.Recommendations.ReccomendationsDetails;
import sg.edu.np.mad.mad2023_team2.ui.Recommendations.Restaurant;
import sg.edu.np.mad.mad2023_team2.ui.accomodations.Accommodations;
import sg.edu.np.mad.mad2023_team2.ui.accomodations.AccommodationsDetails;


public class FavoritesFragment extends Fragment implements OnLongClickInterface {

    private SearchView search;
    private TextView itemsText;
    private RecyclerView rv;
    private ProgressBar pb;
    private FavoritesAdapter adapter;
    private ArrayList<Object> favoriteList = new ArrayList<>();
    private ArrayList<Object> searchList = new ArrayList<>();
    private DatabaseReference dbRef;
    private String username;

    public FavoritesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_favorites, container, false);
        search = v.findViewById(R.id.fav_search);
        rv = v.findViewById(R.id.fav_rv);
        itemsText = v.findViewById(R.id.fav_noItemsText);
        pb = v.findViewById(R.id.fav_pb);
        LinearLayoutManager lm = new LinearLayoutManager(requireContext());
        rv.setLayoutManager(lm);
        rv.setItemAnimator(new DefaultItemAnimator());
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("CartFb", MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");
        favoriteList = new ArrayList<>();

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });

        retrieveFavorites(this, getContext());

        return v;
    }

    private OnLongClickInterface getInterface(){return this;}

    private void retrieveFavorites(OnLongClickInterface onClickInterface, Context context) {

        if (username != null)
        {
            dbRef = FirebaseDatabase.getInstance().getReference("users").child(username).child("Favourites");
            dbRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    favoriteList.clear();
                    for (DataSnapshot s : snapshot.getChildren())
                    {
                        String type = s.child("Type").getValue(String.class);
                        if (Objects.equals(type, "Accommodations"))
                        {
                            Accommodations data = s.child("Values").getValue(Accommodations.class);
                            favoriteList.add(data);
                        }
                        else if (Objects.equals(type, "Restaurant"))
                        {
                            Restaurant data = s.child("Values").getValue(Restaurant.class);
                            favoriteList.add(data);
                        }
                        else if (Objects.equals(type, "Attraction"))
                        {
                            Attraction data = s.child("Values").getValue(Attraction.class);
                            favoriteList.add(data);
                        }
                    }

                    if (favoriteList.isEmpty())
                    {
                        itemsText.setVisibility(View.VISIBLE);
                        rv.setVisibility(View.GONE);
                    }
                    adapter = new FavoritesAdapter(onClickInterface,favoriteList,getContext());
                    rv.setAdapter(adapter);
                    pb.setVisibility(View.GONE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    showErrorAlert(onClickInterface,context,error);
                }
            });
        }
    }

    public void filter(String text)
    {
        text = text.toLowerCase();
        ArrayList<Object> searchData = new ArrayList<>();

        for (Object o : favoriteList)
        {
            String name = "";
            if (o instanceof Favourites)
            {
                Favourites favourites = (Favourites) o;
                name = favourites.getName();
            }
            if (name.toLowerCase().contains(text))
            {
                searchData.add(o);
            }
        }

        searchList = searchData;

        if (adapter!=null)
        {
            adapter.updateAdapter(searchList);
        }
        else
        {
            adapter = new FavoritesAdapter(getInterface(),searchList,getContext());
            rv.setAdapter(adapter);
        }
    }

    private void showErrorAlert(OnLongClickInterface onClickInterface, Context context, DatabaseError error)
    {
        AlertDialog.Builder errorAlert =  new AlertDialog.Builder(context);
        errorAlert.setTitle("Error occurred when fetching request \n:(");
        errorAlert.setMessage(error.getMessage() + "\nDo you want to try again?");

        errorAlert.setPositiveButton("Reload", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                retrieveFavorites(onClickInterface,context);
            }
        });

        errorAlert.setNegativeButton("Back to home", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Send back to home page if the user does not want to retry
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void OnClick(int p) {
        Object item;
        if (searchList.isEmpty())
        {
            item = favoriteList.get(p);
        }
        else
        {
            item = searchList.get(p);
        }
        if (item instanceof Accommodations)
        {
            Intent intent = new Intent(requireActivity(), AccommodationsDetails.class);
            intent.putExtra("accommodation", (Accommodations)item);
            intent.putExtra("isViewing",true);
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

    @Override
    public Boolean OnLongClick(int p) {
        Object item;
        if (searchList.isEmpty())
        {
            item = favoriteList.get(p);
        }
        else
        {
            item = searchList.get(p);
        }
        if (item instanceof Favourites)
        {
            Favourites favourites = (Favourites) item;
            deleteFavourite(favourites.getId());
        }
        else
        {
            return false;
        }
        adapter.updateAdapter(favoriteList);
        if (favoriteList.isEmpty())
        {
            itemsText.setVisibility(View.VISIBLE);
            rv.setVisibility(View.GONE);
        }

        return true;
    }

    private void deleteFavourite(int id)
    {
        String s = String.valueOf(id);
        dbRef = FirebaseDatabase.getInstance().getReference("users")
                .child(username)
                .child("Favourites")
                .child(s);
        dbRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(requireContext(), "Favourite Deleted Successfully!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(requireContext(), "Error while trying to delete item. Please try again!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}