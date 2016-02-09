package stathis_katerina.little_math;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.Arrays;
import java.util.List;

import static stathis_katerina.little_math.UnfilledLine.*;

public class Equation extends ProcedureDemoFragment {

    public Equation() {

    }

    static final List<String> SIGN = Arrays.asList("+/-", "+", "-");

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Activity act = getActivity();
        if (act instanceof LittleMath) {
            ((LittleMath) act).setTitle("Εξισώσεις");
        }
        stepZero();
    }

    @Override
    protected void restart() {
        super.restart();
        ((LinearLayout) get(R.id.content)).removeAllViews();
        setComment("");
        stepZero();
    }

    void stepZero() {
        setComment("Συμπλήρωσε την εξίσωση που θες να λύσεις");
        get(R.id.fill).setVisibility(View.INVISIBLE);
        get(R.id.restart).setVisibility(View.INVISIBLE);

        final UnfilledLineView line = new UnfilledLineView(getContext());
        line.setModel(new UnfilledLine(Arrays.asList(
                new Element(null, ElementType.NUMBER, null),
                new Element("x"),
                new Element(SIGN, null, null),
                new Element(null, ElementType.UNSIGNED_NUMBER, null),
                new Element("="),
                new Element(null, ElementType.NUMBER, null)
        )));
        line.setDisableOnCorrect(false);
        ((LinearLayout) get(R.id.content)).addView(line);

        final Button ok = Common.makeOkButton(getContext(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (line.getModel().isAllFilled()) {
                    stepOne(line.getModel());
                    v.setVisibility(View.GONE);
                }
            }
        });
        line.addView(ok, Common.makeMarginLayoutParams(100, 0, 0, 0));
    }

    void stepOne(UnfilledLine prevLine) {
        setComment("Πέρνα το "+prevLine.getElements().get(3).getValue()+" στο αλλο μέλος και άλλαξε του το πρόσημο");
        final UnfilledLineView line = new UnfilledLineView(getContext());
        line.setModel(new UnfilledLine(Arrays.asList(
                new Element(null, ElementType.NUMBER, prevLine.getElements().get(0).getValue()),
                new Element("x"),
                new Element("="),
                new Element(null, ElementType.NUMBER, prevLine.getElements().get(5).getValue()),
                new Element(SIGN, null, prevLine.getElements().get(2).getValue().equals("+") ? "-" : "+"),
                new Element(null, ElementType.UNSIGNED_NUMBER, prevLine.getElements().get(3).getValue())
        )));
        ((LinearLayout) get(R.id.content)).addView(line);
        line.setDisableOnCorrect(true);

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
        setComment("Κάνε τις πράξεις στο δεξί μέλος");

        final double p1 = Double.parseDouble(prevLine.getElements().get(0).getValue());
        final double n1 = Double.parseDouble(prevLine.getElements().get(3).getValue());
        final double n2 = Double.parseDouble(prevLine.getElements().get(5).getValue());
        final double rightHand = prevLine.getElements().get(4).getValue().equals("+") ? n1 + n2 : n1 - n2;
        final UnfilledLineView line = new UnfilledLineView(getContext());

        line.setModel(new UnfilledLine(Arrays.asList(
                new Element(null, ElementType.NUMBER, prevLine.getElements().get(0).getValue()),
                new Element("x"),
                new Element("="),
                new Element(null, ElementType.NUMBER, rightHand + "")
        )));
        ((LinearLayout) get(R.id.content)).addView(line);
        line.setDisableOnCorrect(true);

        fillAtion = new FillAction() {
            @Override public void fill() {
                line.fill();
            }
        };

        line.setOnFilled(new UnfilledLineView.FilledListener() {
            @Override

            public void onFilled(UnfilledLineView unfilledLineView) {
                if ((p1 == 0) && (rightHand == 0)) {
                    setComment("Η εξίσωση έχει άπειρες λύσεις");
                    get(R.id.restart).setVisibility(View.VISIBLE);
                } else if ((p1 == 0) && (rightHand != 0)) {
                    setComment("Η εξίσωση δεν έχει καμία λύση");
                    get(R.id.restart).setVisibility(View.VISIBLE);
                } else {
                    double result = rightHand / p1;
                    setComment("Η εξίσωση έχει μια μοναδική λύση");
                    stepThree(p1, result);
                }
            }
        });
    }

    void stepThree(double a, final double result) {
        setComment("Διαίρεσε και τα δύο μέλη με "+a);
        final UnfilledLineView line = new UnfilledLineView(getContext());
        line.setModel(new UnfilledLine(Arrays.asList(
                new Element("x"),
                new Element("="),
                new Element(null, ElementType.NUMBER, result + "")
        )));
        ((LinearLayout) get(R.id.content)).addView(line);
        line.setDisableOnCorrect(true);

        fillAtion = new FillAction() {
            @Override public void fill() {
                line.fill();
            }
        };

        line.setOnFilled(new UnfilledLineView.FilledListener() {
            @Override public void onFilled(UnfilledLineView unfilledLineView) {
                setComment("Μπράβο! Το βρήκες!");
                get(R.id.restart).setVisibility(View.VISIBLE);
                get(R.id.fill).setVisibility(View.INVISIBLE);
            }
        });
    }


}
