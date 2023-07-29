package sg.edu.np.mad.mad2023_team2.ui.Recommendations;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.Objects;

import sg.edu.np.mad.mad2023_team2.R;


public class RecommendationsPopUp implements AdapterView.OnItemSelectedListener{

    private View view;
    private Integer distance;
    private String unit;
    private String[] unitList = {"Metric (Km)", "Imperial (Miles)"};

    public RecommendationsPopUp(View v, boolean isRestraunt,PopupCallback callback) {
        view = v;

        Context context = v.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View pv = inflater.inflate(R.layout.reccomendations_popupfilters, null);
        PopupWindow pw = new PopupWindow(pv, ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT, true);
        pw.setAnimationStyle(android.R.style.Animation_Dialog);
        pw.showAtLocation(v, Gravity.CENTER, 0, 0);

        EditText radiusInput = pv.findViewById(R.id.reccoPU_radius);
        Spinner unitSpinner = pv.findViewById(R.id.reccoPU_units);
        Button save = pv.findViewById(R.id.reccoPU_save);


        radiusInput.setText("10");
        radiusInput.setFilters(new InputFilter[]{new MaxMinFilter(1,isRestraunt?10:25)});

        if (isRestraunt)
        {
            radiusInput.setHint("1-10");
        }
        else
        {
            radiusInput.setHint("1-25");
        }

        unitSpinner.setOnItemSelectedListener(this);
        ArrayAdapter adapter = new ArrayAdapter(pv.getContext(), android.R.layout.simple_spinner_dropdown_item, unitList);
        unitSpinner.setAdapter(adapter);

        unit = unitList[0];
        unitSpinner.setSelection(0);

        ImageButton cancel = pv.findViewById(R.id.reccoPU_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pw.dismiss();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (unit == null || radiusInput.getText().toString().trim().length() == 0)
                {
                    Toast.makeText(v.getContext(), "Please fill in everything to save", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    distance = Integer.parseInt(radiusInput.getText().toString());
                    callback.getValues(distance, unit.equals(unitList[0]) ?"km":"mi");
                    pw.dismiss();
                }
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        unit = unitList[position];
        Toast.makeText(view.getContext(), unitList[position],Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private class MaxMinFilter implements InputFilter {
        private int min, max;

        public MaxMinFilter(int min, int max) {
            this.min = min;
            this.max = max;
        }

        // Checks if it matches into the regex, allows the input if does, else gives an exception not allowing the user to type further
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            try {
                int input = Integer.parseInt(dest.toString() + source.toString());
                if (inRange(min, max, input))
                    return null;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            return "";
        }

        private boolean inRange(int a, int b, int c) {
            return b > a ? c >= a && c <= b : c >= b && c <= a;
        }

    }
    public interface PopupCallback{
        void getValues(int distance, String unit);
    }
}

