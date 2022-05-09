package com.example.epicdatabaseexample.ui.note_list

import com.example.epicdatabaseexample.db.note.NoteEntity

data class NoteListViewState(
    val noteList: List<NoteEntity> = emptyList()
)