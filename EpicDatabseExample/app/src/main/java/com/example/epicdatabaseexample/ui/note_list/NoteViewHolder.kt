package com.example.epicdatabaseexample.ui.note_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.epicdatabaseexample.R

class NoteViewHolder private constructor(
    itemView: View,
    val title: TextView = itemView.findViewById(R.id.item_title),
    val personName: TextView = itemView.findViewById(R.id.item_person_name),
    val isCompleted: CheckBox = itemView.findViewById(R.id.is_completed)
) : RecyclerView.ViewHolder(itemView) {

    companion object {

        fun create(parent: ViewGroup): NoteViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val itemView = layoutInflater.inflate(R.layout.item_note, parent, false)
            return NoteViewHolder(itemView)
        }
    }
}