package com.example.daftarak.ui.adapter;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.daftarak.R;
import com.example.daftarak.data.model.Notebook;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NotebookAdapter extends RecyclerView.Adapter<NotebookAdapter.NotebookViewHolder> {

    private List<Notebook> notebooks = new ArrayList<>();
    private final OnNotebookClickListener listener;
    private final OnNotebookContextMenuListener contextMenuListener;

    public interface OnNotebookClickListener {
        void onClick(Notebook notebook);
    }

    public interface OnNotebookContextMenuListener {
        void onEdit(Notebook notebook);
        void onDelete(Notebook notebook);
    }

    public List<Notebook> getNotebooks() {
        return notebooks;
    }

    public NotebookAdapter(OnNotebookClickListener listener, OnNotebookContextMenuListener contextMenuListener) {
        this.listener = listener;
        this.contextMenuListener = contextMenuListener;
    }

    @NonNull
    @Override
    public NotebookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notebook, parent, false);
        return new NotebookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotebookViewHolder holder, int position) {
        Notebook notebook = notebooks.get(position);
        holder.title.setText(notebook.getTitle());

        String formattedDate = DateFormat.getDateTimeInstance().format(new Date(notebook.getCreatedAt()));
        holder.subtitle.setText(formattedDate);

        holder.itemView.setOnClickListener(v -> listener.onClick(notebook));

        // Store notebook in holder for context menu callbacks
        holder.currentNotebook = notebook;
    }

    @Override
    public int getItemCount() {
        return notebooks.size();
    }

    public void setNotebooks(List<Notebook> notebooks) {
        this.notebooks = notebooks;
        notifyDataSetChanged();
    }

    class NotebookViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        TextView title, subtitle;
        Notebook currentNotebook; // to keep track of the notebook for the context menu

        public NotebookViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.txtNotebookTitle);
            subtitle = itemView.findViewById(R.id.txtNotebookSubtitle);

            // Register context menu for long press
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            // Inflate your menu here
            menu.setHeaderTitle("Select Action");
            menu.add(this.getAdapterPosition(), R.id.menu_edit, 0, "Edit")
                    .setOnMenuItemClickListener(onEditMenu);
            menu.add(this.getAdapterPosition(), R.id.menu_delete, 1, "Delete")
                    .setOnMenuItemClickListener(onDeleteMenu);
        }

        private final MenuItem.OnMenuItemClickListener onEditMenu = item -> {
            if (contextMenuListener != null && currentNotebook != null) {
                contextMenuListener.onEdit(currentNotebook);
                return true;
            }
            return false;
        };

        private final MenuItem.OnMenuItemClickListener onDeleteMenu = item -> {
            if (contextMenuListener != null && currentNotebook != null) {
                contextMenuListener.onDelete(currentNotebook);
                return true;
            }
            return false;
        };
    }
}
