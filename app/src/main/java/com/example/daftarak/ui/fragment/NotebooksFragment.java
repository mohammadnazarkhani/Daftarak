package com.example.daftarak.ui.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import com.example.daftarak.data.model.Notebook;
import com.example.daftarak.ui.adapter.NotebookAdapter;
import com.example.daftarak.ui.fragment.dialog.CreateNotebookDialogFragment;
import com.example.daftarak.ui.viewmodel.NotebookViewModel;

public class NotebooksFragment extends Fragment {

    private NotebookViewModel viewModel;
    private NotebookAdapter adapter;
    private RecyclerView rvNotebooks;
    private int selectedPosition = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notebooks, container, false);

        rvNotebooks = view.findViewById(R.id.rvNotebooks);
        rvNotebooks.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter = new NotebookAdapter(
                notebook -> {
                    // Navigate to notes fragment
                    Bundle bundle = new Bundle();
                    bundle.putInt("notebook_id", notebook.getId());
                    NavHostFragment.findNavController(this).navigate(R.id.notesFragment, bundle);
                },
                new NotebookAdapter.OnNotebookContextMenuListener() {
                    @Override
                    public void onEdit(Notebook notebook) {
                        CreateNotebookDialogFragment dialog = CreateNotebookDialogFragment
                                .newInstance(notebook.getId(), notebook.getTitle());
                        dialog.show(getParentFragmentManager(), "editNotebook");
                    }

                    @Override
                    public void onDelete(Notebook notebook) {
                        showDeleteConfirmationDialog(notebook);
                    }
                }
        );

        rvNotebooks.setAdapter(adapter);
        registerForContextMenu(rvNotebooks);

        viewModel = new ViewModelProvider(this).get(NotebookViewModel.class);
        viewModel.getAllNotebooks().observe(getViewLifecycleOwner(), adapter::setNotebooks);

        return view;
    }

    private void showDeleteConfirmationDialog(Notebook notebook) {
        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.delete_notebook)
                .setMessage(R.string.are_you_sure_you_want_to_delete_this_notebook)
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    viewModel.delete(notebook);
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        return super.onContextItemSelected(item);
    }
}
