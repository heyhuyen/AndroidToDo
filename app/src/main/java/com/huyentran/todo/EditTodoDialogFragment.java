package com.huyentran.todo;

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
import android.widget.DatePicker;
import android.widget.EditText;

import com.huyentran.todo.model.Todo;
import com.huyentran.todo.util.DateUtils;

import java.util.Calendar;

import static com.huyentran.todo.MainActivity.TODO_DUE_DATE_KEY;
import static com.huyentran.todo.MainActivity.TODO_ID_KEY;
import static com.huyentran.todo.MainActivity.TODO_POS_KEY;
import static com.huyentran.todo.MainActivity.TODO_VALUE_KEY;

/**
 * {@link DialogFragment} using {@link AlertDialog} for editing todo items in a modal view.
 */
public class EditTodoDialogFragment extends DialogFragment {

    public interface EditTodoDialogListener {
        void onFinishEditDialog(int pos, long id, String value, String dueDate);
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
        // Setup date picker
        final DatePicker dpDueDate = (DatePicker) dialogView.findViewById(R.id.dpDueDate);
        Calendar dueDate = DateUtils.getDateFromString(args.getString(TODO_DUE_DATE_KEY));
        dpDueDate.updateDate(dueDate.get(Calendar.YEAR), dueDate.get(Calendar.MONTH),
                dueDate.get(Calendar.DAY_OF_MONTH));
        // Wiring buttons
        alertDialogBuilder.setPositiveButton(R.string.btn_save, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Return input text back to activity through the implemented listener
                    EditTodoDialogListener listener = (EditTodoDialogListener) getActivity();
                    Bundle args = getArguments();
                    listener.onFinishEditDialog(args.getInt(TODO_POS_KEY),
                            args.getLong(TODO_ID_KEY),
                            etValue.getText().toString(),
                            DateUtils.getDateStringFromPicker(dpDueDate));
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
