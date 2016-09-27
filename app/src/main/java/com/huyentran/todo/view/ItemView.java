package com.huyentran.todo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huyentran.todo.R;
import com.huyentran.todo.model.Todo;

/**
 * Custom ListView row.
 */
public class ItemView extends RelativeLayout {
    private TextView tvId;
    private TextView tvValue;

    public ItemView(Context c) {
        this(c, null);
    }

    public ItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater.from(context).inflate(R.layout.item_todo, this, true);
        setupChildren();
    }

    public static ItemView inflate(ViewGroup parent) {
        return (ItemView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_view, parent, false);
    }

    public void setItem(Todo item) {
        tvId.setText(String.valueOf(item.getId()));
        tvValue.setText(item.getValue());
    }

    public TextView getIdTextView() {
        return tvId;
    }

    public TextView getValueTextView() {
        return tvValue;
    }

    private void setupChildren() {
        tvId = (TextView) findViewById(R.id.tvId);
        tvValue = (TextView) findViewById(R.id.tvValue);
    }
}
