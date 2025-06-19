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

import com.example.daftarak.R;
import com.example.daftarak.data.model.Note;
import com.example.daftarak.data.repository.NoteRepository;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AddNoteDialogFragment extends BottomSheetDialogFragment {

    private static final String ARG_NOTEBOOK_ID = "notebook_id";

    private EditText editTextTitle;
    private EditText editTextBody;
    private Button btnAdd;
    private Button btnCancel;

    private int notebookId;

    public static AddNoteDialogFragment newInstance(int notebookId) {
        AddNoteDialogFragment fragment = new AddNoteDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_NOTEBOOK_ID, notebookId);
        fragment.setArguments(args);
        return fragment;
    }

    public AddNoteDialogFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_note_dialog, container, false);

        // Extract notebookId from args
        if (getArguments() != null) {
            notebookId = getArguments().getInt(ARG_NOTEBOOK_ID, -1);
        }

        // Bind views
        editTextTitle = view.findViewById(R.id.editTextNoteTitle);
        editTextBody = view.findViewById(R.id.editTextNoteBody);
        btnAdd = view.findViewById(R.id.btnAddNote);
        btnCancel = view.findViewById(R.id.btnCancelNote);

        // Cancel button dismisses dialog
        btnCancel.setOnClickListener(v -> dismiss());

        // Add button logic
        btnAdd.setOnClickListener(v -> {
            String title = editTextTitle.getText().toString().trim();
            String body = editTextBody.getText().toString().trim();

            if (TextUtils.isEmpty(title)) {
                editTextTitle.setError("Title cannot be empty");
                return;
            }

            Note note = new Note(title, body, System.currentTimeMillis(), notebookId);
            NoteRepository repository = new NoteRepository(requireContext());
            repository.insertNote(note);

            Toast.makeText(requireContext(), "Note added", Toast.LENGTH_SHORT).show();
            dismiss();
        });

        return view;
    }
}
