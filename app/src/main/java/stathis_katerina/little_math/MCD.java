package stathis_katerina.little_math;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static stathis_katerina.little_math.UnfilledLine.*;


/**
 * A simple {@link Fragment} subclass.
 */
public class MCD extends Fragment {


    public MCD() {
        // Required empty public constructor
    }

    private View get(int id) {
        return  getView().findViewById(id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mcd, container, false);
    }

    private void askNumberCount() {
        final UnfilledLineView line = new UnfilledLineView(getContext());
        line.setModel(new UnfilledLine(Arrays.asList(
                new Element("Πόσοι είναι οι αριθμοί;"),
                new Element("2", ElementType.NATURAL, null)
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
            @Override
            public void onClick(View v) {
            if (line.getModel().isAllFilled()) {
                chooseNumbers(Integer.parseInt(line.getModel().getElements().get(1).getValue()));
                ok.setVisibility(View.GONE);
                line.setEnabled(false);
            }
            }
        });
    }

    private void chooseNumbers(int numberCount) {
        List<Element> elems = new LinkedList<>();
        for (int i = 0; i < numberCount; i++) {
            elems.add(new Element(null, ElementType.NATURAL, null));
        }
    }

    private void writeDivisor() {

    }

    private void divide() {

    }

    private void multiplyDivisors() {

    }

}
