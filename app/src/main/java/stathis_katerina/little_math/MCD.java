package stathis_katerina.little_math;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static stathis_katerina.little_math.UnfilledLine.*;


/**
 * A simple {@link Fragment} subclass.
 */
public class MCD extends ProcedureDemoFragment {

    private int mcd;

    public MCD() {
        // Required empty public constructor
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        get(R.id.restart).setVisibility(View.INVISIBLE);
        askNumberCount();
    }

    @Override
    protected void restart() {
        super.restart();
        ((LinearLayout) get(R.id.content)).removeAllViews();
        ((TextView) get(R.id.comment)).setText("");
        get(R.id.restart).setVisibility(View.INVISIBLE);
        askNumberCount();
    }

    private void askNumberCount() {
        get(R.id.fill).setVisibility(View.INVISIBLE);
        final UnfilledLineView line = new UnfilledLineView(getContext());
        line.setModel(new UnfilledLine(Arrays.asList(
                new Element("Πόσοι είναι οι αριθμοί;  "),
                new Element("2", ElementType.NATURAL, null)
        )));
        line.setDisableOnCorrect(false);
        ((LinearLayout) get(R.id.content)).addView(line);

        final Button ok = Common.makeOkButton(getContext(), new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (line.getModel().isAllFilled()) {
                    line.setVisibility(View.GONE);
                    int numberCount = Integer.parseInt(line.getModel().getElements().get(1).getValue());
                    chooseNumbers(numberCount);
                }
            }
        });
        line.addView(ok, Common.makeMarginLayoutParams(100, 0, 0, 0));
    }

    private void chooseNumbers(int numberCount) {
        mcd = 1;
        ((TextView) get(R.id.comment)).setText("Συμπλήρωσε τους "+numberCount+" αριθμούς");

        List<Element> elems = new LinkedList<>();
        for (int i = 0; i < numberCount; i++) {
            elems.add(new Element(null, ElementType.NATURAL, null));
        }
        final UnfilledLineView line = new UnfilledLineView(getContext());
        line.setModel(new UnfilledLine(elems));
        line.setDisableOnCorrect(false);
        ((LinearLayout) get(R.id.content)).addView(line);

        final Button ok = Common.makeOkButton(getContext(), new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (line.getModel().isAllFilled()) {
                    line.setDisableOnCorrect(true);
                    writeDivisor(line, 2);
                    v.setVisibility(View.GONE);
                }
            }
        });
        line.addView(ok, Common.makeMarginLayoutParams(100, 0, 0, 0));
    }

    private void writeDivisor(final UnfilledLineView line, int lastDivisor) {
        final List<Element> prevElements = line.getModel().getElements();
        final List<Element> elements = new LinkedList<>();
        boolean foundDivisor = false;
        boolean noDivisor = false;

        while (!foundDivisor) {
            foundDivisor = true;
            for (Element elem : prevElements) {
                int elemValue = Integer.parseInt(elem.getValue());
                if (elemValue < lastDivisor) {
                    noDivisor = true; break;
                }
                if (elemValue % lastDivisor != 0) {
                    foundDivisor = false; break;
                }
            }
            if (!foundDivisor) lastDivisor = new BigInteger(lastDivisor+"").nextProbablePrime().intValue();
        }
        if (noDivisor) {
            multiplyDivisors();
            return;
        }

        final int divisor = lastDivisor;
        mcd *= divisor;

        ((TextView) get(R.id.comment)).setText("Βρες το μικρότερο αριθμό που είναι κοινός διαιρέτης");
        elements.addAll(prevElements);
        elements.addAll(Arrays.asList(new Element("|"), new Element(null, ElementType.NATURAL, divisor + "")));
        line.setModel(new UnfilledLine(elements));

        get(R.id.fill).setVisibility(View.VISIBLE);
        fillAtion = new FillAction() {
            @Override public void fill() {
                ((EditText) line.getChildAt(line.getModel().getElements().size()-1)).setText(divisor+"");
            }
        };

        line.setOnFilled(new UnfilledLineView.FilledListener() {
            @Override
            public void onFilled(UnfilledLineView unfilledLineView) {
                divide(line.getModel(), divisor);
            }
        });
    }

    private void divide(UnfilledLine prevLine, final int divisor) {
        ((TextView) get(R.id.comment)).setText("Διαίρεσε τους αριθμούς με το διαιρέτη που βρήκες");
        List<Element> elems = new LinkedList<>();
        boolean terminated = false;

        for (Element prevElem : prevLine.getElements()) {
            if ("|".equals(prevElem.getValue())) break;

            int prev = Integer.parseInt(prevElem.getValue());
            if (prev / divisor > divisor) terminated = true;
            elems.add(new Element(null, ElementType.NATURAL, (prev / divisor)+""));
        }

        final UnfilledLineView line = new UnfilledLineView(getContext());
        line.setDisableOnCorrect(true);
        line.setModel(new UnfilledLine(elems));
        ((LinearLayout) get(R.id.content)).addView(line);

        fillAtion = new FillAction() {
            @Override public void fill() {
                line.fill();
            }
        };

        final boolean terminatedFinal = terminated;
        line.setOnFilled(new UnfilledLineView.FilledListener() {
            @Override
            public void onFilled(UnfilledLineView unfilledLineView) {
                if (terminatedFinal) {
                    writeDivisor(line, divisor);
                } else {
                    multiplyDivisors();
                }
            }
        });
    }

    private void multiplyDivisors() {
        if (mcd > 1) {
            ((TextView) get(R.id.comment)).setText("Πλέον δεν υπάρχει κοινός διαιρέτης εκτός από το 1, πολλαπλασίασε όλους τους διαιρέτες που χρησιμοποίησες για να βρεις το αποτέλεσμα");
        } else {
            ((TextView) get(R.id.comment)).setText("Οι αριθμοί που έδωσες δεν έχουν άλλο κοινό διαιρέτη εκτός από το 1");
        }
        final UnfilledLineView line = new UnfilledLineView(getContext());
        line.setDisableOnCorrect(true);
        line.setModel(new UnfilledLine(Arrays.asList(
                new Element("Ο μέγιστος κοινός διαιρέτης είναι: "),
                new Element(null, ElementType.NATURAL, mcd + "")
        )));
        ((LinearLayout) get(R.id.content)).addView(line);

        fillAtion = new FillAction() {
            @Override public void fill() {
                line.fill();
            }
        };

        line.setOnFilled(new UnfilledLineView.FilledListener() {
            @Override  public void onFilled(UnfilledLineView unfilledLineView) {
                ((TextView) get(R.id.comment)).setText("Μπράβο!");
                get(R.id.restart).setVisibility(View.VISIBLE);
                get(R.id.fill).setVisibility(View.INVISIBLE);
            }
        });
    }

}
