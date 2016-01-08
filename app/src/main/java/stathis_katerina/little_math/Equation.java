package stathis_katerina.little_math;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        UnfilledLineView line = new UnfilledLineView(getContext());
        line.setModel(new UnfilledLine(Arrays.asList(
                new Element(null, ElementType.NUMBER, null),
                new Element("x"),
                new Element("+", ElementType.LIST, "+"),
                new Element(null, ElementType.NUMBER, null),
                new Element("="),
                new Element(null, ElementType.NUMBER, null)
        )));
        ((LinearLayout) get(R.id.main)).addView(line);
    }
}
