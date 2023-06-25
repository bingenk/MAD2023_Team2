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


public class AccommodationPopup{

    private View view;
    private int adults, rooms;
    private Date checkin,checkout;
    private boolean initial;
    public AccommodationPopup(View v, int a, int r, Date ci, Date co, boolean in, final PopupCallback callback)
    {
        view = v;
        adults = a;
        rooms = r;
        checkin = ci;
        checkout = co;
        initial = in;

        Context context = v.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View pv = inflater.inflate(R.layout.accommodations_popupfilters, null);
        PopupWindow pw = new PopupWindow(pv, ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT, true);
        pw.setAnimationStyle(android.R.style.Animation_Dialog);
        pw.showAtLocation(v, Gravity.CENTER, 0, 0);

        Button cancel = pv.findViewById(R.id.filter_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pw.dismiss();
            }
        });
        if (initial)
        {
            cancel.setVisibility(View.GONE);
        }

        EditText editAdults = pv.findViewById(R.id.filter_ppl);
        EditText editRooms = pv.findViewById(R.id.filter_rooms);
        EditText editCheckin = pv.findViewById(R.id.filters_checkin);
        EditText editCheckout = pv.findViewById(R.id.filters_checkout);
        Button apply = pv.findViewById(R.id.filter_apply);

        editAdults.setText(String.valueOf(adults));
        editRooms.setText(String.valueOf(rooms));

        editAdults.setFilters(new InputFilter[]{new MaxMinFilter(1,29)});
        editRooms.setFilters(new InputFilter[]{new MaxMinFilter(1,29)});


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

        editCheckin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();

                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        checkin = new Date(year-1900,month,dayOfMonth);
                        String date = dateFormat.format(checkin);
                        editCheckin.setText(date);
                    }
                }, year, month, day);
                Date tmr = new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000);
                dialog.getDatePicker().setMinDate(tmr.getTime());
                if (checkout != null)
                {
                    checkout = null;
                    Toast.makeText(v.getContext(), "Checkout date has been resetted. Please choose again later", Toast.LENGTH_SHORT).show();
                }
                dialog.show();
            }
        });

        editCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    dialog.getDatePicker().setMinDate(checkin.getTime()+ 24 * 60 * 60 * 1000);
                    dialog.show();
                }
                else
                {
                    Toast.makeText(v.getContext(),"Please select the check-in date first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    private class MaxMinFilter implements InputFilter
    {
        private int min, max;

        public MaxMinFilter(int min, int max)
        {
            this.min = min;
            this.max = max;
        }
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
    public interface PopupCallback{
        void getValues(View v,int adults, int rooms, Date checkin, Date checkout);
    }
}


