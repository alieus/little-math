package stathis_katerina.little_math;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class Common {
    public static Button makeOkButton(Context context, View.OnClickListener action) {
        Button ok = new Button(context);
        ok.setText("OK!");
        ok.setPadding(20, 20, 20, 20);
        ok.setOnClickListener(action);
        return ok;
    }

    public static ViewGroup.MarginLayoutParams makeMarginLayoutParams(int left, int top, int right, int bottom) {
        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        params.setMargins(100, 0, 0, 0);
        return params;
    }
}
