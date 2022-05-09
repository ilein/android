package com.example.epicdatabaseexample.ui.note_detail

import com.example.epicdatabaseexample.db.person.PersonEntity

data class NoteDetailViewState(
    val noteId: Int?,
    val title: String = "",
    val description: String = "",
    val personName: String = PersonEntity.DEFAULT_PERSON_NAME
)