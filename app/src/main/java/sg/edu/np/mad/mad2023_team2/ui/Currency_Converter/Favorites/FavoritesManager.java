package sg.edu.np.mad.mad2023_team2.ui.Currency_Converter.Favorites;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import sg.edu.np.mad.mad2023_team2.ui.Recommendations.Attraction;
import sg.edu.np.mad.mad2023_team2.ui.Recommendations.Restaurant;
import sg.edu.np.mad.mad2023_team2.ui.accomodations.Accommodations;

public class FavoritesManager {

    private Object item;
    private DatabaseReference dbRef;

    private Context context;
    private String type;
    private String id;


    public FavoritesManager(Object data, Context context, String type, int id) {

        this.item = data;
        this.context = context;
        this.type = type;
        this.id = String.valueOf(id);
    }

    public void createObject() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("CartFb", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");

        dbRef = FirebaseDatabase.getInstance().getReference("users").child(username).child("Favourites");
        Map<String, Object> values = new HashMap<>();

        values.put("Values", item);
        values.put("Type", type);

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(id))
                {
                    Toast.makeText(context, "This item is already favourited!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    dbRef.child(id).setValue(values).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(context, "Successfully added to Favourites!", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "Error while adding to Favourites. Please try again!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Error while adding to Favourites. Please try again!", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
