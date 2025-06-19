package com.example.daftarak.ui.fragment.dialog;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.daftarak.R;
import com.example.daftarak.data.model.Notebook;
import com.example.daftarak.ui.viewmodel.NotebookViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class CreateNotebookDialogFragment extends BottomSheetDialogFragment {

    private NotebookViewModel notebookViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_notebook_dialog, container, false);

        notebookViewModel = new ViewModelProvider(requireActivity()).get(NotebookViewModel.class);

        EditText titleEditText = view.findViewById(R.id.editTextTitle);
        Button btnCreate = view.findViewById(R.id.btnCreate);
        Button btnCancel = view.findViewById(R.id.btnCancel);

        btnCreate.setOnClickListener(v -> {
            String title = titleEditText.getText().toString().trim();
            if (TextUtils.isEmpty(title)) {
                Toast.makeText(getContext(), "Title cannot be empty", Toast.LENGTH_SHORT).show();
            } else {
                Notebook notebook = new Notebook(0, title, System.currentTimeMillis());
                notebookViewModel.insert(notebook);
                Toast.makeText(getContext(), "Created New Notebook.", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

        btnCancel.setOnClickListener(v -> dismiss());

        return view;
    }
}
