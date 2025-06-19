package com.example.daftarak.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.daftarak.R;
import com.example.daftarak.ui.adapter.NoteAdapter;
import com.example.daftarak.ui.viewmodel.NoteViewModel;

import java.util.ArrayList;

public class NotesFragment extends Fragment {

    private static final String ARG_NOTEBOOK_ID = "notebook_id";
    private Integer notebookId = null; // nullable

    private RecyclerView notesRecyclerView;
    private NoteAdapter noteAdapter;
    private NoteViewModel noteViewModel;

    public NotesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().containsKey(ARG_NOTEBOOK_ID)) {
            notebookId = getArguments().getInt(ARG_NOTEBOOK_ID);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        notesRecyclerView = view.findViewById(R.id.notesRecyclerView);
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        noteAdapter = new NoteAdapter(new ArrayList<>());
        notesRecyclerView.setAdapter(noteAdapter);

        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);

        if (notebookId != null && notebookId != -1) {
            // Show notes only for the selected notebook
            noteViewModel.getNotesByNotebookId(notebookId).observe(getViewLifecycleOwner(), notes -> {
                noteAdapter.setNotes(notes);
            });
        } else {
            // Show all notes if no notebook ID was passed
            noteViewModel.getAllNotes().observe(getViewLifecycleOwner(), notes -> {
                noteAdapter.setNotes(notes);
            });
        }
    }
}
