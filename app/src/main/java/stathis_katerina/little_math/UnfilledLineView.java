package stathis_katerina.little_math;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import static android.text.InputType.*;

import static stathis_katerina.little_math.UnfilledLine.*;
import android.app.AlertDialog;

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
                textView.setTextColor(Color.argb(255, 1, 77, 0));
                if (disableOnCorrect) textView.setEnabled(false);
                if (model.isAllCorrect()) {
                    if (onFilledListener != null) onFilledListener.onFilled(UnfilledLineView.this);
                }
            } else {
                textView.setTextColor(Color.RED);
            }
        }
    }

    private void onListClicked(final Element elem, final TextView v) {
        if (!v.isEnabled()) return;
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setTitle("Επιλέξτε "+ elem.getOptions().get(0))
                .setItems(elem.getOptions().subList(1, elem.getOptions().size()).toArray(new String[0]), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String val = elem.getOptions().get(which+1);
                        v.setText(val);
                        v.refreshDrawableState();
                        elem.setValue(val);

                        if (elem.isCorrect()) {
                            v.setTextColor(Color.argb(255, 1, 77, 0));
                            if (disableOnCorrect) v.setEnabled(false);
                            if (model.isAllCorrect()) {
                                if (onFilledListener != null) onFilledListener.onFilled(UnfilledLineView.this);
                            }
                        } else {
                            v.setTextColor(Color.RED);
                        }
                    }
                });
        builder.show();
    }


    public void setModel(UnfilledLine model) {
        this.model = model;
        removeAllViews();

        for (final Element elem : model.getElements()) {
            View view;
            switch (elem.getType()) {
                case CONSTANT:
                    TextView textView = new TextView(getContext());
                    textView.setText(elem.getValue());
                    textView.setTextSize(30);
                    textView.setPadding(10, 0, 10, 0);
                    textView.setTextColor(Color.argb(200, 0, 0, 0));
                    view = textView;
                    break;
                case INTEGER:
                    EditText editText = new EditText(getContext());
                    editText.setInputType(TYPE_CLASS_NUMBER | TYPE_NUMBER_FLAG_SIGNED);
                    editText.setText(elem.getValue());
                    view = editText;
                    break;
                case NATURAL:
                    editText = new EditText(getContext());
                    editText.setInputType(TYPE_CLASS_NUMBER);
                    editText.setText(elem.getValue());
                    view = editText;
                    break;
                case NUMBER:
                    editText = new EditText(getContext());
                    editText.setInputType(TYPE_CLASS_NUMBER | TYPE_NUMBER_FLAG_DECIMAL | TYPE_NUMBER_FLAG_SIGNED);
                    editText.setText(elem.getValue());
                    view = editText;
                    break;
                case UNSIGNED_NUMBER:
                    editText = new EditText(getContext());
                    editText.setInputType(TYPE_CLASS_NUMBER | TYPE_NUMBER_FLAG_DECIMAL);
                    editText.setText(elem.getValue());
                    view = editText;
                    break;
                case TEXT:
                    TextView textViewT = new TextView(getContext());
                    textViewT.setText(elem.getValue());
                    textViewT.setTextSize(30);

                    view = textViewT;
                    break;
                case LIST:
                    final TextView textView2 = new TextView(getContext());
                    textView2.setText(elem.getValue() == null ? elem.getOptions().get(0) : elem.getValue());
                    LinearLayout frame = new LinearLayout(getContext());
                    frame.setOrientation(VERTICAL);
                    textView2.setPadding(10, 3, 10, 10);
                    frame.setMinimumWidth(45);
                    frame.setBackgroundResource(R.drawable.back_list_elem);
                    textView2.setTextSize(30);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                    params.gravity = Gravity.CENTER_HORIZONTAL;
                    frame.addView(textView2, params);
                    textView2.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onListClicked(elem, textView2);
                        }
                    });
                    view = frame;
                    break;
                default:
                    editText = new EditText(getContext());
                    editText.setInputType(TYPE_CLASS_TEXT | TYPE_TEXT_VARIATION_NORMAL);
                    view = editText;
            }

            if (view instanceof TextView) {
                ((TextView) view).addTextChangedListener(new ElementTextChanged(elem, (TextView) view));
            }

            if (view instanceof EditText) {
                ((TextView) view).setMinWidth(45);
                ((EditText) view).setTextSize(26);
            }

            addView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
    }

    public void fill() {
        int count = model.getElements().size();
        for (int i = 0; i < count; i++) {
            Element elem = model.getElements().get(i);
            switch (elem.getType()) {
                case LIST:
                    elem.setValue(elem.getCorrectValue());
                    TextView v = ((TextView) ((ViewGroup) getChildAt(i)).getChildAt(0));
                    v.setText(elem.getValue());
                    if (disableOnCorrect) {
                        v.setEnabled(false);
                    }
                    v.setTextColor(Color.argb(255, 1, 77, 0));
                    break;
                default:
                    elem.setValue(elem.getCorrectValue());
                    ((TextView) getChildAt(i)).setText(elem.getValue());
            }
        }
    }
}
