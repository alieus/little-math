package layout;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import stathis_katerina.little_math.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Power extends Fragment {


    public Power() {
        // Required empty public constructor
    }

    private View get(int id) {
        return  getView().findViewById(id);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NumberPicker exp = (NumberPicker) get(R.id.exponent);
        exp.setMaxValue(9);
        exp.setMinValue(0);

        // catch base changes
        ((EditText) get(R.id.base)).addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                updateTexts();
            }
        });

        // catch exponent changes
        exp.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                updateTexts();
            }
        });
        updateTexts();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_power, container, false);
    }


    private Integer getBase() {
        String baseText = ((EditText) get(R.id.base)).getText().toString();
        return baseText.isEmpty() ? null : Integer.parseInt(baseText);
    }

    private int getExp() {
        return ((NumberPicker) get(R.id.exponent)).getValue();
    }

    private void setCalculationText(String text) {
        ((TextView) get(R.id.calculation)).setText(text);
    }

    private void setResultText(String text) {
        ((TextView) get(R.id.result)).setText(text);
    }


    private void updateTexts() {
        Integer base = getBase();
        int exp = getExp();

        String calc;
        String result;
        if (base == null) {
            result = "";
            calc = "Write base";
        } else if (exp == 0) {
            if (base == 0) {
                calc = "Not Defined for base and exponent 0";
                result = "Not Defined";
            } else {
                calc = "By Definition";
                result = "=1";
            }
        } else {
            calc = repeat(base+"X", exp-1)+base;
            result = "="+Math.round(Math.pow(base, exp));
        }

        setResultText(result);
        setCalculationText(calc);

    }

    private static String repeat(String text, int times) {
        StringBuilder sb = new StringBuilder(text.length()*times);
        for (int i = 0; i < times; i++) {
            sb.append(text);
        }
        return sb.toString();
    }
}
