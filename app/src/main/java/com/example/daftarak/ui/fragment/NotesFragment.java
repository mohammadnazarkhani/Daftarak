package com.example.daftarak.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.daftarak.R;
import com.example.daftarak.data.model.Note;
import com.example.daftarak.data.repository.NoteRepository;
import com.example.daftarak.ui.adapter.NoteAdapter;
import com.example.daftarak.ui.fragment.dialog.AddNoteDialogFragment;
import com.example.daftarak.ui.fragment.dialog.NotebookListBottomSheetDialogFragment;
import com.example.daftarak.ui.fragment.dialog.SearchOptionsBottomSheetDialog;
import com.example.daftarak.ui.fragment.dialog.SortOptionsBottomSheetDialogFragment;
import com.example.daftarak.ui.viewmodel.NoteViewModel;
import com.example.daftarak.ui.widgets.SearchToolbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.example.daftarak.ui.fragment.dialog.SortOptionsBottomSheetDialogFragment.REQUEST_KEY_SORT_OPTIONS;
import static com.example.daftarak.ui.fragment.dialog.SortOptionsBottomSheetDialogFragment.RESULT_KEY_SORT_BY;
import static com.example.daftarak.ui.fragment.dialog.SortOptionsBottomSheetDialogFragment.RESULT_KEY_ASCENDING;


public class NotesFragment extends Fragment implements
        SearchToolbar.OnSearchListener {

    private static final String ARG_NOTEBOOK_ID = "notebook_id";
    private Integer notebookId = null;

    private RecyclerView notesRecyclerView;
    private NoteAdapter noteAdapter;
    private NoteViewModel noteViewModel;
    private SearchToolbar searchToolbar;

    private List<Note> allNotes = new ArrayList<>();
    private String searchQuery = "";
    private boolean searchTitle = true, searchBody = true;
    private boolean sortByTitle = false, sortAsc = true;

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

        searchToolbar = view.findViewById(R.id.searchToolbar);
        searchToolbar.setSearchListener(this);

        notesRecyclerView = view.findViewById(R.id.notesRecyclerView);
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        noteAdapter = new NoteAdapter(new ArrayList<>(),
                note -> AddNoteDialogFragment.newInstance(notebookId, note)
                        .show(getParentFragmentManager(), "EditNoteDialog"),
                note -> new NoteRepository(requireContext()).deleteNote(note)
        );
        notesRecyclerView.setAdapter(noteAdapter);

        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);

        if (notebookId != null && notebookId != -1) {
            loadNotesSorted();
        } else {
            new NotebookListBottomSheetDialogFragment()
                    .show(requireActivity().getSupportFragmentManager(), "NotebookListBottomSheet");
        }

        getParentFragmentManager().setFragmentResultListener(
                SearchOptionsBottomSheetDialog.RESULT_KEY,
                getViewLifecycleOwner(),
                (requestKey, bundle) -> {
                    boolean titleChecked = bundle.getBoolean(SearchOptionsBottomSheetDialog.RESULT_TITLE, true);
                    boolean bodyChecked = bundle.getBoolean(SearchOptionsBottomSheetDialog.RESULT_BODY, true);

                    searchTitle = titleChecked;
                    searchBody = bodyChecked;

                    filterAndSortNotes();
                });

        getParentFragmentManager().setFragmentResultListener(
                REQUEST_KEY_SORT_OPTIONS,
                getViewLifecycleOwner(),
                (requestKey, bundle) -> {
                    sortByTitle = "title".equals(bundle.getString(RESULT_KEY_SORT_BY, "date"));
                    sortAsc = bundle.getBoolean(RESULT_KEY_ASCENDING, true);
                    loadNotesSorted();
                }
        );

    }

    private void loadNotesSorted() {
        NoteRepository repo = new NoteRepository(requireContext());
        repo.getNotesByNotebookIdSorted(notebookId, sortByTitle ? "title" : "date", sortAsc)
                .observe(getViewLifecycleOwner(), notes -> {
                    allNotes = notes;
                    filterAndSortNotes(); // Only applies search query filtering
                });
    }

    private void filterAndSortNotes() {
        List<Note> filtered = new ArrayList<>();
        for (Note note : allNotes) {
            boolean matches = (searchTitle && note.getTitle().toLowerCase().contains(searchQuery.toLowerCase()))
                    || (searchBody && note.getBody().toLowerCase().contains(searchQuery.toLowerCase()));
            if (searchQuery.isEmpty() || matches) filtered.add(note);
        }
        noteAdapter.setNotes(filtered);
    }

    @Override
    public void onSearchTextChanged(String text) {
        this.searchQuery = text;
        filterAndSortNotes();
    }

    @Override
    public void onSearchConfirmed(String query) {
        this.searchQuery = query;
        filterAndSortNotes();
    }

    @Override
    public void onSearchOptionsClick() {
        Bundle args = new Bundle();
        args.putBoolean("search_title", searchTitle);
        args.putBoolean("search_body", searchBody);
        NavHostFragment.findNavController(this)
                .navigate(R.id.searchOptionsBottomSheetDialog, args);
    }

    @Override
    public void onSortTypeClick() {
        Bundle args = new Bundle();
        args.putString("sort_by", sortByTitle ? "title" : "date");
        args.putBoolean("ascending", sortAsc);
        NavHostFragment.findNavController(this)
                .navigate(R.id.sortOptionsBottomSheetDialog, args);
    }
}
