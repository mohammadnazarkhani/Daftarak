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
    private static final String ARG_NOTE = "note";

    private EditText editTextTitle;
    private EditText editTextBody;
    private Button btnAdd;
    private Button btnCancel;

    private int notebookId;
    private Note existingNote;

    public static AddNoteDialogFragment newInstance(int notebookId) {
        AddNoteDialogFragment fragment = new AddNoteDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_NOTEBOOK_ID, notebookId);
        fragment.setArguments(args);
        return fragment;
    }

    public static AddNoteDialogFragment newInstance(int notebookId, Note noteToEdit) {
        AddNoteDialogFragment fragment = new AddNoteDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_NOTEBOOK_ID, notebookId);
        args.putSerializable(ARG_NOTE, noteToEdit);
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

        if (getArguments() != null) {
            notebookId = getArguments().getInt(ARG_NOTEBOOK_ID, -1);
            existingNote = (Note) getArguments().getSerializable(ARG_NOTE);
        }

        editTextTitle = view.findViewById(R.id.editTextNoteTitle);
        editTextBody = view.findViewById(R.id.editTextNoteBody);
        btnAdd = view.findViewById(R.id.btnAddNote);
        btnCancel = view.findViewById(R.id.btnCancelNote);

        // Pre-fill fields if editing
        if (existingNote != null) {
            editTextTitle.setText(existingNote.getTitle());
            editTextBody.setText(existingNote.getBody());
            btnAdd.setText(R.string.update); // Change text from "Add" to "Update"
        }

        btnCancel.setOnClickListener(v -> dismiss());

        btnAdd.setOnClickListener(v -> {
            String title = editTextTitle.getText().toString().trim();
            String body = editTextBody.getText().toString().trim();

            if (TextUtils.isEmpty(title)) {
                editTextTitle.setError("Title cannot be empty");
                return;
            }

            NoteRepository repository = new NoteRepository(requireContext());

            if (existingNote != null) {
                // Edit mode
                existingNote.setTitle(title);
                existingNote.setBody(body);
                repository.updateNote(existingNote);
                Toast.makeText(requireContext(), "Note updated", Toast.LENGTH_SHORT).show();
            } else {
                // Add mode
                Note newNote = new Note(title, body, System.currentTimeMillis(), notebookId);
                repository.insertNote(newNote);
                Toast.makeText(requireContext(), "Note added", Toast.LENGTH_SHORT).show();
            }

            dismiss();
        });

        return view;
    }
}
