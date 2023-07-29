package sg.edu.np.mad.mad2023_team2.ui.contact;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;

import sg.edu.np.mad.mad2023_team2.R;
import sg.edu.np.mad.mad2023_team2.databinding.FragmentContactBinding;

public class ContactFragment extends Fragment {

    private FragmentContactBinding binding;
    private String mEditTextTo;
    //private EditText mEditTextSubject;
    private EditText mEditTextMessage;
    private Button mButtonSubject;
    private ArrayList<String> subjectOptionArray;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        ContactViewModel contactViewModel =
                new ViewModelProvider(this).get(ContactViewModel.class);

        binding = FragmentContactBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mEditTextTo = "travel.wise.faq@gmail.com";
        //mEditTextSubject = root.findViewById(R.id.edit_text_subject); //can be hardcoded like above, current use is from an edittext
        mButtonSubject = root.findViewById(R.id.subject_select); //current use is from button
        mEditTextMessage = root.findViewById(R.id.edit_text_message); //can be hardcoded like above, current use is from an edittext

        subjectOptionArray = new ArrayList<>();
        subjectOptionArray.add("SELECT OPTION");
        subjectOptionArray.add("BOOKING ISSUES");
        subjectOptionArray.add("PAYMENT ISSUES");
        subjectOptionArray.add("MAP ISSUES");
        subjectOptionArray.add("FEEDBACK");
        subjectOptionArray.add("OTHER");

        Button subjectChooseBtn = root.findViewById(R.id.subject_select);
        Button buttonSend = root.findViewById(R.id.mail_send_button);

        subjectChooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectSubject();
            }
        });
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMail();
            }
        });
        return root;
    }

    private void selectSubject() {
        PopupMenu popupMenu = new PopupMenu(getActivity(), mButtonSubject);

        for (int i=0; i< subjectOptionArray.size(); i++) {

            popupMenu.getMenu().add(Menu.NONE, i, i, subjectOptionArray.get(i));
        }

        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                int position = item.getItemId();

                String subjectOption = subjectOptionArray.get(position);

                mButtonSubject.setText(subjectOption);

                return false;
            }
        });
    }

    private void sendMail() {
        String recipient = mEditTextTo; //recipient is "travel.wise.faq@gmail.com"

        //String subject = mEditTextSubject.getText().toString(); // subject EditText is turned into a string
        String subject = mButtonSubject.getText().toString();
        String message = mEditTextMessage.getText().toString(); //message EditText is turned into a string

        Intent send = new Intent(Intent.ACTION_SEND); //make a new intent to send the email
        send.putExtra(Intent.EXTRA_EMAIL, new String[] {recipient}); //include the recipient (array is a MUST)
        send.putExtra(Intent.EXTRA_SUBJECT, subject); //include subject
        send.putExtra(Intent.EXTRA_TEXT, message); //message

        send.setType("message/rfc822"); //set the intent type as sending an email
        startActivity(Intent.createChooser(send, "Choose an email client"));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
