package com.example.daftarak.ui.adapter;

import android.view.LayoutInflater;
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

    public interface OnNotebookClickListener {
        void onClick(Notebook notebook);
    }

    public NotebookAdapter(OnNotebookClickListener listener) {
        this.listener = listener;
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

        // Format createdAt timestamp into readable date string
        String formattedDate = DateFormat.getDateTimeInstance().format(new Date(notebook.getCreatedAt()));
        holder.subtitle.setText(formattedDate);

        // Handle click
        holder.itemView.setOnClickListener(v -> listener.onClick(notebook));
    }

    @Override
    public int getItemCount() {
        return notebooks.size();
    }

    public void setNotebooks(List<Notebook> notebooks) {
        this.notebooks = notebooks;
        notifyDataSetChanged();
    }

    static class NotebookViewHolder extends RecyclerView.ViewHolder {
        TextView title, subtitle;

        public NotebookViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.txtNotebookTitle);
            subtitle = itemView.findViewById(R.id.txtNotebookSubtitle);
        }
    }
}
