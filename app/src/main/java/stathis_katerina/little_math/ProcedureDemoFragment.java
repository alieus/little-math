package stathis_katerina.little_math;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProcedureDemoFragment extends Fragment {

    protected static interface FillAction {
        void fill();
    }

    protected FillAction fillAtion;

    protected View get(int id) {
        return  getView().findViewById(id);
    }

    public ProcedureDemoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_demonstration, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((Button) get(R.id.fill)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFill();
            }
        });
        ((Button) get(R.id.restart)).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                restart();
            }
        });
    }

    public void onFill() {
        if (fillAtion != null) {
            fillAtion.fill();
        }
    }

    protected void restart() {

    }

}
