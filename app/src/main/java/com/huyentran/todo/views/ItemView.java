package com.huyentran.todo.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huyentran.todo.R;
import com.huyentran.todo.models.Todo;
import com.huyentran.todo.utils.Constants;
import com.huyentran.todo.utils.DateUtils;

import java.util.Calendar;

import static com.huyentran.todo.utils.DateUtils.TODAY;
import static com.huyentran.todo.utils.DateUtils.TOMORROW;
import static com.huyentran.todo.utils.DateUtils.YESTERDAY;

/**
 * Custom ListView row.
 */
public class ItemView extends RelativeLayout {
    private CheckBox cbStatus;
    private TextView tvValue;
    private TextView tvDueDate;
    private TextView tvPriority;

    public interface ItemViewListener {
        void onItemViewCheckBoxToggle(int pos);
    }

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

    public void setItem(int pos, Todo todo, ItemViewListener listener) {
        tvValue.setText(todo.getValue());
        setCheckBox(pos, todo, listener);
        setDueDate(todo);
        setPriority(todo);
    }

    private void setupChildren() {
        tvValue = (TextView) findViewById(R.id.tvValue);
        cbStatus = (CheckBox) findViewById(R.id.cbStatus);
        tvDueDate = (TextView) findViewById(R.id.tvDueDate);
        tvPriority = (TextView) findViewById(R.id.tvPriority);
    }

    private void setCheckBox(final int pos, Todo item, final ItemViewListener listener) {
        cbStatus.setChecked(item.getStatus());
        cbStatus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemViewCheckBoxToggle(pos);
            }
        });
    }

    private void setDueDate(Todo todo) {
        String dueDateStr = todo.getDueDate();
        if (dueDateStr == null || dueDateStr.isEmpty()) {
            tvDueDate.setText(Constants.EMPTY_STRING);
        } else {
            Calendar dueDate = DateUtils.getDateFromString(dueDateStr);
            // check due date relation to today
            int datePosition = DateUtils.compareToday(dueDate);
            if (datePosition < TODAY) {
                tvDueDate.setTextColor(Color.RED);
                if (datePosition == YESTERDAY) {
                    dueDateStr = getResources().getString(R.string.tv_yesterday);
                }
            } else if (datePosition > TODAY) {
                tvDueDate.setTextColor(Color.DKGRAY);
                if (datePosition == TOMORROW) {
                    dueDateStr = getResources().getString(R.string.tv_tomorrow);
                }
            } else {
                tvDueDate.setTextColor(Color.GREEN);
                dueDateStr = getResources().getString(R.string.tv_today);
            }
            tvDueDate.setText(String.format(getResources().getString(R.string.tv_due_date_label_format), dueDateStr));
        }
    }

    private void setPriority(Todo todo) {
        switch (todo.getPriority()) {
            case (Todo.PRIORITY_HI):
                tvPriority.setText(getResources().getString(R.string.tv_high));
                tvPriority.setTextColor(Color.RED);
                break;
            case (Todo.PRIORITY_MED):
                tvPriority.setText(getResources().getString(R.string.tv_med));
                tvPriority.setTextColor(Color.YELLOW);
                break;
            default:
                tvPriority.setText(getResources().getString(R.string.tv_low));
                tvPriority.setTextColor(Color.GREEN);
                break;
        }
    }
}
