package com.huyentran.todo.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.huyentran.todo.R;
import com.huyentran.todo.models.Todo;
import com.huyentran.todo.utils.DateUtils;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static com.huyentran.todo.utils.Constants.*;
import static com.huyentran.todo.utils.DateUtils.getDateStringFromPicker;

/**
 * {@link DialogFragment} using {@link AlertDialog} for editing todo items in a modal view.
 */
public class EditTodoDialogFragment extends DialogFragment
{

    public interface EditTodoDialogListener {
        void onFinishEditDialog(int pos, Todo todo);
    }

    public EditTodoDialogFragment() {
        // Empty constructor is required for DialogFragment
    }

    public static EditTodoDialogFragment newInstance(int pos, Todo todo) {
        EditTodoDialogFragment fragment = new EditTodoDialogFragment();
        Bundle args = new Bundle();
        args.putInt(TODO_POS_KEY, pos);
        args.putLong(TODO_ID_KEY, todo.getId());
        args.putString(TODO_VALUE_KEY, todo.getValue());
        args.putString(TODO_DUE_DATE_KEY, todo.getDueDate());
        args.putString(TODO_NOTES_KEY, todo.getNotes());
        args.putInt(TODO_PRIORITY_KEY, todo.getPriority());
        fragment.setArguments(args);
        return fragment;
    }

    /** The system calls this only when creating the layout in a dialog. */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View dialogView = inflater.inflate(R.layout.fragment_edit_todo, null);
        alertDialogBuilder.setView(dialogView);
        Bundle args = getArguments();

        // Setup edit text field
        final EditText etValue = (EditText) dialogView.findViewById(R.id.etEditTodo);
        String value = args.getString(TODO_VALUE_KEY);
        etValue.setText(value);
        etValue.setSelection(value.length());

        // Setup due date items
        final ImageButton btnAddDueDate = (ImageButton) dialogView.findViewById(R.id.btnAddDueDate);
        final ImageButton btnRemoveDueDate = (ImageButton) dialogView.findViewById(R.id.btnRemoveDueDate);
        final DatePicker dpDueDate = (DatePicker) dialogView.findViewById(R.id.dpDueDate);
        btnAddDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnAddDueDate.setVisibility(View.GONE);
                btnRemoveDueDate.setVisibility(View.VISIBLE);
                dpDueDate.setVisibility(View.VISIBLE);

                // default to today
                Calendar dueDate = new GregorianCalendar();
                dpDueDate.updateDate(dueDate.get(Calendar.YEAR), dueDate.get(Calendar.MONTH),
                        dueDate.get(Calendar.DAY_OF_MONTH));
            }
        });
        btnRemoveDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnRemoveDueDate.setVisibility(View.GONE);
                btnAddDueDate.setVisibility(View.VISIBLE);
                dpDueDate.setVisibility(View.GONE);
            }
        });
        String dueDateStr = args.getString(TODO_DUE_DATE_KEY);
        if (dueDateStr == null) {
            btnAddDueDate.setVisibility(View.VISIBLE);
            btnRemoveDueDate.setVisibility(View.GONE);
            dpDueDate.setVisibility(View.GONE);
        } else {
            btnAddDueDate.setVisibility(View.GONE);
            btnRemoveDueDate.setVisibility(View.VISIBLE);
            dpDueDate.setVisibility(View.VISIBLE);

            Calendar dueDate = DateUtils.getDateFromString(dueDateStr);
            dpDueDate.updateDate(dueDate.get(Calendar.YEAR), dueDate.get(Calendar.MONTH),
                    dueDate.get(Calendar.DAY_OF_MONTH));
        }

        // Setup notes field
        final EditText etNotes = (EditText) dialogView.findViewById(R.id.etTodoNotes);
        etNotes.setText(args.getString(TODO_NOTES_KEY));

        // setup priority spinner
        final Spinner spPriority = (Spinner) dialogView.findViewById(R.id.spPriority);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.priority_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPriority.setAdapter(adapter);
        spPriority.setSelection(args.getInt(TODO_PRIORITY_KEY));

        // alert dialog buttons
        alertDialogBuilder.setPositiveButton(R.string.btn_save, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Return input text back to activity through the implemented listener
                    EditTodoDialogListener listener = (EditTodoDialogListener) getActivity();
                    Bundle args = getArguments();
                    Todo.TodoBuilder todoBuilder = new Todo.TodoBuilder(etValue.getText().toString())
                            .id(args.getLong(TODO_ID_KEY))
                            .status(false)
                            .notes(etNotes.getText().toString())
                            .priority(spPriority.getSelectedItemPosition());
                    if (dpDueDate.getVisibility() == View.VISIBLE) {
                        todoBuilder.dueDate(getDateStringFromPicker(dpDueDate));
                    }
                    listener.onFinishEditDialog(args.getInt(TODO_POS_KEY), todoBuilder.build());
                    // Close the dialog and return back to the parent activity
                    dismiss();
                }
            });
        alertDialogBuilder.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dismiss();
                }
            });
        AlertDialog dialog = alertDialogBuilder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onResume() {
        // Get existing layout params for the window
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        // Assign window properties to fill the parent
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        // Call super onResume after sizing
        super.onResume();
    }

    // TODO: Figure out why this isn't quite working as expected
    // Fires whenever the text field has an action performed
    // In this case, when the "Done" button is pressed
    // REQUIRES a 'soft keyboard' (virtual keyboard)
//    @Override
//    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//        if (EditorInfo.IME_ACTION_DONE == actionId) {
//            // Return input text back to activity through the implemented listener
//            EditTodoDialogListener listener = (EditTodoDialogListener) getActivity();
//            Bundle args = getArguments();
//            listener.onFinishEditDialog(args.getInt(TODO_POS_KEY),
//                    args.getLong(TODO_ID_KEY),
//                    v.getText().toString());
//            dismiss();
//            return true;
//        }
//        return false;
//    }

    // saved snippets:
    // implements TextView.OnEditorActionListener
    // editText.setOnEditorActionListener(this);
    // dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

}
