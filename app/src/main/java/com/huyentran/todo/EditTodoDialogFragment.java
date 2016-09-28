package com.huyentran.todo;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.huyentran.todo.model.Todo;

import static com.huyentran.todo.MainActivity.TODO_ID_KEY;
import static com.huyentran.todo.MainActivity.TODO_POS_KEY;
import static com.huyentran.todo.MainActivity.TODO_VALUE_KEY;
import static com.huyentran.todo.R.id.etEditTodo;

/**
 * {@link DialogFragment} using {@link AlertDialog} for editing todo items in a modal view.
 */
public class EditTodoDialogFragment extends DialogFragment {

    public interface EditTodoDialogListener {
        void onFinishEditDialog(int pos, long id, String value);
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
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(getResources().getString(R.string.tv_edit_todo_label));
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View dialogView = inflater.inflate(R.layout.fragment_edit_todo, null);
        alertDialogBuilder.setView(dialogView);
        // Setup edit text field
        final EditText editText = (EditText) dialogView.findViewById(etEditTodo);
        String oldValue = getArguments().getString(TODO_VALUE_KEY);
        editText.setText(oldValue);
        editText.setSelection(oldValue.length());
        // Wiring buttons
        alertDialogBuilder.setPositiveButton(R.string.btn_save, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Return input text back to activity through the implemented listener
                    EditTodoDialogListener listener = (EditTodoDialogListener) getActivity();
                    Bundle args = getArguments();
                    listener.onFinishEditDialog(args.getInt(TODO_POS_KEY),
                            args.getLong(TODO_ID_KEY),
                            editText.getText().toString());
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

        return alertDialogBuilder.create();
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
