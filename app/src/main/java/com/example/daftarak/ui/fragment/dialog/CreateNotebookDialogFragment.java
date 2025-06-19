package com.example.daftarak.ui.fragment.dialog;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.daftarak.R;
import com.example.daftarak.data.model.Notebook;
import com.example.daftarak.ui.viewmodel.NotebookViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class CreateNotebookDialogFragment extends BottomSheetDialogFragment {

    private static final String ARG_NOTEBOOK_ID = "notebook_id";
    private static final String ARG_NOTEBOOK_TITLE = "notebook_title";

    private NotebookViewModel notebookViewModel;
    private int notebookId = 0; // 0 means new notebook, otherwise editing existing
    private EditText titleEditText;
    private Button btnCreate;
    private TextView dialogTitle;

    // Factory method for creation mode
    public static CreateNotebookDialogFragment newInstance() {
        return new CreateNotebookDialogFragment();
    }

    // Factory method for editing mode
    public static CreateNotebookDialogFragment newInstance(int id, String title) {
        CreateNotebookDialogFragment fragment = new CreateNotebookDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_NOTEBOOK_ID, id);
        args.putString(ARG_NOTEBOOK_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_notebook_dialog, container, false);

        notebookViewModel = new ViewModelProvider(requireActivity()).get(NotebookViewModel.class);

        titleEditText = view.findViewById(R.id.editTextTitle);
        btnCreate = view.findViewById(R.id.btnCreate);
        Button btnCancel = view.findViewById(R.id.btnCancel);
        dialogTitle = view.findViewById(R.id.dialogTitle);

        // Check if we have arguments for editing
        if (getArguments() != null) {
            notebookId = getArguments().getInt(ARG_NOTEBOOK_ID, 0);
            String title = getArguments().getString(ARG_NOTEBOOK_TITLE, "");

            if (notebookId != 0) { // editing mode
                titleEditText.setText(title);
                btnCreate.setText(R.string.update);
                dialogTitle.setText(R.string.edit_notebook);
            }
        }

        btnCreate.setOnClickListener(v -> {
            String title = titleEditText.getText().toString().trim();
            if (TextUtils.isEmpty(title)) {
                Toast.makeText(getContext(), R.string.title_cannot_be_empty, Toast.LENGTH_SHORT).show();
                return;
            }

            if (notebookId == 0) {
                // Create new notebook
                Notebook notebook = new Notebook(0, title, System.currentTimeMillis());
                notebookViewModel.insert(notebook);
                Toast.makeText(getContext(), R.string.created_new_notebook, Toast.LENGTH_SHORT).show();
            } else {
                // Update existing notebook
                Notebook notebook = new Notebook(notebookId, title, System.currentTimeMillis());
                notebookViewModel.update(notebook);
                Toast.makeText(getContext(), R.string.notebook_updated, Toast.LENGTH_SHORT).show();
            }
            dismiss();
        });

        btnCancel.setOnClickListener(v -> dismiss());

        return view;
    }
}
