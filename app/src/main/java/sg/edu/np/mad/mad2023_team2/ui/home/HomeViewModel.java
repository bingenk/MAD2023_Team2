package sg.edu.np.mad.mad2023_team2.ui.home;

import androidx.lifecycle.ViewModel;
import android.content.Context;
import android.content.Intent;

public class HomeViewModel extends ViewModel {
    // Create a method to open the SettingsActivity
    public void openSettingsActivity(Context context) {
        Intent intent = new Intent(context, SettingsActivity.class);
        context.startActivity(intent);
    }
}
