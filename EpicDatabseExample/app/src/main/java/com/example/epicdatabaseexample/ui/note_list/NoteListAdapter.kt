package com.example.epicdatabaseexample.ui.note_list

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.epicdatabaseexample.db.note.NoteEntity

class NoteListAdapter(
    private val onNoteClick: (noteId: Int) -> Unit,
    private val onIsCompletedClick: (note: NoteEntity) -> Unit
) : ListAdapter<NoteEntity, NoteViewHolder>(NoteDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder.create(parent)
    }


    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = getItem(position)
        holder.title.text = note.title
        holder.itemView.setOnClickListener {
            onNoteClick.invoke(note.id)
        }
        holder.personName.text = note.personName
        // TODO Раскомментировать, после добавления нового поля в NoteEntity.
        holder.isCompleted.setOnClickListener {
            onIsCompletedClick.invoke(note.copy(
                isCompleted = !note.isCompleted
            ))
        }
        val isCompletedChanged = note.isCompleted != holder.isCompleted.isChecked
        if (isCompletedChanged) {
            holder.isCompleted.isChecked = note.isCompleted
        }
    }

    override fun onBindViewHolder(holder: NoteViewHolder,
                                  position: Int,
                                  payloads: MutableList<Any>
    ) {
        val payload: NoteDiffUtilCallback.Payload? =
            payloads.firstOrNull() as? NoteDiffUtilCallback.Payload
        if (payload != null) {
            if (payload.isCompeteChanged) {
                val note = getItem(position)
                holder.isCompleted.isChecked = note.isCompleted
            }
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }
}