package stathis_katerina.little_math;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import static android.text.InputType.*;

import static stathis_katerina.little_math.UnfilledLine.*;

/**
 * Created by Katerina on 8/1/2016.
 */
public class UnfilledLineView extends LinearLayout {

    private UnfilledLine model;

    public UnfilledLineView(Context context) {
        super(context);
    }

    public UnfilledLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UnfilledLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public UnfilledLine getModel() {
        return model;
    }

    public void setModel(UnfilledLine model) {
        this.model = model;
        removeAllViews();

        for (Element elem : model.getElements()) {
            View view;
            switch (elem.getType()) {
                case CONSTANT:
                    TextView textView = new TextView(getContext());
                    textView.setText(elem.getValue());
                    textView.setTextSize(30);

                    view = textView;
                    break;
                case INTEGER:
                    EditText editText = new EditText(getContext());
                    editText.setInputType(TYPE_CLASS_NUMBER | TYPE_NUMBER_FLAG_SIGNED);
                    view = editText;
                    break;
                case NATURAL:
                    editText = new EditText(getContext());
                    editText.setInputType(TYPE_CLASS_NUMBER | TYPE_NUMBER_VARIATION_NORMAL);
                    view = editText;
                    break;
                case NUMBER:
                    editText = new EditText(getContext());
                    editText.setInputType(TYPE_CLASS_NUMBER | TYPE_NUMBER_FLAG_DECIMAL | TYPE_NUMBER_FLAG_SIGNED);
                    view = editText;
                    break;
                case LIST:
                default:
                    editText = new EditText(getContext());
                    editText.setInputType(TYPE_CLASS_TEXT | TYPE_TEXT_VARIATION_NORMAL);
                    view = editText;
            }

            addView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
    }
}
