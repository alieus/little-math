package stathis_katerina.little_math;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.Arrays;
import static stathis_katerina.little_math.UnfilledLine.*;

public class Equation extends ProcedureDemoFragment {
    String apotelesma;


    public Equation() {

    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        stepZero();
    }

    void stepZero() {
        get(R.id.fill).setVisibility(View.INVISIBLE);
        get(R.id.restart).setVisibility(View.INVISIBLE);

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
        ((LinearLayout) get(R.id.content)).addView(line);

        final Button ok = new Button(getContext());
        ok.setText("ΟΚ!");
        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        params.setMargins(100, 0, 0, 0);
        line.addView(ok, params);
        ok.setPadding(20, 20, 20, 20);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        ((LinearLayout) get(R.id.content)).addView(line);

        fillAtion = new FillAction() {
            @Override public void fill() {
                line.fill();
            }
        };
        get(R.id.fill).setVisibility(View.VISIBLE);

        line.setOnFilled(new UnfilledLineView.FilledListener() {
            @Override
            public void onFilled(UnfilledLineView unfilledLineView) {
                stepTwo(line.getModel());
            }
        });
    }

    void stepTwo(final UnfilledLine prevLine) {
        double p1=Double.parseDouble(prevLine.getElements().get(0).getValue());
        double n1 = Double.parseDouble(prevLine.getElements().get(3).getValue());
        double n2 = Double.parseDouble(prevLine.getElements().get(5).getValue());
        final double result = prevLine.getElements().get(5).getValue().equals("+") ? n1 + n2 : n1 - n2;
        final UnfilledLineView line = new UnfilledLineView(getContext());

        line.setModel(new UnfilledLine(Arrays.asList(
                new Element(null, ElementType.NUMBER, prevLine.getElements().get(0).getValue()),
                new Element("x"),
                new Element("="),
                new Element(null, ElementType.NUMBER, result + "")
        )));
        ((LinearLayout) get(R.id.content)).addView(line);

        final double upologismos;
        if ((p1 == 0) && (result == 0)) {
            apotelesma = "Η εξίσωση έχει άπειρες λύσεις!";
            System.out.println(apotelesma);
            upologismos = 0.0;
        } else if ((p1 == 0) && (result != 0)) {
            apotelesma = "Η εξίσωση δεν έχει καμία λύση!";
            System.out.println(apotelesma);
            upologismos = 0.0;
        } else if ((p1 != 0) && (result != 0)) {
            upologismos = result / p1;
            apotelesma = "Η εξίσωση έχει μια μόνο λύση! " + upologismos;
            System.out.println(apotelesma);
            System.out.println(upologismos);
        } else {// if ((p1!=0) && (result==0)){
            upologismos = 0;
            apotelesma = "Η εξίσωση έχει μια μόνο λύση! " + upologismos;
            System.out.println(apotelesma);
            System.out.println(upologismos);
        }

        fillAtion = new FillAction() {
            @Override public void fill() {
                line.fill();
            }
        };
        line.setOnFilled(new UnfilledLineView.FilledListener() {
            @Override

            public void onFilled(UnfilledLineView unfilledLineView) {

               prior_step_three(upologismos);
                stepThree(apotelesma);
            }
        });
    }

    void prior_step_three(double upologismos){
        final UnfilledLineView line = new UnfilledLineView(getContext());
        line.setModel(new UnfilledLine(Arrays.asList(
                new Element("x"),
                new Element("="),
                new Element(upologismos + "", ElementType.NUMBER, upologismos + "")
                //new Element(upologismos, ElementType.NUMBER, upologismos)
        )));
        ((LinearLayout) get(R.id.content)).addView(line);

    }
    void stepThree(String apotelesma) {

        final UnfilledLineView line = new UnfilledLineView(getContext());
        line.setModel(new UnfilledLine(Arrays.asList(
                new Element(apotelesma, ElementType.TEXT, apotelesma)
        )));
        ((LinearLayout) get(R.id.content)).addView(line);


    }

}
