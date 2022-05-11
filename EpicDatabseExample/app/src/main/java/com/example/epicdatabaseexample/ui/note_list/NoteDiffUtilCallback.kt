package com.example.epicdatabaseexample.ui.note_list

import androidx.recyclerview.widget.DiffUtil
import com.example.epicdatabaseexample.db.note.NoteEntity

class NoteDiffUtilCallback : DiffUtil.ItemCallback<NoteEntity>() {

    override fun areItemsTheSame(oldItem: NoteEntity, newItem: NoteEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: NoteEntity, newItem: NoteEntity): Boolean {
        return false
    }
}