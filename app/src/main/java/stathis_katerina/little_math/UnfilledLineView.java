package stathis_katerina.little_math;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
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

    static interface FilledListener {
        void onFilled(UnfilledLineView unfilledLineView);
    }

    private UnfilledLine model;
    private FilledListener onFilledListener;
    private boolean disableOnCorrect = true;

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

    public FilledListener getOnFilled() {
        return onFilledListener;
    }

    public void setOnFilled(FilledListener onFilled) {
        this.onFilledListener = onFilled;
    }

    public boolean isDisableOnCorrect() {
        return disableOnCorrect;
    }

    public void setDisableOnCorrect(boolean disableOnCorrect) {
        this.disableOnCorrect = disableOnCorrect;
    }

    class ElementTextChanged implements TextWatcher {
        private final Element elem;
        private final TextView textView;

        public ElementTextChanged(Element elem, TextView textView) {
            this.elem = elem;
            this.textView = textView;
        }

        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            elem.setValue(textView.getText() == null ? null : textView.getText().toString());
            if (elem.isCorrect()) {
                textView.setTextColor(Color.GREEN);
                if (disableOnCorrect) textView.setEnabled(false);
                if (model.isAllCorrect()) {
                    if (onFilledListener != null) onFilledListener.onFilled(UnfilledLineView.this);
                }
            } else {
                textView.setTextColor(Color.RED);
            }
        }
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
                case UNSIGNED_NUMBER:
                    editText = new EditText(getContext());
                    editText.setInputType(TYPE_CLASS_NUMBER | TYPE_NUMBER_FLAG_DECIMAL);
                    view = editText;
                    break;
                case TEXT:
                    TextView textViewT = new TextView(getContext());
                    textViewT.setText(elem.getValue());
                    textViewT.setTextSize(30);

                    view = textViewT;
                    break;
                case LIST:
                default:
                    editText = new EditText(getContext());
                    editText.setInputType(TYPE_CLASS_TEXT | TYPE_TEXT_VARIATION_NORMAL);
                    view = editText;
            }

            if (view instanceof TextView) {
                ((TextView) view).addTextChangedListener(new ElementTextChanged(elem, (TextView) view));
            }

            addView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
    }

    public void fill() {
        int count = model.getElements().size();
        for (int i = 0; i < count; i++) {
            Element elem = model.getElements().get(i);
            switch (elem.getType()) {
                default:
                    elem.setValue(elem.getCorrectValue());
                    ((TextView) getChildAt(i)).setText(elem.getValue());
            }
        }
    }
}
