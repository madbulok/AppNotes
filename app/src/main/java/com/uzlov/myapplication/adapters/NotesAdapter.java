package com.uzlov.myapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.uzlov.myapplication.Note;
import com.uzlov.myapplication.OnNoteItemClick;
import com.uzlov.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {

    private final List<Note> notes = new ArrayList<>();
    private final OnNoteItemClick onNoteItemClick;

    public NotesAdapter(OnNoteItemClick onNoteItemClick) {
        this.onNoteItemClick = onNoteItemClick;
    }

    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesAdapter.NoteViewHolder holder, int position) {
        holder.inBind(notes.get(position));
    }

    public void setNotes(List<Note> _notes){
        notes.clear();
        notes.addAll(_notes);
        notifyDataSetChanged();
    }

    public void addNote(Note note){
        notes.add(note);
        notifyItemInserted(notes.size()-1);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public void removeNote(Note note) {
        int pos = notes.indexOf(note);
        if (pos != -1 && notes.remove(note)){
            notifyItemRemoved(pos);
        }
    }


    protected final class NoteViewHolder extends RecyclerView.ViewHolder{

        private final AppCompatImageView ivNote;
        private final AppCompatTextView tvNoteTitle;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            ivNote = itemView.findViewById(R.id.item_image_note);
            tvNoteTitle = itemView.findViewById(R.id.item_title);
        }

        void inBind(Note note){
            if (note == null) return;
            ivNote.setImageResource(note.getImage());
            tvNoteTitle.setText(note.getName());
            itemView.setOnClickListener(v -> onNoteItemClick.onClick(getAdapterPosition()));
        }
    }
}
