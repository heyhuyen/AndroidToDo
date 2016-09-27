package com.huyentran.todo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Custom Adapter for Todo items.
 */
public class TodosAdapter extends ArrayAdapter<Todo> {
    // View lookup cache
    private static class ViewHolder {
        TextView value;
        TextView id;
    }

    public TodosAdapter(Context context, ArrayList<Todo> todos) {
        super(context, 0, todos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Todo todo = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_todo, parent, false);
            viewHolder.value = (TextView) convertView.findViewById(R.id.tvValue);
            viewHolder.id = (TextView) convertView.findViewById(R.id.tvId);
            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object
        viewHolder.value.setText(todo.value);
        viewHolder.id.setText(String.valueOf(todo.id));
        // Return the completed view to render on screen
        return convertView;

    }
}
