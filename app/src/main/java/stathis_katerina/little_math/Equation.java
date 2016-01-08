package stathis_katerina.little_math;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.Arrays;
import static stathis_katerina.little_math.UnfilledLine.*;

public class Equation extends Fragment {

    private View get(int id) {
        return  getView().findViewById(id);
    }

    public Equation() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_equation, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        stepZero();
    }

    void makeFillButton(final UnfilledLineView line) {
        final Button fill = new Button(getContext());
        fill.setText("Fill");
        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        params.setMargins(100, 0, 0, 0);
        line.addView(fill, params);
        fill.setPadding(20, 20, 20, 20);
        fill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                line.fill();
                fill.setVisibility(View.GONE);
            }
        });
    }

    void stepZero() {
        final UnfilledLineView line = new UnfilledLineView(getContext());
        line.setModel(new UnfilledLine(Arrays.asList(
                new Element(null, ElementType.NUMBER, null),
                new Element("x"),
                new Element(null, ElementType.LIST, null),
                new Element(null, ElementType.UNSIGNED_NUMBER, null),
                new Element("="),
                new Element(null, ElementType.NUMBER, null)
        )));
        line.setDisableOnCorrect(false);
        ((LinearLayout) get(R.id.main)).addView(line);

        final Button ok = new Button(getContext());
        ok.setText("OK!");
        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        params.setMargins(100, 0, 0, 0);
        line.addView(ok, params);
        ok.setPadding(20, 20, 20, 20);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (line.getModel().isAllFilled()) {
                    stepOne(line.getModel());
                    ok.setVisibility(View.GONE);
                }
            }
        });


    }

    void stepOne(UnfilledLine prevLine) {
        final UnfilledLineView line = new UnfilledLineView(getContext());
        line.setModel(new UnfilledLine(Arrays.asList(
                new Element(null, ElementType.NUMBER, prevLine.getElements().get(0).getValue()),
                new Element("x"),
                new Element("="),
                new Element(null, ElementType.NUMBER, prevLine.getElements().get(5).getValue()),
                new Element(null, ElementType.LIST, prevLine.getElements().get(2).getValue().equals("+") ? "-" : "+"),
                new Element(null, ElementType.UNSIGNED_NUMBER, prevLine.getElements().get(3).getValue())
        )));
        ((LinearLayout) get(R.id.main)).addView(line);
        makeFillButton(line);
        line.setOnFilled(new UnfilledLineView.FilledListener() {
            @Override
            public void onFilled(UnfilledLineView unfilledLineView) {
                stepTwo(line.getModel());
            }
        });
    }

    void stepTwo(UnfilledLine prevLine) {
        double n1 = Double.parseDouble(prevLine.getElements().get(3).getValue());
        double n2 = Double.parseDouble(prevLine.getElements().get(5).getValue());
        double result = prevLine.getElements().get(5).getValue().equals("+") ? n1+n2 : n1-n2;
        final UnfilledLineView line = new UnfilledLineView(getContext());
        line.setModel(new UnfilledLine(Arrays.asList(
                new Element(null, ElementType.NUMBER, prevLine.getElements().get(0).getValue()),
                new Element("x"),
                new Element("="),
                new Element(null, ElementType.NUMBER, result+"")
        )));
        ((LinearLayout) get(R.id.main)).addView(line);
        makeFillButton(line);
        line.setOnFilled(new UnfilledLineView.FilledListener() {
            @Override
            public void onFilled(UnfilledLineView unfilledLineView) {
                //stepTwo(line.getModel());
            }
        });
    }
}
