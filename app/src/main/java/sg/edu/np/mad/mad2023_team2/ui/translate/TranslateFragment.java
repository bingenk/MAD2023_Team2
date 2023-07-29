package sg.edu.np.mad.mad2023_team2.ui.translate;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import sg.edu.np.mad.mad2023_team2.R;
import sg.edu.np.mad.mad2023_team2.databinding.FragmentTranslateBinding;


public class TranslateFragment extends Fragment {

    private FragmentTranslateBinding binding;

    private EditText sourceLanguageEt;
    private TextView destinationLanguageTv;
    private MaterialButton sourceLanguageChooseBtn;
    private MaterialButton destinationLanguageChooseBtn;
    private MaterialButton translateBtn;



    private TranslatorOptions translatorOptions;

    private Translator translator;

    private ProgressDialog progressDialog;

    private ArrayList<ModelLanguage> languageArrayList;

    private static final String TAG = "MAIN_TAG";


    private String sourceLanguageCode = "en";
    private String sourceLanguageTitle = "English";
    private String destinationLanguageCode = "en";
    private String destinationLanguageTitle = "English";
    private String sourceLanguageText = "";


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        TranslateViewModel translateViewModel =
                new ViewModelProvider(this).get(TranslateViewModel.class);

        binding = FragmentTranslateBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        sourceLanguageEt = root.findViewById(R.id.sourceLanguageEt);
        destinationLanguageTv = root.findViewById(R.id.destinationLanguageEt);
        sourceLanguageChooseBtn = root.findViewById(R.id.sourceLanguageChooseBtn);
        destinationLanguageChooseBtn = root.findViewById(R.id.destinationLanguageChooseBtn);
        translateBtn = root.findViewById(R.id.translateBtn);


        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Please Wait");
        progressDialog.setCanceledOnTouchOutside(false);

        loadAvailableLanguages();

        sourceLanguageChooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sourceLanguageChoose();
            }
        });

        destinationLanguageChooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                destinationLanguageChoose();
            }
        });

        translateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });

        return root;
    }

    private void validateData() {

        sourceLanguageText = sourceLanguageEt.getText().toString().trim();

        Log.d(TAG, "validateData: sourceLanguageText: "+ sourceLanguageText);

        if (sourceLanguageText.isEmpty()) {
            Toast.makeText(getActivity(), "Enter text to translate...", Toast.LENGTH_SHORT).show();
        }
        else {
            startTranslations();
        }
    }

    private void startTranslations() {

        progressDialog.setMessage("Processing language model...");
        progressDialog.show();

        translatorOptions = new TranslatorOptions.Builder()
                .setSourceLanguage(sourceLanguageCode)
                .setTargetLanguage(destinationLanguageCode)
                .build();
        translator = Translation.getClient(translatorOptions);

        DownloadConditions downloadConditions = new DownloadConditions.Builder()
                .requireWifi()
                .build();

        translator.downloadModelIfNeeded(downloadConditions)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "onSuccess: model ready, translating...");

                        progressDialog.setMessage("Translating...");

                        translator.translate(sourceLanguageText)
                                .addOnSuccessListener(new OnSuccessListener<String>() {
                                    @Override
                                    public void onSuccess(String translatedText) {
                                        Log.d(TAG, "onSuccess: translated text: "+translatedText);
                                        progressDialog.dismiss();
                                        destinationLanguageTv.setText(translatedText);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        Log.e(TAG, "onFailure: ",e);
                                        Toast.makeText(getActivity(), "Failed to translate due to "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Log.e(TAG, "onFailure: ",e);
                        Toast.makeText(getActivity(), "Failed to ready model due to "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void sourceLanguageChoose() {

        PopupMenu popupMenu = new PopupMenu(getActivity(), sourceLanguageChooseBtn);

        for (int i=0; i< languageArrayList.size(); i++) {

            popupMenu.getMenu().add(Menu.NONE, i, i, languageArrayList.get(i).languageTitle);
        }

        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                int position = item.getItemId();


                sourceLanguageCode = languageArrayList.get(position).languageCode;
                sourceLanguageTitle = languageArrayList.get(position).languageTitle;

                sourceLanguageChooseBtn.setText(sourceLanguageTitle);
                sourceLanguageEt.setHint("Enter "+sourceLanguageTitle);


                Log.d(TAG, "OnMenuItemClick: sourceLanguageCode: "+sourceLanguageCode);
                Log.d(TAG, "OnMenuItemClick: sourceLanguageTitle: "+sourceLanguageTitle);

                return false;
            }
        });
    }

    private void destinationLanguageChoose() {

        PopupMenu popupMenu = new PopupMenu(getActivity(),destinationLanguageChooseBtn);

        for(int i=0; i<languageArrayList.size(); i++) {
            popupMenu.getMenu().add(Menu.NONE, i, i, languageArrayList.get(i).getLanguageTitle());
        }

        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                int position = item.getItemId();

                destinationLanguageCode = languageArrayList.get(position).languageCode;
                destinationLanguageTitle = languageArrayList.get(position).languageTitle;

                destinationLanguageChooseBtn.setText(destinationLanguageTitle);

                Log.d(TAG, "OnMenuItemClick: destinationLanguageCode: "+destinationLanguageCode);
                Log.d(TAG, "OnMenuItemClick: destinationLanguageTitle: "+destinationLanguageTitle);

                return false;
            }
        });
    }

    private void loadAvailableLanguages() {
        languageArrayList = new ArrayList<>();

        List<String> languageCodeList = TranslateLanguage.getAllLanguages();

        for (String languageCode: languageCodeList) {

            String languageTitle = new Locale(languageCode).getDisplayLanguage();

            Log.d(TAG, "loadAvailableLanguages: languageCode: "+languageCode);
            Log.d(TAG, "loadAvailableLanguages: languageCode: "+languageTitle);

            ModelLanguage modelLanguage = new ModelLanguage(languageCode, languageTitle);
            languageArrayList.add(modelLanguage);

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}