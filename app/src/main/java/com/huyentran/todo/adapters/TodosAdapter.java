package com.huyentran.todo.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.huyentran.todo.activities.MainActivity;
import com.huyentran.todo.models.Todo;
import com.huyentran.todo.views.ItemView;

import java.util.ArrayList;

/**
 * Custom Adapter for Todo items.
 */
public class TodosAdapter extends ArrayAdapter<Todo> {

    public TodosAdapter(Context context, ArrayList<Todo> todos) {
        super(context, 0, todos);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ItemView itemView = (ItemView) convertView;
        if (null == itemView)
            itemView = ItemView.inflate(parent);
        itemView.setItem(position, getItem(position),
                (MainActivity) getContext()); // this may be questionable according to SO
        return itemView;
    }
}
