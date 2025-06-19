// NotebookListAdapter.java
package com.example.daftarak.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.daftarak.R;
import com.example.daftarak.data.model.Notebook;

import java.util.List;

public class NotebookListAdapter extends RecyclerView.Adapter<NotebookListAdapter.NotebookViewHolder> {

    public interface OnNotebookClickListener {
        void onNotebookClick(Notebook notebook);
    }

    private List<Notebook> notebooks;
    private final OnNotebookClickListener listener;

    public NotebookListAdapter(List<Notebook> notebooks, OnNotebookClickListener listener) {
        this.notebooks = notebooks;
        this.listener = listener;
    }

    public void setNotebooks(List<Notebook> notebooks) {
        this.notebooks = notebooks;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NotebookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new NotebookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotebookViewHolder holder, int position) {
        final Notebook notebook = notebooks.get(position);
        holder.textView.setText(notebook.getTitle());
        holder.itemView.setOnClickListener(v -> listener.onNotebookClick(notebook));
    }

    @Override
    public int getItemCount() {
        return notebooks != null ? notebooks.size() : 0;
    }

    static class NotebookViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        NotebookViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }
    }
}
