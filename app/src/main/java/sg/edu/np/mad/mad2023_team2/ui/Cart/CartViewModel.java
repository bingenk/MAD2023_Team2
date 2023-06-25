package sg.edu.np.mad.mad2023_team2.ui.Cart;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CartViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public CartViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Cart/Checkout fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }



}
