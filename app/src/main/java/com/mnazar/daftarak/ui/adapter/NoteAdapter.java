package com.mnazar.daftarak.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mnazar.daftarak.R;
import com.mnazar.daftarak.data.model.Note;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private List<Note> notes;

    public interface OnNoteClickListener {
        void onNoteClick(Note note);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(Note note);
    }

    private final OnNoteClickListener noteClickListener;
    private final OnDeleteClickListener deleteClickListener;

    public NoteAdapter(List<Note> notes,
                       OnNoteClickListener noteClickListener,
                       OnDeleteClickListener deleteClickListener) {
        this.notes = notes;
        this.noteClickListener = noteClickListener;
        this.deleteClickListener = deleteClickListener;
    }

    public void setNotes(List<Note> newNotes) {
        this.notes = newNotes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = notes.get(position);
        holder.bind(note, noteClickListener, deleteClickListener);
    }

    @Override
    public int getItemCount() {
        return notes != null ? notes.size() : 0;
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, bodyTextView, dateTextView;
        ImageButton deleteButton;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.txtNoteTitle);
            bodyTextView = itemView.findViewById(R.id.txtNoteBody);
            dateTextView = itemView.findViewById(R.id.txtNoteDate);
            deleteButton = itemView.findViewById(R.id.btnDeleteNote);
        }

        public void bind(Note note,
                         OnNoteClickListener clickListener,
                         OnDeleteClickListener deleteListener) {
            titleTextView.setText(note.getTitle());
            bodyTextView.setText(note.getBody());

            String formattedDate = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                    .format(new Date(note.getCreatedAt()));
            dateTextView.setText(formattedDate);

            // Click on item
            itemView.setOnClickListener(v -> clickListener.onNoteClick(note));

            // Click on delete icon
            deleteButton.setOnClickListener(v -> deleteListener.onDeleteClick(note));
        }
    }
}
