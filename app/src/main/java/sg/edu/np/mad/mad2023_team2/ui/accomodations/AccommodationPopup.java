package sg.edu.np.mad.mad2023_team2.ui.accomodations;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import sg.edu.np.mad.mad2023_team2.R;

// Creates a popup where users can fill in information needed to display the accommodations
public class AccommodationPopup{

    private View view;
    private int adults, rooms;
    private Date checkin,checkout;
    private boolean initial;    // Check if it is opening fragment for the first time (disable cancel button)
    public AccommodationPopup(View v, int a, int r, Date ci, Date co, boolean in, final PopupCallback callback)
    {
        view = v;
        adults = a;
        rooms = r;
        checkin = ci;
        checkout = co;
        initial = in;

        // Inflates popup window layout and show it
        Context context = v.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View pv = inflater.inflate(R.layout.accommodations_popupfilters, null);
        PopupWindow pw = new PopupWindow(pv, ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT, true);
        pw.setAnimationStyle(android.R.style.Animation_Dialog);
        pw.showAtLocation(v, Gravity.CENTER, 0, 0);

        // Set cancel button so it dismisses popup on click
        Button cancel = pv.findViewById(R.id.filter_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pw.dismiss();
            }
        });

        // Ensures users must enter information when entering the fragment but can cancel popup after
        if (initial)
        {
            cancel.setVisibility(View.GONE);
        }

        // Initialize edittext and button variables
        EditText editAdults = pv.findViewById(R.id.filter_ppl);
        EditText editRooms = pv.findViewById(R.id.filter_rooms);
        EditText editCheckin = pv.findViewById(R.id.filters_checkin);
        EditText editCheckout = pv.findViewById(R.id.filters_checkout);
        Button apply = pv.findViewById(R.id.filter_apply);

        // Sets the edittext to the variables' values
        editAdults.setText(String.valueOf(adults));
        editRooms.setText(String.valueOf(rooms));

        // Sets the maximum and minimum values of the edittexts using InputFilters
        editAdults.setFilters(new InputFilter[]{new MaxMinFilter(1,29)});
        editRooms.setFilters(new InputFilter[]{new MaxMinFilter(1,29)});

        // Formats and displays the checkin checkout dates if there are present beforehand
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (checkin!=null)
        {
            String checkinS = dateFormat.format(checkin);
            editCheckin.setText(checkinS);
        }
        if (checkout!=null)
        {
            String checkoutS = dateFormat.format(checkout);
            editCheckout.setText(checkoutS);
        }

        // Displays a datepickerdialog to pick the date and sets it to the edittext
        editCheckin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get current date
                final Calendar c = Calendar.getInstance();

                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Sets checkin date to date picked
                        checkin = new Date(year-1900,month,dayOfMonth);
                        String date = dateFormat.format(checkin);
                        editCheckin.setText(date);
                    }
                }, year, month, day);

                // Sets minimum date to tomorrow
                Date tmr = new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000);
                dialog.getDatePicker().setMinDate(tmr.getTime());

                // If checkout is not null but checkin is being changed, reset checkout value
                if (checkout != null)
                {
                    checkout = null;
                    Toast.makeText(v.getContext(), "Checkout date has been resetted. Please choose again later", Toast.LENGTH_SHORT).show();
                }
                dialog.show();
            }
        });

        // Displays a datepickerdialog to pick the date and sets it to the edittext
        editCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if checkin is keyed in yet, else it would not allow to change
                if (checkin != null)
                {
                    final Calendar c = Calendar.getInstance();

                    int year = c.get(Calendar.YEAR);
                    int month = c.get(Calendar.MONTH);
                    int day = c.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog dialog = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            checkout = new Date(year-1900,month,dayOfMonth);
                            String date = dateFormat.format(checkout);
                            editCheckout.setText(date);
                        }
                    }, year, month, day);
                    // Change the minimum date to the checkin date keyed in beforehand
                    dialog.getDatePicker().setMinDate(checkin.getTime()+ 24 * 60 * 60 * 1000);
                    dialog.show();
                }
                else
                {
                    Toast.makeText(v.getContext(),"Please select the check-in date first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Saves the data to sharedprefernces and starts the recieveHotels method(calls api and start recyclerview)
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Verify if the fields are not empty
                if (checkin!=null && checkout!=null && editAdults.getText().toString().trim().length()!= 0 && editRooms.getText().toString().trim().length()!= 0)
                {
                    SharedPreferences sp = v.getContext().getSharedPreferences("Values", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    adults = Integer.parseInt(editAdults.getText().toString());
                    rooms = Integer.parseInt(editRooms.getText().toString());
                    editor.putInt("adults", adults);
                    editor.putInt("rooms", rooms);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    editor.putString("checkin", dateFormat.format(checkin));
                    editor.putString("checkout", dateFormat.format(checkout));
                    editor.commit();

                    callback.getValues(view, adults, rooms, checkin, checkout);
                    pw.dismiss();
                }
                else
                {
                    Toast.makeText(v.getContext(), "Please fill in everything", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Creates a new minimum and maximum number filter checking if the numbers are within the range
    private class MaxMinFilter implements InputFilter
    {
        private int min, max;

        public MaxMinFilter(int min, int max)
        {
            this.min = min;
            this.max = max;
        }

        // Checks if it matches into the regex, allows the input if does, else gives an exception not allowing the user to type further
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            try
            {
                int input = Integer.parseInt(dest.toString() + source.toString());
                if (inRange(min, max, input))
                    return null;
            }
            catch (NumberFormatException e)
            {
                e.printStackTrace();
            }
            return "";
        }

        private boolean inRange(int a, int b , int c)
        {
            return b > a ? c >= a && c <= b : c >= b && c <= a;
        }
    }

    // Allow other classes to retrieve the user inputs
    public interface PopupCallback{
        void getValues(View v,int adults, int rooms, Date checkin, Date checkout);
    }
}


