package com.example.epicdatabaseexample.db.person

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.epicdatabaseexample.db.NoteDatabase

@Entity(tableName = NoteDatabase.PERSON_TABLE)
data class PersonEntity(

    @PrimaryKey
    @ColumnInfo(name = COLUMN_NAME)
    val title: String,
) {

    companion object {
        const val COLUMN_NAME = "name"
        const val DEFAULT_PERSON_NAME = "UNKNOWN"
    }
}