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
import com.example.daftarak.ui.adapter.NotebookAdapter;
import com.example.daftarak.ui.viewmodel.NotebookViewModel;

public class NotebooksFragment extends Fragment {

    private NotebookViewModel viewModel;
    private NotebookAdapter adapter;
    private RecyclerView rvNotebooks;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notebooks, container, false);

        // Step 1: Init RecyclerView
        rvNotebooks = view.findViewById(R.id.rvNotebooks);
        rvNotebooks.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new NotebookAdapter(notebook -> {
                Bundle bundle = new Bundle();
                bundle.putInt("notebookId", notebook.getId());

                NavHostFragment.findNavController(this).navigate(R.id.notesFragment, bundle);
            });

            rvNotebooks.setAdapter(adapter);

        // Step 2: Setup ViewModel
        viewModel = new ViewModelProvider(this).get(NotebookViewModel.class);

        // Step 3: Observe notebooks LiveData
        viewModel.getAllNotebooks().observe(getViewLifecycleOwner(), notebooks -> {
            // Update the adapter's data
            adapter.setNotebooks(notebooks);
        });

        return view;
    }
}
