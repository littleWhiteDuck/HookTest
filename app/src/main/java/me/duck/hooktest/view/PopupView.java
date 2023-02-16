package me.duck.hooktest.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.ViewGroup;

import androidx.appcompat.view.ContextThemeWrapper;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import me.duck.hooktest.R;

public class PopupView extends ViewGroup {
    public MaterialTextView title;
    public MaterialTextView content;
    public MaterialButton confirm;
    public MaterialButton cancel;
    private final LayoutParams textViewParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    private final LayoutParams buttonViewParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

    @SuppressLint({"UseCompatLoadingForDrawables", "ResourceType"})
    public PopupView(Context context) {
        super(context);
        title = new MaterialTextView(new ContextThemeWrapper(context, R.style.PopupWindow_Title));
        content = new MaterialTextView(context);
        cancel = new MaterialButton(context);
        confirm = new MaterialButton(context);
        addView(title);
        addView(content);
        addView(confirm);
        addView(cancel);
        int padding = (int) context.getResources().getDimension(R.dimen.popup_padding);
        setPadding(padding, padding, padding, padding);
    }


    @SuppressLint("Range")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int textViewWidth = getMeasuredWidth() - getPaddingStart() - getPaddingEnd();
        title.setLayoutParams(textViewParams);
        content.setLayoutParams(textViewParams);
        confirm.setLayoutParams(buttonViewParams);
        cancel.setLayoutParams(buttonViewParams);
        title.measure(MeasureSpec.makeMeasureSpec(textViewWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(LayoutParams.WRAP_CONTENT, MeasureSpec.AT_MOST));
        content.measure(MeasureSpec.makeMeasureSpec(textViewWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(LayoutParams.WRAP_CONTENT, MeasureSpec.AT_MOST));
        confirm.measure(MeasureSpec.makeMeasureSpec(LayoutParams.WRAP_CONTENT, MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec(LayoutParams.WRAP_CONTENT, MeasureSpec.AT_MOST));
        cancel.measure(MeasureSpec.makeMeasureSpec(LayoutParams.WRAP_CONTENT, MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec(LayoutParams.WRAP_CONTENT, MeasureSpec.AT_MOST));
        setMeasuredDimension(getMeasuredWidth(), getPaddingTop() + title.getMeasuredHeight() + content.getMeasuredHeight() + confirm.getMeasuredHeight() + getPaddingBottom());
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        title.layout(getPaddingStart(), getPaddingTop(), getPaddingStart() + title.getMeasuredWidth(), getPaddingTop() + title.getMeasuredHeight());
        content.layout(title.getLeft(), title.getBottom(), title.getLeft() + content.getMeasuredWidth(), title.getBottom() + content.getMeasuredHeight());
        cancel.layout(title.getLeft(), content.getBottom(), title.getLeft() + cancel.getMeasuredWidth(), content.getBottom() + cancel.getMeasuredHeight());
        confirm.layout(this.getMeasuredWidth() - confirm.getMeasuredWidth() - getPaddingEnd(), content.getBottom(), this.getMeasuredWidth() - confirm.getMeasuredWidth() - getPaddingEnd() + confirm.getMeasuredWidth(), content.getBottom() + confirm.getMeasuredHeight());
    }
}
