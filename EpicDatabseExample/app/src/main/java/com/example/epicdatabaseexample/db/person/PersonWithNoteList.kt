package com.example.epicdatabaseexample.db.person

import androidx.room.Embedded
import androidx.room.Relation
import com.example.epicdatabaseexample.db.note.NoteEntity

data class PersonWithNoteList(

    @Embedded
    val person: PersonEntity,

    @Relation(
        parentColumn = PersonEntity.COLUMN_NAME,
        entityColumn = NoteEntity.COLUMN_PERSON_NAME,
    )
    val personList: List<PersonEntity>
)