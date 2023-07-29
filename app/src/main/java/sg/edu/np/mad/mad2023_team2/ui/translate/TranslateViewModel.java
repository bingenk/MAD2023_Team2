package sg.edu.np.mad.mad2023_team2.ui.translate;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TranslateViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public TranslateViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}