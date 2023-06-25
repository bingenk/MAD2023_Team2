package sg.edu.np.mad.mad2023_team2.ui.Cart;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

// ViewModel class for the Cart/Checkout fragment
public class CartViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    // Constructor for the CartViewModel class
    public CartViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Cart/Checkout fragment");
    }

    // Method to get the text LiveData object
    public LiveData<String> getText() {
        return mText;
    }
}
