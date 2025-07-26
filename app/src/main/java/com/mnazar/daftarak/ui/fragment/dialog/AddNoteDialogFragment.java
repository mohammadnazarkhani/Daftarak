package com.mnazar.daftarak.ui.fragment.dialog;

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

import com.mnazar.daftarak.R;
import com.mnazar.daftarak.data.model.Note;
import com.mnazar.daftarak.data.repository.NoteRepository;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AddNoteDialogFragment extends BottomSheetDialogFragment {

    private static final String ARG_NOTEBOOK_ID = "notebook_id";
    private static final String ARG_NOTE = "note";

    private EditText editTextTitle;
    private EditText editTextBody;
    private Button btnAdd, btnCancel, btnDelete;
    private TextView dialogTitle;

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
        btnDelete = view.findViewById(R.id.btnDeleteNote);
        dialogTitle = view.findViewById(R.id.dialogTitleNote);

        if (existingNote != null) {
            editTextTitle.setText(existingNote.getTitle());
            editTextBody.setText(existingNote.getBody());
            btnAdd.setText(R.string.update);
            dialogTitle.setText(R.string.edit_note);
            btnDelete.setVisibility(View.VISIBLE);
        } else {
            btnDelete.setVisibility(View.GONE);
        }

        btnCancel.setOnClickListener(v -> dismiss());

        btnAdd.setOnClickListener(v -> {
            String title = editTextTitle.getText().toString().trim();
            String body = editTextBody.getText().toString().trim();

            if (TextUtils.isEmpty(title)) {
                editTextTitle.setError(getString(R.string.title_empty_error));
                return;
            }

            NoteRepository repository = new NoteRepository(requireContext());

            if (existingNote != null) {
                existingNote.setTitle(title);
                existingNote.setBody(body);
                repository.updateNote(existingNote);
                Toast.makeText(requireContext(), R.string.note_updated, Toast.LENGTH_SHORT).show();
            } else {
                Note newNote = new Note(title, body, System.currentTimeMillis(), notebookId);
                repository.insertNote(newNote);
                Toast.makeText(requireContext(), R.string.note_added, Toast.LENGTH_SHORT).show();
            }

            dismiss();
        });

        btnDelete.setOnClickListener(v -> {
            if (existingNote != null) {
                new NoteRepository(requireContext()).deleteNote(existingNote);
                Toast.makeText(requireContext(), R.string.note_deleted, Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

        return view;
    }
}
