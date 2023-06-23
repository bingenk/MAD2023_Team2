package sg.edu.np.mad.mad2023_team2.ui.contact;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import sg.edu.np.mad.mad2023_team2.R;
import sg.edu.np.mad.mad2023_team2.databinding.FragmentContactBinding;

public class ContactFragment extends Fragment {

    private FragmentContactBinding binding;
    private String mEditTextTo;
    private EditText mEditTextSubject;
    private EditText mEditTextMessage;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        ContactViewModel contactViewModel =
                new ViewModelProvider(this).get(ContactViewModel.class);

        binding = FragmentContactBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textContact;
        contactViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        mEditTextTo = "travel.wise.faq@gmail.com";
        mEditTextSubject = root.findViewById(R.id.edit_text_subject); //can be hardcoded like above, current use is from an edittext
        mEditTextMessage = root.findViewById(R.id.edit_text_message); //can be hardcoded like above, current use is from an edittext

        Button buttonSend = root.findViewById(R.id.mail_send_button);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMail();
            }
        });
        return root;
    }

    private void sendMail() {
        String recipient = mEditTextTo; //recipient is "travel.wise.faq@gmail.com"

        String subject = mEditTextSubject.getText().toString(); // subject EditText is turned into a string
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
