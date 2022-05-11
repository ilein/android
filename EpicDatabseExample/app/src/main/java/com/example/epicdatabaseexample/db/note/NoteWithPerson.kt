package com.example.epicdatabaseexample.db.note

import androidx.room.Embedded
import androidx.room.Relation
import com.example.epicdatabaseexample.db.person.PersonEntity

data class NoteWithPerson(

    @Embedded
    val note: NoteEntity,

    @Relation(
        parentColumn = NoteEntity.COLUMN_PERSON_NAME,
        entityColumn = PersonEntity.COLUMN_NAME,
    )
    val person: PersonEntity?,
)