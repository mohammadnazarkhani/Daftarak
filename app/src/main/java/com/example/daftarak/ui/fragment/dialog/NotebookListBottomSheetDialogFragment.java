// NotebookListBottomSheetDialogFragment.java
package com.example.daftarak.ui.fragment.dialog;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.daftarak.R;
import com.example.daftarak.data.model.Notebook;
import com.example.daftarak.data.repository.NotebookRepository;
import com.example.daftarak.ui.adapter.NotebookListAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class NotebookListBottomSheetDialogFragment extends BottomSheetDialogFragment {

    private RecyclerView recyclerView;
    private NotebookListAdapter adapter;
    private NotebookRepository notebookRepository;
    private NavController navController;

    public NotebookListBottomSheetDialogFragment() {
        super(R.layout.fragment_notebook_list_bottom_sheet_dialog);
    }

    @Override
    public void onViewCreated(@NonNull android.view.View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerViewNotebooks);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter = new NotebookListAdapter(new ArrayList<>(), this::onNotebookClicked);
        recyclerView.setAdapter(adapter);

        navController = NavHostFragment.findNavController(this);

        notebookRepository = new NotebookRepository(requireContext());
        notebookRepository.getAllNotebooks().observe(getViewLifecycleOwner(), notebooks -> {
            if (notebooks != null) {
                adapter.setNotebooks(notebooks);
            }
        });
    }

    private void onNotebookClicked(Notebook notebook) {
        // Prepare bundle with notebook id and navigate to notesFragment
        Bundle args = new Bundle();
        args.putInt("notebook_id", notebook.getId());

        navController.navigate(R.id.notesFragment, args);

        dismiss();  // Close the bottom sheet
    }
}
